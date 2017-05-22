package com.passwordboss.android.event;

import com.passwordboss.android.utils.Pref;

public class ViewModeChangedEvent {
    private final Pref.ViewMode mViewMode;

    public ViewModeChangedEvent(Pref.ViewMode viewMode) {
        mViewMode = viewMode;
    }

    public Pref.ViewMode getViewMode() {
        return mViewMode;
    }
}
