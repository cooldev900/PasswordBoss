package com.passwordboss.android.event;

/**
 * Created by Boris on 01/06/16.
 */
public class AvatarChosenEvent extends BaseEvent {
    private int mNumber;
    private int resId;

    public AvatarChosenEvent(int number, int resId) {
        this.mNumber = number;
        this.resId = resId;
    }

    public int getNumber() {
        return mNumber;
    }

    public int getResId() {
        return resId;
    }
}
