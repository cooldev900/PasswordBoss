package com.passwordboss.android.event;

public class BaseEvent {
    private final Throwable mThrowable;

    public BaseEvent(Throwable throwable) {
        mThrowable = throwable;
    }

    public BaseEvent() {
        this(null);
    }

    public Throwable getError() {
        return mThrowable;
    }

    public boolean hasError() {
        return null != mThrowable;
    }

    public boolean isSuccessful() {
        return !hasError();
    }
}
