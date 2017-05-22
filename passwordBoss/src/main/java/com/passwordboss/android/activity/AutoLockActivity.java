package com.passwordboss.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.passwordboss.android.BuildConfig;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.event.AutoLockTimeUpdatedEvent;
import com.passwordboss.android.logback.AppSqlError;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

abstract public class AutoLockActivity extends EventBusActivity {

    private static final String KEY_NEXT_AUTO_LOCK_TIME = "keyNextAutoLockTime";
    private static Handler sHandler = new Handler();
    private static long sNextAutoLockTime = 0;
    private static long sAutoLockTime = -1;

    protected void lockApp() {
        sNextAutoLockTime = 0;
        startActivity(new Intent(this, UnlockActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (null != savedInstanceState) {
            sNextAutoLockTime = savedInstanceState.getLong(KEY_NEXT_AUTO_LOCK_TIME, 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AutoLockTimeUpdatedEvent event) {
        updateAutoLockTimeValue();
        startAutoLockTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sHandler.removeCallbacksAndMessages(null); // sNextAutoLockTime will be checked at onResume
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sNextAutoLockTime != 0 && sNextAutoLockTime <= System.currentTimeMillis()) {
            lockApp();
        } else {
            startAutoLockTimer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_NEXT_AUTO_LOCK_TIME, sNextAutoLockTime);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (sNextAutoLockTime == 0) return; // already locked, skip starting new task
        startAutoLockTimer();
    }

    private void startAutoLockTimer() {
        if (sAutoLockTime == -1) updateAutoLockTimeValue();
        sHandler.removeCallbacksAndMessages(null);
        if (BuildConfig.DEBUG) return;
        sHandler.postDelayed(this::lockApp, sAutoLockTime);
        sNextAutoLockTime = System.currentTimeMillis() + sAutoLockTime;
    }

    private void updateAutoLockTimeValue() {
        try {
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(this));
            sAutoLockTime = configurationBll.getAutoLockTime(Pref.EMAIL);
        } catch (SQLException e) {
            sAutoLockTime = ConfigurationBll.DEFAULT_AUTO_LOCK_TIME;
            new AppSqlError(e).log(getClass());
        }
    }

}
