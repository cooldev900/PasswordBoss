package com.passwordboss.android.activity;

import android.support.annotation.NonNull;

import com.passwordboss.android.fragment.BaseFragment;
import com.passwordboss.android.fragment.DigitalWalletFragment;

public class DigitalWalletActivity extends BaseSecureItemsActivity {
    @NonNull
    @Override
    protected BaseFragment createFragment() {
        return new DigitalWalletFragment();
    }

}
