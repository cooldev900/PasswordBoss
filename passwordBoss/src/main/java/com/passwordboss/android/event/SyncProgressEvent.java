package com.passwordboss.android.event;

public class SyncProgressEvent extends BaseEvent {
    public final int Progress;

    public SyncProgressEvent(Throwable throwable) {
        super(throwable);
        Progress = 0;
    }

    public SyncProgressEvent(int progress) {
        Progress = progress;
    }
}
