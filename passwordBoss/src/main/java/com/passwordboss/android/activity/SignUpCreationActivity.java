package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import com.passwordboss.android.BuildConfig;
import com.passwordboss.android.R;
import com.passwordboss.android.app.Installation;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.crypto.CryptoHelper;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.database.bll.UserInfoBll;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.event.SyncResultEvent;
import com.passwordboss.android.http.NullResponseError;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.ServerException;
import com.passwordboss.android.http.UnexpectedServerError;
import com.passwordboss.android.http.beans.AuthenticationHttpBean;
import com.passwordboss.android.http.beans.DeviceHttpBean;
import com.passwordboss.android.http.beans.DevicePostHttpBean;
import com.passwordboss.android.http.beans.ErrorHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.task.SyncTask;
import com.passwordboss.android.utils.LanguagesUtils;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.crypto.utils.pem.CryptoPemHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.security.KeyPair;
import java.sql.SQLException;

public class SignUpCreationActivity extends BaseActivity {
    private final static Object LOCK = new Object();
    private static final String KEY_EMAIL = "keyEmail";
    private static final String KEY_PASSWORD = "keyPassword";
    private static Thread sTask;

    public static void start(Context context, String email, String password) {
        Intent intent = new Intent(context, SignUpCreationActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    private String getEmail() {
        return getIntent().getStringExtra(KEY_EMAIL);
    }

    private String getPassword() {
        return getIntent().getStringExtra(KEY_PASSWORD);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_creation);
        synchronized (LOCK) {
            if (null == sTask) {
                sTask = new CreationTask(this, getEmail(), getPassword());
                sTask.start();
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SyncResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        synchronized (LOCK) {
            sTask = null;
            if (event.hasError()) {
                new ErrorDialog(this).show(event.getError(), (dialog, which) -> {
                    finish(); // return to previous screen
                });
            }
            Intent intent = new Intent(this, SignUpSuccessfulActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears Sign Up screens
            startActivity(intent);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CreationResultEvent event) {
        synchronized (LOCK) {
            sTask = null;
            EventBus.getDefault().removeStickyEvent(event);
            if (event.isSuccessful()) {
                sTask = new SyncTask(this);
                sTask.start();
                return;
            }
            Throwable error = event.getError();
            if (error instanceof AccountAlreadyExistsException) {
                showAccountAlreadyExists();
                return;
            }
            new ErrorDialog(this).show(error.getMessage(), (dialog, which) -> {
                finish(); // return to previous screen
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void showAccountAlreadyExists() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.CreateAccountAccountExist)
                .setMessage(R.string.CreateAccountAccounExistsMessage)
                .setPositiveButton(R.string.CreateAccountUseExisting, (dialog, which) -> {
                    startSignInActivity();
                })
                .setNegativeButton(R.string.CreateAccountCreateNew, (dialog, which) -> {
                    startSignUpEmailActivity();
                })
                .show();
    }

    private void startSignInActivity() {
        SignInActivity.start(this, getEmail());
    }

    private void startSignUpEmailActivity() {
        Intent intent = new Intent(this, SignUpEmailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private static class CreationResultEvent extends BaseEvent {
        public CreationResultEvent(Throwable throwable) {
            super(throwable);
        }

        public CreationResultEvent() {
        }
    }

    private static class CreationTask extends Thread {
        private final Context mContext;
        private final String mEmail;
        private final String mPassword;
        private ServerAPI mApi = new ServerAPI();
        private String mLanguage;
        private String mPrivateKey;
        private String mPublicKey;

        private CreationTask(Context context, String email, String password) {
            mContext = context;
            mEmail = email;
            mPassword = password;
        }

        private void authenticateAccount() throws Exception {
            KeyPair key = CryptoHelper.createKeyPair();
            mPublicKey = CryptoPemHelper.getPEMPublicKey(key);
            mPrivateKey = CryptoPemHelper.getPEMPrivateKey(key);
            Pref.setValue(mContext, Constants.PUBLIC_KEY, mPublicKey);
            Pref.setValue(mContext, Constants.PRIVATE_KEY, mPrivateKey);
            LanguagesUtils changeLanguage = new LanguagesUtils(mContext);
            String shortLanguage = mContext.getResources().getConfiguration().locale.getLanguage();
            mLanguage = changeLanguage.getLanguageByLetterCode(shortLanguage);
            AuthenticationHttpBean auth = new AuthenticationHttpBean();
            auth.setPublicKey(Base64.encodeToString(CryptoPemHelper.getPEMPublicKey(key).getBytes(), Base64.DEFAULT));
            auth.setEmail(mEmail);
            auth.setLanguage(mLanguage);
            auth.setInstallation(Pref.INSTALLATION_UUID);
            ServerResponseHttpBean response = mApi.accountAuthentication(auth);
            ErrorHttpBean error = response.getError();
            if (null == error) return;
            if (error.getCode() == 400 && error.getId() == 600) {
                throw new AccountAlreadyExistsException();
            }
            throw new UnexpectedServerError(mContext, error);
        }

        private void createDatabase() throws SQLException {
            Pref.DATABASE_KEY = Pref.generatePassword(mEmail, mPassword);
            DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(mContext);
            DatabaseHelperSecure databaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            SettingsBll settingsBll = new SettingsBll(databaseHelperSecure);
            UserInfoBll userInfoBll = new UserInfoBll(databaseHelperSecure);
            ConfigurationBll configurationBll = new ConfigurationBll(databaseHelperNonSecure);
            UserInfo mUserInfo = new UserInfo(mEmail,
                    DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString(),
                    DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
            mUserInfo.setPublicKey(mPublicKey.getBytes());
            mUserInfo.setPrivateKey(mPrivateKey.getBytes());
            mUserInfo.calculateHash();
            userInfoBll.insertOrUpdateRow(mUserInfo);
            settingsBll.insertOrUpdate(DatabaseConstants.ADVANCED_AUTO_LOGIN, "1");
            settingsBll.insertOrUpdate(DatabaseConstants.ADVANCED_AUTO_STORE_DATA, "1");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.AUTOLOCK, "3");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.REMEMBER_EMAIL, "1");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.PUSH_NOTIFICATIONS, "0");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION, "0");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.LANGUAGE, mLanguage);
        }

        private void registerDevice() throws ServerException, SQLException {
            Installation installation = new Installation();
            if (!installation.isRegistered()) {
                if (!installation.register(mContext))
                    throw new ServerException("Installation was not registered");
            }
            DevicePostHttpBean deviceEndpoint = new DevicePostHttpBean();
            deviceEndpoint.setInstallation(installation.getUuid());
            deviceEndpoint.setNickname(Build.MODEL);
            deviceEndpoint.setSoftwareVersion(BuildConfig.VERSION_NAME);
            ServerResponseHttpBean serverResponseHttpBean = mApi.deviceEndpointPost(deviceEndpoint, mEmail);
            if (null == serverResponseHttpBean) throw new NullResponseError(mContext);
            ErrorHttpBean error = serverResponseHttpBean.getError();
            if (null != error) throw new UnexpectedServerError(mContext, error);
            DeviceHttpBean deviceHttp = null;
            for (int i = 0, max = serverResponseHttpBean.getDevices().length; i < max; i++) {
                if (null == deviceHttp
                        || serverResponseHttpBean.getDevices()[i].getLastModifiedDate().getMillis() > deviceHttp.getLastModifiedDate().getMillis()) {
                    deviceHttp = serverResponseHttpBean.getDevices()[i];
                }
            }
            if (null == deviceHttp) return;
            Device device = new Device();
            device.setActive(true);
            device.setCreatedDate(deviceHttp.getCreatedDate().toDateTimeISO().toString());
            device.setLastModifiedDate(deviceHttp.getLastModifiedDate().toDateTimeISO().toString());
            device.setInstallationId(deviceHttp.getInstallation().getUuid());
            device.setUuid(deviceHttp.getUuid());
            device.setNickname(deviceHttp.getNickname());
            device.setOs(deviceHttp.getInstallation().getOs().getName());
            device.setDeviceCategory(Constants.MOBILE);
            Pref.DEVICE_UUID = device.getUuid();
            DeviceBll deviceBll = new DeviceBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            deviceBll.insertRow(device);
        }

        public void run() {
            try {
                runImpl();
                EventBus.getDefault().postSticky(new CreationResultEvent());
            } catch (Exception e) {
                EventBus.getDefault().postSticky(new CreationResultEvent(e));
            }
        }

        private void runImpl() throws Exception {
            authenticateAccount();
            Pref.EMAIL = mEmail; // to apply on database path
            Pref.setValue(mContext, Constants.EMAIL, mEmail);
            createDatabase();
            registerDevice();
        }

    }


    private static class AccountAlreadyExistsException extends Exception {
    }
}