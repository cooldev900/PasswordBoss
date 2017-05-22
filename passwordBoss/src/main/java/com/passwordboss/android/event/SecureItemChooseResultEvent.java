package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemChooseResultEvent extends BaseEvent {
    private final SecureItem mSecureItem;

    public SecureItemChooseResultEvent(SecureItem secureItem) {
        mSecureItem = secureItem;
    }

    public SecureItem getSecureItem() {
        return mSecureItem;
    }
}
