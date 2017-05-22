package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemChangedEvent extends BaseEvent {
    private final SecureItem mSecureItem;

    public SecureItemChangedEvent(SecureItem secureItem) {
        mSecureItem = secureItem;
    }

    public SecureItem getSecureItem() {
        return mSecureItem;
    }
}
