package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemEditEvent extends BaseEvent {
    private final SecureItem mSecureItem;

    public SecureItemEditEvent(SecureItem secureItem) {
        mSecureItem = secureItem;
    }

    public SecureItem getSecureItem() {
        return mSecureItem;
    }
}
