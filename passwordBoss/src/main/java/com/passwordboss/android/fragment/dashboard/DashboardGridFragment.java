package com.passwordboss.android.fragment.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.logback.AppSqlError;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.utils.Pref;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardGridFragment extends DashboardFragment {
    @Bind(R.id.ac_mn_toolbar_menu)
    ActionMenuView mMenuView;
    @Bind(R.id.ac_mn_toolbar_synced_devices)
    TextView mSyncedDevicesView;
    @Bind(R.id.ac_mn_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ac_mn_toolbar_title)
    TextView mToolbarTitleView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dashboard_grid;
    }

    @Override
    public void onResume() {
        super.onResume();
        DeviceBll.getObservable()
                .map(deviceBll -> {
                    try {
                        return deviceBll.getActiveDevices().size();
                    } catch (SQLException e) {
                        new AppSqlError(e).log(getClass());
                        return -1;
                    }
                })
                .compose(new ApplySchedulers<>())
                .subscribe(count -> {
                    String text = getText(R.string.SyncedDevices) + " " + Integer.toString(count);
                    mSyncedDevicesView.setText(text);
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mToolbarTitleView.setText(Pref.EMAIL);
        new SupportMenuInflater(getContext()).inflate(R.menu.fragment_dashboard, mMenuView.getMenu());
        mMenuView.setOnMenuItemClickListener(this::onOptionsItemSelected);
    }
}
