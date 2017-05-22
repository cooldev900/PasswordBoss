package com.passwordboss.android.event;

public class SyncResultEvent extends BaseEvent {
    public SyncResultEvent(Throwable throwable) {
        super(throwable);
    }

    public SyncResultEvent() {
    }
}
