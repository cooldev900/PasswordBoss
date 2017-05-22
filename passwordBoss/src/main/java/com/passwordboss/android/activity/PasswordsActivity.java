package com.passwordboss.android.activity;

import android.support.annotation.NonNull;

import com.passwordboss.android.fragment.BaseFragment;
import com.passwordboss.android.fragment.PasswordsFragment;

public class PasswordsActivity extends BaseSecureItemsActivity {
    @Override
    @NonNull
    protected BaseFragment createFragment() {
        return new PasswordsFragment();
    }
}
