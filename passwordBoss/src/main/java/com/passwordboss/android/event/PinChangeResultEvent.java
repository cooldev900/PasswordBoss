package com.passwordboss.android.event;

public class PinChangeResultEvent extends PinResultEvent {
    private final boolean mPinAdded;

    public PinChangeResultEvent(boolean pinAdded) {
        mPinAdded = pinAdded;
    }

    public boolean isPinAdded() {
        return mPinAdded;
    }
}
