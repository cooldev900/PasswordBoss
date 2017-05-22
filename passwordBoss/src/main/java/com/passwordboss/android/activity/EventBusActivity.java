package com.passwordboss.android.activity;

import com.passwordboss.android.event.UpNavigationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

abstract class EventBusActivity extends BaseActivity {
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpNavigationEvent event) {
        onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
