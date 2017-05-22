package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.helper.SoftKeyboard;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.widget.AppPasswordView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationMasterPasswordFragment extends BaseFragment {
    @Bind(R.id.fr_tsvmp_password)
    AppPasswordView mPasswordView;

    @OnClick(R.id.fr_tsvmp_button_next)
    void onClickButtonNext() {
        if (!Pref.DATABASE_KEY.equals(Pref.generatePassword(Pref.EMAIL, mPasswordView.getText().toString()))) {
            new ErrorDialog(getContext()).show(getText(R.string.PleaseEnterCorrectPasswordTSV), null);
            return;
        }
        new SoftKeyboard().hide(this);
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.AuthenticationApp));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_master_password, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new SoftKeyboard().showImplicit(mPasswordView);
    }
}
