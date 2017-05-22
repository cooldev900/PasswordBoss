package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.passwordboss.android.BuildConfig;
import com.passwordboss.android.R;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.helper.Regex;
import com.passwordboss.android.http.NullResponseError;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.ServerException;
import com.passwordboss.android.http.beans.DeviceHttpBean;
import com.passwordboss.android.http.beans.DevicePostHttpBean;
import com.passwordboss.android.http.beans.ErrorHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends EventBusActivity {
    @Bind(R.id.ac_vr_code)
    EditText mCodeView;
    private AwesomeValidation mAwesomeValidation;

    private void initializeValidation() {
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(mCodeView,  Regex.NOT_EMPTY, getString(R.string.EnterCode));
    }

    @OnClick(R.id.ac_vr_button_next)
    void onClickButtonNext() {
        if (!mAwesomeValidation.validate()) return;
        new ProgressDialog(this).showLoading();
        new VerificationTask(this, mCodeView.getText().toString()).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        initializeValidation();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventNewVerificationCodeResultEvent(NewVerificationCodeResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        try {
            if (event.hasError()) {
                new ErrorDialog(this).show(event.getError());
            }
        } finally {
            new ProgressDialog(this).dismiss();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventVerificationResult(VerificationResultEvent event) {
        try {
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                if (event.getError() instanceof VerificationFailed) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.IncorrectVerificationCode)
                            .setMessage(R.string.IncorrectVerificationCodeDescription)
                            .setPositiveButton(R.string.TryAgain, null)
                            .setNegativeButton(R.string.Resend, (dialog, which) -> {
                                new ProgressDialog(VerificationActivity.this).showLoading();
                                new NewVerificationCodeTask().start();
                            }).show();
                    return;
                }
                new ErrorDialog(this).show(event.getError());
                return;
            }
            startActivity(new Intent(this, UpdateActivity.class));
        } finally {
            new ProgressDialog(this).dismiss();
        }

    }

    private static class VerificationResultEvent extends BaseEvent {
        public VerificationResultEvent(Throwable throwable) {
            super(throwable);
        }

        public VerificationResultEvent() {
        }
    }

    private static class VerificationTask extends Thread {
        private final Context mContext;
        private final String mEmail = Pref.EMAIL;
        private final String mVerificationCode;

        private VerificationTask(Context context, String verificationCode) {
            mContext = context;
            mVerificationCode = verificationCode;
        }

        private void createDatabase(@Nullable Device device) throws Exception {
            DatabaseHelperSecure databaseHelperSecure;
            try {
                databaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            } catch (Exception e) {
                throw new Exception(mContext.getString(R.string.EmailNotMatchPassword));
            }
            DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(mContext);
            SettingsBll settingsBll = new SettingsBll(databaseHelperSecure);
            ConfigurationBll configurationBll = new ConfigurationBll(databaseHelperNonSecure);
            settingsBll.insertOrUpdate(DatabaseConstants.ADVANCED_AUTO_LOGIN, "1");
            settingsBll.insertOrUpdate(DatabaseConstants.ADVANCED_AUTO_STORE_DATA, "1");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.AUTOLOCK, "3");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.REMEMBER_EMAIL, "1");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.PUSH_NOTIFICATIONS, "0");
            configurationBll.updateOrInsertItem(mEmail, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION, "0");
            new DeviceBll(databaseHelperSecure).insertRow(device);
        }

        @Nullable
        private Device registerDevice() throws ServerException, SQLException {
            DevicePostHttpBean deviceEndpoint = new DevicePostHttpBean();
            deviceEndpoint.setInstallation(Pref.INSTALLATION_UUID);
            deviceEndpoint.setNickname(Build.MODEL);
            deviceEndpoint.setSoftwareVersion(BuildConfig.VERSION_NAME);
            deviceEndpoint.setVerification(mVerificationCode);
            ServerResponseHttpBean serverResponseHttpBean = new ServerAPI().deviceEndpointPost(deviceEndpoint, mEmail);
            if (null == serverResponseHttpBean) throw new NullResponseError(mContext);
            ErrorHttpBean error = serverResponseHttpBean.getError();
            if (null != error) throw new VerificationFailed();
            DeviceHttpBean deviceHttp = null;
            for (int i = 0, max = serverResponseHttpBean.getDevices().length; i < max; i++) {
                if (null == deviceHttp
                        || serverResponseHttpBean.getDevices()[i].getLastModifiedDate().getMillis() > deviceHttp.getLastModifiedDate().getMillis()) {
                    deviceHttp = serverResponseHttpBean.getDevices()[i];
                }
            }
            if (null == deviceHttp) return null;
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
            return device;
        }

        @Override
        public void run() {
            try {
                Device device = registerDevice();
                createDatabase(device);
                EventBus.getDefault().postSticky(new VerificationResultEvent());
            } catch (Exception e) {
                EventBus.getDefault().postSticky(new VerificationResultEvent(e));
            }
        }
    }

    private static class NewVerificationCodeResultEvent extends BaseEvent {

        public NewVerificationCodeResultEvent(Exception e) {
            super(e);
        }

        public NewVerificationCodeResultEvent() {
        }
    }

    private static class NewVerificationCodeTask extends Thread {
        @Override
        public void run() {
            try {
                new ServerAPI().verificationEmail(Pref.EMAIL);
                EventBus.getDefault().postSticky(new NewVerificationCodeResultEvent());
            } catch (Exception e) {
                EventBus.getDefault().postSticky(new NewVerificationCodeResultEvent(e));
            }
        }
    }

    private static class VerificationFailed extends ServerException {
    }
}
