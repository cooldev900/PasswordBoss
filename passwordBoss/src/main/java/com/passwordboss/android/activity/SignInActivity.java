package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.passwordboss.android.R;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.helper.NetworkState;
import com.passwordboss.android.helper.Regex;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.beans.DevicePostHttpBean;
import com.passwordboss.android.http.beans.ErrorHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInActivity.class);
    private static final String KEY_EMAIL = "keyEmail";
    AwesomeValidation mAwesomeValidation;
    @Bind(R.id.ac_si_email)
    EditText mEmailView;
    @Bind(R.id.ac_si_logo)
    View mLogoView;
    @Bind(R.id.ac_si_password)
    EditText mPasswordView;
    private KeyboardWatcher mKeyboardWatcher;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, String email) {
        if (DatabaseHelperSecure.instanceExists()) {
            // FIXME: 6/3/2016 Dagger 2?
            DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(context, Pref.DATABASE_KEY);
            if (null != helperSecure) helperSecure.close();
        }
        Intent intent = new Intent(context, SignInActivity.class);
        if (null != email) intent.putExtra(KEY_EMAIL, email);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Nullable
    private String getEmail() {
        String email = getIntent().getStringExtra(KEY_EMAIL);
        if (!TextUtils.isEmpty(email)) {
            return email; // activity was started with predefined email (sign up use case)
        }
        email = Pref.getValue(getApplicationContext(), Constants.EMAIL, null);
        DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(this);
        ConfigurationBll configurationBll = null;
        try {
            configurationBll = new ConfigurationBll(databaseHelperNonSecure);
        } catch (SQLException e) {
            LOGGER.error("SQL: create configuration", e);
        }
        if (TextUtils.isEmpty(email) || null == configurationBll) return null;
        Configuration configuration = configurationBll.getConfigurationByEmailAndKey(email, DatabaseConstants.REMEMBER_EMAIL);
        return null != configuration && "1".equals(configuration.getValue()) ? email : null;
    }

    private void initializeKeyboardWatcher() {
        mKeyboardWatcher = KeyboardWatcher.initWith(this).bindKeyboardWatcher(new KeyboardWatcher.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardClosed() {
                if (isPhone()) mLogoView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onKeyboardShown(int keyboardSize) {
                if (isPhone()) mLogoView.setVisibility(View.GONE);
            }
        });
    }

    private void initializeValidation() {
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(mEmailView, Patterns.EMAIL_ADDRESS, getString(R.string.ValidEmailAddress));
        mAwesomeValidation.addValidation(mPasswordView, Regex.NOT_EMPTY, getString(R.string.EnterMasterPassword));
    }

    private boolean isValid() {
        return mAwesomeValidation.validate();
    }

    @OnClick(R.id.ac_si_button_sign_in)
    void onClickButtonSignIn() {
        if (!isValid()) return;
        new ProgressDialog(this).showLoading();
        String email = mEmailView.getText().toString().toLowerCase().trim();
        String password = mPasswordView.getText().toString();
        new SignInTask(this, email, password).start();
    }

    @OnClick(R.id.ac_si_sign_up)
    void onClickSignUp() {
        startActivity(new Intent(this, GetStartedActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        Pref.DATABASE_KEY = null;
        preFillEmailViewIfNeeded();
        initializeKeyboardWatcher();
        initializeValidation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mKeyboardWatcher.unbindKeyboardWatcher();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventSignInResult(SignInResultEvent event) {
        try {
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                new ErrorDialog(this).show(event.getError());
                return;
            }
            switch (event.NextStep) {
                case Main:
                    MainActivity.start(this);
                    break;
                case TwoStepSignIn:
                    startActivity(new Intent(this, SignInTwoStepVerificationActivity.class));
                    break;
                case Verification:
                    startActivity(new Intent(this, VerificationActivity.class));
                    break;
            }
        } finally {
            new ProgressDialog(this).dismiss();
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

    private void preFillEmailViewIfNeeded() {
        String email = getEmail();
        if (TextUtils.isEmpty(email)) return;
        mEmailView.setText(email);
        mEmailView.setSelection(email.length());
        mPasswordView.requestFocus();
    }

    private enum NextStep {
        Main, TwoStepSignIn, Verification
    }

    private static class SignInResultEvent extends BaseEvent {
        @NonNull
        public final NextStep NextStep;

        public SignInResultEvent(Throwable throwable) {
            super(throwable);
            //noinspection ConstantConditions
            NextStep = null;
        }

        public SignInResultEvent(@NonNull NextStep nextStep) {
            NextStep = nextStep;
        }
    }

    private static class SignInTask extends Thread {
        private final Context mContext;
        private final String mEmail;
        private final String mPassword;
        private final ServerAPI mApi = new ServerAPI();
        private NextStep mNextStep = NextStep.TwoStepSignIn;

        private SignInTask(Context context, String email, String password) {
            mContext = context;
            mEmail = email;
            mPassword = password;
        }

        private boolean isDatabaseExists() {
            String databasePath = Constants.APP_DATABASE
                    + File.separator + Pref.EMAIL + File.separator
                    + DatabaseConstants.SECURED_DB_NAME;
            return new File(databasePath).exists();
        }

        private void replaceNoEmailForLanguageKey() {
            try {
                new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext)).replaceNoEmailForLanguageKey(Pref.EMAIL);
            } catch (SQLException e) {
                LOGGER.error("SQL: error during replace NO_EMAIL", e);
            }
        }

        @Override
        public void run() {
            try {
                runImpl();
                EventBus.getDefault().postSticky(new SignInResultEvent(mNextStep));
            } catch (Exception e) {
                EventBus.getDefault().postSticky(new SignInResultEvent(e));
            }

        }

        private void runImpl() throws Exception {
            Pref.EMAIL = mEmail;
            Pref.DATABASE_KEY = Pref.generatePassword(mEmail, mPassword);
            replaceNoEmailForLanguageKey();
            if (isDatabaseExists()) {
                signInViaDatabase();
            } else {
                signInViaDeviceApi();
            }
            if (mNextStep == NextStep.Verification) {
                mApi.verificationEmail(mEmail);
            }
            Pref.setValue(mContext, Constants.EMAIL, Pref.EMAIL);
        }

        private void signInViaDatabase() throws Exception {
            DatabaseHelperSecure databaseHelperSecure;
            try {
                databaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            } catch (Exception e) {
                throw new Exception(mContext.getString(R.string.EmailNotMatchPassword));
            }
            DeviceBll deviceBll = new DeviceBll(databaseHelperSecure);
            Device device = deviceBll.getDeviceByInstallationUuid(Pref.INSTALLATION_UUID);
            if (null == device) {
                mNextStep = NextStep.Verification;
                return;
            }
            Pref.DEVICE_UUID = device.getUuid();
            DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(mContext);
            String email = Pref.EMAIL;
            ConfigurationBll configurationBll = new ConfigurationBll(databaseHelperNonSecure);
            Configuration twoStepVerificationConfig = configurationBll.getConfigurationByEmailAndKey(email, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION);
            Configuration trustedDeviceDateConfig = configurationBll.getConfigurationByEmailAndKey(email, DatabaseConstants.ADVANCED_TRUSTED_DEVICE_DATE);
            Configuration trustedDeviceConfig = configurationBll.getConfigurationByEmailAndKey(email, DatabaseConstants.ADVANCED_TRUSTED_DEVICE);
            if (twoStepVerificationConfig == null || !twoStepVerificationConfig.valueEquals("1")) {
                mNextStep = NextStep.Main;
                return;
            }
            if (trustedDeviceConfig == null || !trustedDeviceConfig.valueEquals("1")) {
                mNextStep = NextStep.TwoStepSignIn;
                return;
            }
            if (trustedDeviceDateConfig == null || trustedDeviceDateConfig.getValue().length() == 0) {
                mNextStep = NextStep.TwoStepSignIn;
                return;
            }

            DateTime deviceDateConfig = DateTime
                    .parse(trustedDeviceDateConfig.getValue(), DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"))
                    .withPeriodAdded(Period.days(30), 1);
            if (deviceDateConfig.isBeforeNow()) {
                configurationBll.updateOrInsertItem(email, DatabaseConstants.ADVANCED_TRUSTED_DEVICE_DATE, DateTime.now().toString("MM/dd/yyyy HH:mm:ss"));
                mNextStep = NextStep.TwoStepSignIn;
            } else {
                mNextStep = NextStep.Main;
            }
        }

        private void signInViaDeviceApi() throws Exception {
            new NetworkState(mContext).throwNoConnectionExceptionIfNeeded();
            DevicePostHttpBean deviceEndpoint = new DevicePostHttpBean();
            deviceEndpoint.setInstallation(Pref.INSTALLATION_UUID);
            deviceEndpoint.setNickname(Build.MODEL);
            deviceEndpoint.setSoftwareVersion(Pref.getAppVersion(mContext));
            ServerResponseHttpBean serverResponseHttpBean = mApi.deviceEndpointPost(deviceEndpoint, Pref.EMAIL);
            int errorCode;
            ErrorHttpBean error = serverResponseHttpBean.getError();
            if (null == error) {
                throw new Exception(mContext.getString(R.string.ErrorUnexpectedError));
            }
            if (error.getId() == 620) {
                throw new Exception(mContext.getString(R.string.ErrorID620));
            }
            errorCode = error.getCode();
            switch (errorCode) {
                case 403:
                    mNextStep = NextStep.Verification;
                    return;
                case 412:
                    throw new Exception(mContext.getString(R.string.AccountDoesNotExists));
                default:
                    throw new Exception(mContext.getString(R.string.ErrorUnexpectedError));
            }
        }
    }
}
