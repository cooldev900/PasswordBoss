package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.SecureItem;

public class SecureItemSavedEvent extends SecureItemChangedEvent {

    public SecureItemSavedEvent(SecureItem secureItem) {
        super(secureItem);
    }

}
