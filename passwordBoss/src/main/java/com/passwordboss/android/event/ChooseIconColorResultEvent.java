package com.passwordboss.android.event;

import android.support.annotation.ColorInt;

public class ChooseIconColorResultEvent extends BaseEvent {
    @ColorInt
    public final int Color;

    public ChooseIconColorResultEvent(int color) {
        Color = color;
    }
}
