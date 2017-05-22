package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.dialog.alert.InfoDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.helper.Regex;
import com.passwordboss.android.helper.SafeIntent;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.ServerException;
import com.passwordboss.android.http.UnexpectedServerError;
import com.passwordboss.android.http.beans.ServerResponseHttpBeanTwoStep;
import com.passwordboss.android.task.NetworkTask;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInTwoStepVerificationActivity extends EventBusActivity {
    private static final String URL_LOST_PHONE = "https://support.passwordboss.com/customer/portal/articles/1839106-i-forgot-my-master-password-what-do-i-do-?b_id=6281&utm_source=PC&utm_medium=Login&utm_campaign=FMP";
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInTwoStepVerificationActivity.class);
    @Bind(R.id.ac_sitsv_code)
    EditText mCodeView;
    @Bind(R.id.ac_sitsv_trusted_device)
    CheckBox mTrustedDeviceView;

    @OnClick(R.id.ac_sitvs_button_verify)
    void onClickButtonVerify() {
        AwesomeValidation validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(mCodeView, Regex.NOT_EMPTY, getString(R.string.EnterCode));
        if (!validation.validate()) return;
        new ProgressDialog(this).showLoading();
        new VerifyCodeTask(this, mCodeView.getText().toString()).start();
    }

    @OnClick(R.id.ac_sitsv_lost_phone)
    void onClickLostPhone() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_LOST_PHONE));
        if (new SafeIntent(this, intent).check()) startActivity(intent);
    }

    @OnClick(R.id.ac_sitsv_trusted_device_info)
    void onClickTrustedDeviceInfo() {
        new InfoDialog(this).show(getText(R.string.AccountSettingTrustedDeviceTooltipText));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_two_step_verification);
        ButterKnife.bind(this);
        setTitle(R.string.TwoStepVerificationTitle);
        displayHomeAsUp();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventTaskResult(TaskResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        new ProgressDialog(this).dismiss();
        if (event.hasError()) {
            new ErrorDialog(this).show(event.getError());
            return;
        }
        updateTrustedDeviceIfNeeded();
        MainActivity.start(this);
    }

    private void updateTrustedDeviceIfNeeded() {
        if (!mTrustedDeviceView.isChecked()) return;
        try {
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(this));
            configurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ADVANCED_TRUSTED_DEVICE, "1");
            configurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ADVANCED_TRUSTED_DEVICE_DATE, DateTime.now().toString("MM/dd/yyyy HH:mm:ss"));
        } catch (SQLException e) {
            LOGGER.error("updateTrustedDeviceIfNeeded", e);
        }
    }

    private static class TaskResultEvent extends BaseEvent {
        public TaskResultEvent(Throwable throwable) {
            super(throwable);
        }

        public TaskResultEvent() {
        }
    }

    private static class VerifyCodeTask extends NetworkTask<TaskResultEvent> {
        private final String mCode;

        private VerifyCodeTask(Context context, String code) {
            super(context);
            mCode = code;
        }

        @Override
        protected TaskResultEvent createEventError(Exception e) {
            return new TaskResultEvent(e);
        }

        @Override
        protected TaskResultEvent createEventSuccess() {
            return new TaskResultEvent();
        }

        @Override
        protected void runImpl() throws Exception {
            ServerResponseHttpBeanTwoStep response = new ServerAPI().twoStepVerificationPost(mCode);
            if (null != response.getError()) {
                throw new UnexpectedServerError(getContext(), response.getError());
            }
            if (response.isAuth()) return;
            throw new ServerException(getContext().getString(R.string.PleaseEnterCorrect6DigitCode));
        }
    }

}
