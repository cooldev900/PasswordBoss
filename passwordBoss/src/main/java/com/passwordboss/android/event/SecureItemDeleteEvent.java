package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemDeleteEvent extends BaseEvent {
    private final SecureItem mSecureItem;

    public SecureItemDeleteEvent(SecureItem secureItem) {
        mSecureItem = secureItem;
    }

    public SecureItem getSecureItem() {
        return mSecureItem;
    }
}
