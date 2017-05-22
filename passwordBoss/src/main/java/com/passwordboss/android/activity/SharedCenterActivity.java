package com.passwordboss.android.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.fragment.SharedCenterFragment;

public class SharedCenterActivity extends ToolbarWrappedFragmentActivity {

    @Nullable
    @Override
    protected Fragment createFragment() {
        return new SharedCenterFragment();
    }


}
