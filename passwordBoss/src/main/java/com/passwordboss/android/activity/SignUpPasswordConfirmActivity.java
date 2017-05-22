package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.constants.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpPasswordConfirmActivity extends BaseActivity {

    private static final String KEY_PASSWORD = "keyPassword";
    private static final String KEY_EMAIL = "keyEmail";

    @Bind(R.id.ac_supc_password_confirm)
    EditText mPasswordConfirmView;
    @Bind(R.id.ac_supc_password_match)
    View mPasswordMatchView;

    public static void start(Context context, String email, String password) {
        Intent intent = new Intent(context, SignUpPasswordConfirmActivity.class);
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

    private boolean isValid() {
        String passwordConfirm = mPasswordConfirmView.getText().toString();
        return passwordConfirm.equals(getPassword());
    }

    @OnClick(R.id.ac_supc_button_sign_up)
    void onClickButtonSignUp() {
        if (!isValid()) {
            mPasswordConfirmView.setError(getString(R.string.PasswordNotMatched));
            return;
        }
        SignUpCreationActivity.start(this, getEmail(), getPassword());
    }

    @OnClick(R.id.ac_supc_policy)
    void onClickPolicy() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY_LINK)));
    }

    @OnClick(R.id.ac_supc_terms)
    void onClickTerms() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TERMS_OF_SERVICES_LINK)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password_confirm);
        ButterKnife.bind(this);
        mPasswordConfirmView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPasswordMatchView.setVisibility(s.toString().equals(getPassword()) ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }
}
