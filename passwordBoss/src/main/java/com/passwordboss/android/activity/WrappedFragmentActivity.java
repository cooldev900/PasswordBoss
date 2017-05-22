package com.passwordboss.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.fragment.BackPressConsumer;

import butterknife.ButterKnife;

abstract class WrappedFragmentActivity extends AutoLockActivity {

    @Nullable
    protected abstract Fragment createFragment();

    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    protected Fragment getWrappedFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.ac_fr_fragment);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getWrappedFragment();
        if (fragment instanceof BackPressConsumer) {
            if (((BackPressConsumer) fragment).onBackPressed()) return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (null == savedInstanceState) {
            Fragment fragment = createFragment();
            if (null != fragment) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.ac_fr_fragment, fragment)
                        .commit();
            }
        }
    }
}
