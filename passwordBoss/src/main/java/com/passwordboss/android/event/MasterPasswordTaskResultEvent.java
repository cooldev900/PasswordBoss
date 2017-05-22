package com.passwordboss.android.event;

public class MasterPasswordTaskResultEvent extends BaseEvent {
    public MasterPasswordTaskResultEvent() {
    }

    public MasterPasswordTaskResultEvent(Throwable throwable) {
        super(throwable);
    }
}
