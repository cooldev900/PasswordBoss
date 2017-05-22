package com.passwordboss.android.event;

import com.passwordboss.android.fragment.SettingsFragment;

public class AutoLockTimeSelectedEvent extends SettingItemResultEvent {
    private SettingsFragment.AutoLockTimeLimitValue mValue;

    public AutoLockTimeSelectedEvent(SettingsFragment.AutoLockTimeLimitValue value) {
        mValue = value;
    }

    public SettingsFragment.AutoLockTimeLimitValue getValue() {
        return mValue;
    }
}
