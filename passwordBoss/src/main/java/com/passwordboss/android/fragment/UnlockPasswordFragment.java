package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.passwordboss.android.R;
import com.passwordboss.android.activity.UnlockActivity;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class UnlockPasswordFragment extends BaseFragment {
    @Bind(R.id.fr_unps_email)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    @RegExp(value = RegExp.EMAIL, messageId = R.string.ValidEmailAddress)
    EditText mEmailView;
    @Bind(R.id.fr_unps_logo)
    View mLogoView;
    @Bind(R.id.fr_unps_password)
    @NotEmpty(messageId = R.string.RequiredField, order = 3)
    EditText mPasswordView;
    private KeyboardWatcher mKeyboardWatcher;

    private void initializeKeyboardWatcher() {
        mKeyboardWatcher = KeyboardWatcher.initWith(getActivity()).bindKeyboardWatcher(new KeyboardWatcher.OnKeyboardToggleListener() {
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

    @OnClick(R.id.fr_unps_button_unlock)
    void onClickButtonUnlock() {
        if (!FormValidator.validate(this, new SimpleErrorPopupCallback(getContext(), true))) {
            return;
        }
        String email = mEmailView.getText().toString().toLowerCase().trim();
        String password = mPasswordView.getText().toString();
        Pref.EMAIL = email;
        Pref.DATABASE_KEY = Pref.generatePassword(email, password);
        try {
            DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY);
            EventBus.getDefault().post(new UnlockActivity.UnlockedEvent());
        } catch (Exception e) {
            new ErrorDialog(getContext()).show(getText(R.string.EmailNotMatchPassword), null);
        }
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unlock_password, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mKeyboardWatcher.unbindKeyboardWatcher();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        String email = Pref.getValue(getContext(), Constants.EMAIL, null);
        mEmailView.setText(email);
        initializeKeyboardWatcher();
    }


}
