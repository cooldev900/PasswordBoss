package com.passwordboss.android.activity;

import android.support.annotation.NonNull;

import com.passwordboss.android.fragment.BaseFragment;
import com.passwordboss.android.fragment.PersonaInfoFragment;

public class PersonalInfoActivity extends BaseSecureItemsActivity {
    @NonNull
    @Override
    protected BaseFragment createFragment() {
        return new PersonaInfoFragment();
    }
}
