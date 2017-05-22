package com.passwordboss.android.event;

/**
 * Created by Boris on 19/05/16.
 */
public class SelectSettingEvent extends BaseEvent {
    private int type;

    public SelectSettingEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
