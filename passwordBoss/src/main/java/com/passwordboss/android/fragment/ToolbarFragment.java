package com.passwordboss.android.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.passwordboss.android.helper.UniqueViewId;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarInvalidateEvent;
import com.passwordboss.android.toolbar.ToolbarOwner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

abstract class ToolbarFragment extends EventBusFragment {
    private AppToolbar mToolbar;


    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        toolbar.reset();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ToolbarInvalidateEvent event) {
        if (null != mToolbar) invalidateToolbar(mToolbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
    }

    protected void setToolbarMenuVisibility(@IdRes int idRes, boolean visible) {
        if (null == mToolbar) return;
        final Menu menu = mToolbar.getMenu();
        if (null == menu) return;
        MenuItem item = menu.findItem(idRes);
        if (null == item) return;
        item.setVisible(visible);
    }


    private void setupToolbar(View view) {
        if (getActivity() instanceof ToolbarOwner) {
            mToolbar = ((ToolbarOwner) getActivity()).getToolbar(new UniqueViewId().get(view));
            if (null == mToolbar) return;
            invalidateToolbar(mToolbar);
        }
    }
}
