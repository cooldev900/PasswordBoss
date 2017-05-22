package com.passwordboss.android.activity;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.passwordboss.android.R;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;

import butterknife.Bind;

public abstract class ToolbarWrappedFragmentActivity extends WrappedFragmentActivity implements ToolbarOwner {
    @Bind(R.id.in_tb_toolbar)
    AppToolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_toolbar;
    }

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int id) {
        return mToolbar;
    }

}
