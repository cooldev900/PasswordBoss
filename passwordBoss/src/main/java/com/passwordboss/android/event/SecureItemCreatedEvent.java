package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemCreatedEvent extends SecureItemSavedEvent {

    public SecureItemCreatedEvent(SecureItem secureItem) {
        super(secureItem);
    }
}
