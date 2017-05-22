package com.passwordboss.android.fragment;

import org.greenrobot.eventbus.EventBus;

abstract class EventBusFragment extends BaseFragment {
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
