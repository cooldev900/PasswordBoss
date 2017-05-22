package com.passwordboss.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.event.AutoLockTimeSelectedEvent;
import com.passwordboss.android.event.CardTypeSelectedEvent;
import com.passwordboss.android.event.CountrySelectedEvent;
import com.passwordboss.android.event.LanguageSelectedEvent;
import com.passwordboss.android.event.StorageRegionSelectedEvent;
import com.passwordboss.android.fragment.SettingItemListFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SettingItemListActivity extends ToolbarWrappedFragmentActivity {

    public static final int TYPE_INVALID = -1;
    public static final int TYPE_LANGUAGE = 1;
    public static final int TYPE_COUNTRY = 2;
    public static final int TYPE_AUTO_LOCK_TIME = 3;
    public static final int TYPE_STORAGE_REGION = 4;
    public static final int TYPE_CREDIT_CARDS = 5;

    public static final String EXTRA_TYPE = "extra_type";

    @Nullable
    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        int type = extras.getInt(EXTRA_TYPE, TYPE_INVALID);
        return SettingItemListFragment.newInstance(type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(LanguageSelectedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(CountrySelectedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(AutoLockTimeSelectedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(StorageRegionSelectedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(CardTypeSelectedEvent event) {
        finish();
    }
}

