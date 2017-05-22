package com.passwordboss.android.fragment.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.UniqueViewId;
import com.passwordboss.android.toolbar.ToolbarOwner;

public class DashboardStripFragment extends DashboardFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dashboard_strip;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        if (!(getActivity() instanceof ToolbarOwner)) return;
        Toolbar toolbar = ((ToolbarOwner) getActivity()).getToolbar(new UniqueViewId().get(view));
        if (null == toolbar) return;
        toolbar.inflateMenu(R.menu.fragment_dashboard);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
    }
}
