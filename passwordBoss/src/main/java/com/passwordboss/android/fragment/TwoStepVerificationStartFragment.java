package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationStartFragment extends BaseFragment {
    @OnClick(R.id.fr_tsvs_button_get_started)
    void onClickButtonGetStarted() {
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.MasterPassword));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_start, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
