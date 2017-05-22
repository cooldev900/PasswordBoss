package com.passwordboss.android.fragment;

import android.support.v4.app.Fragment;

import com.passwordboss.android.R;

public abstract class BaseFragment extends Fragment {
    protected boolean isPhone() {
        return !isTablet();
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}
