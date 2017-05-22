package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.PasswordScanner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpPasswordActivity extends BaseActivity {

    private static final String KEY_EMAIL = "keyEmail";
    @Bind(R.id.ac_sup_capitals)
    CheckBox mCapitalsView;
    @Bind(R.id.ac_sup_letters)
    CheckBox mLettersView;
    @Bind(R.id.ac_sup_min_length)
    CheckBox mMinLengthView;
    @Bind(R.id.ac_sup_numbers)
    CheckBox mNumbersView;
    @Bind(R.id.ac_sup_password)
    EditText mPasswordView;
    @Bind(R.id.ac_sup_symbols)
    CheckBox mSymbolsView;

    public static void start(Context context, String email) {
        Intent intent = new Intent(context, SignUpPasswordActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        context.startActivity(intent);
    }

    private String getEmail() {
        return getIntent().getStringExtra(KEY_EMAIL);
    }

    private void initializePasswordView() {
        mPasswordView.addTextChangedListener(new TextWatcher() {
            private final PasswordScanner mPasswordScanner = new PasswordScanner();

            @Override
            public void afterTextChanged(Editable s) {
                mPasswordScanner.scanPassword(s.toString());
                mLettersView.setChecked(mPasswordScanner.hasLetters());
                mCapitalsView.setChecked(mPasswordScanner.hasCapitals());
                mMinLengthView.setChecked(mPasswordScanner.hasMinLength());
                mNumbersView.setChecked(mPasswordScanner.hasDigits());
                mSymbolsView.setChecked(mPasswordScanner.hasSymbols());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private boolean isValid() {
        return mLettersView.isChecked()
                && mCapitalsView.isChecked()
                && mMinLengthView.isChecked()
                && mNumbersView.isChecked()
                && mSymbolsView.isChecked();
    }

    @OnClick(R.id.ac_sup_button_next)
    void onClickButtonNext() {
        if (!isValid()) return;
        SignUpPasswordConfirmActivity.start(this, getEmail(), mPasswordView.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);
        ButterKnife.bind(this);
        initializePasswordView();
    }
}
