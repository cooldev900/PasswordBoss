package com.passwordboss.android.event;

public class PinCheckResultEvent extends PinResultEvent {
    private final boolean mCorrect;

    public PinCheckResultEvent(boolean correct) {
        mCorrect = correct;
    }

    public boolean isCorrect() {
        return mCorrect;
    }
}
