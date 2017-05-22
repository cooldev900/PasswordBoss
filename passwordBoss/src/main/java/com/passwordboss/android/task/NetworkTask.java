package com.passwordboss.android.task;

import android.content.Context;

import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.helper.NetworkState;

import org.greenrobot.eventbus.EventBus;

public abstract class NetworkTask<T extends BaseEvent> extends Thread {
    private final Context mContext;

    protected NetworkTask(Context context) {
        mContext = context;
    }

    protected abstract T createEventError(Exception e);

    protected abstract T createEventSuccess();

    protected Context getContext() {
        return mContext;
    }

    @Override
    final public void run() {
        try {
            new NetworkState(mContext).throwNoConnectionExceptionIfNeeded();
            runImpl();
            EventBus.getDefault().postSticky(createEventSuccess());
        } catch (Exception e) {
            EventBus.getDefault().postSticky(createEventError(e));
        }
    }

    protected abstract void runImpl() throws Exception;
}
