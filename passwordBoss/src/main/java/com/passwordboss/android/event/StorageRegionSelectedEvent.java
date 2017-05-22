package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.StorageRegion;

public class StorageRegionSelectedEvent extends SettingItemResultEvent{
    private StorageRegion mRegion;

    public StorageRegionSelectedEvent(StorageRegion region) {
        mRegion = region;
    }

    public StorageRegion getRegion() {
        return mRegion;
    }
}
