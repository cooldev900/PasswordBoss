package com.passwordboss.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.passwordboss.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpEmailActivity extends BaseActivity {
    @Bind(R.id.ac_sue_email)
    EditText mEmailView;
    @Bind(R.id.ac_sue_logo)
    View mLogoView;
    private AwesomeValidation mAwesomeValidation;
    private KeyboardWatcher mKeyboardWatcher;

    private void initializeKeyboardWatcher() {
        mKeyboardWatcher = KeyboardWatcher.initWith(this).bindKeyboardWatcher(new KeyboardWatcher.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardClosed() {
                mLogoView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onKeyboardShown(int keyboardSize) {
                mLogoView.setVisibility(View.GONE);
            }
        });
    }

    private void initializeValidation() {
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(mEmailView, Patterns.EMAIL_ADDRESS, getString(R.string.ValidEmailAddress));
    }

    @OnClick(R.id.ac_sue_button_next)
    void onClickButtonNext() {
        if (!mAwesomeValidation.validate()) return;
        SignUpPasswordActivity.start(this, mEmailView.getText().toString());
    }

    @OnClick(R.id.ac_sue_sign_in)
    void onClickSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);
        ButterKnife.bind(this);
        initializeKeyboardWatcher();
        initializeValidation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mKeyboardWatcher.unbindKeyboardWatcher();
    }

}
