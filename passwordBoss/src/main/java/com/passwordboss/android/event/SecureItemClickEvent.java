package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemClickEvent extends BaseEvent {
    private final SecureItem mSecureItem;

    public SecureItemClickEvent(SecureItem secureItem) {
        mSecureItem = secureItem;
    }

    public SecureItem getSecureItem() {
        return mSecureItem;
    }
}
