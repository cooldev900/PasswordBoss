package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.passwordboss.android.R;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.helper.Regex;
import com.passwordboss.android.helper.SoftKeyboard;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.ServerException;
import com.passwordboss.android.http.UnexpectedServerError;
import com.passwordboss.android.http.beans.ServerResponseHttpBeanTwoStep;
import com.passwordboss.android.task.NetworkTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationSecurityCodeFragment extends EventBusFragment {

    private static final String KEY_DATA = "keyData";
    @Bind(R.id.fr_tsvsc_security_code)
    EditText mSecurityCodeView;

    public static TwoStepVerificationSecurityCodeFragment newInstance(TwoStepVerificationWizardNextStepEvent.Data data) {
        TwoStepVerificationSecurityCodeFragment fragment = new TwoStepVerificationSecurityCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    private TwoStepVerificationWizardNextStepEvent.Data getData() {
        Bundle arguments = getArguments();
        if (null == arguments) return null;
        return (TwoStepVerificationWizardNextStepEvent.Data) arguments.getSerializable(KEY_DATA);
    }

    @OnClick(R.id.fr_tsvsc_button_next)
    void onClickButtonNext() {
        AwesomeValidation validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(mSecurityCodeView, Regex.NOT_EMPTY, getString(R.string.EnterCode));
        if (!validation.validate()) return;
        new ProgressDialog(this).showLoading();
        new CheckSecurityCodeTask(getContext(), mSecurityCodeView.getText().toString()).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_security_code, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventTaskResult(TaskResultEvent event) {
        new ProgressDialog(this).dismiss();
        EventBus.getDefault().removeStickyEvent(event);
        if (event.hasError()) {
            new ErrorDialog(getContext()).show(event.getError());
            return;
        }
        new SoftKeyboard().hide(this);
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.BackupSecurityCode,
                getData()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new SoftKeyboard().showImplicit(mSecurityCodeView);
    }

    private static class TaskResultEvent extends BaseEvent {
        public TaskResultEvent() {
        }

        public TaskResultEvent(Throwable throwable) {
            super(throwable);
        }
    }

    private static class CheckSecurityCodeTask extends NetworkTask<TaskResultEvent> {
        private final String mSecurityCode;

        public CheckSecurityCodeTask(Context context, String securityCode) {
            super(context);
            mSecurityCode = securityCode;
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
            ServerResponseHttpBeanTwoStep response = new ServerAPI().twoStepVerificationPost(mSecurityCode);
            if (null != response.getError()) {
                throw new UnexpectedServerError(getContext(), response.getError());
            }
            if (response.isAuth()) return;
            throw new ServerException(getContext().getString(R.string.PleaseEnterCorrect6DigitCode));
        }
    }
}
