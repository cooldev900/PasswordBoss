package com.passwordboss.android.activity;

import android.os.Bundle;

import com.passwordboss.android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpSuccessfulActivity extends BaseActivity {
    @OnClick(R.id.ac_sups_button_continue)
    void onClickButtonContinue() {
        SignUpPinActivity.start(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_successful);
        ButterKnife.bind(this);
    }
}
