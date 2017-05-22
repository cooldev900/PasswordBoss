package com.passwordboss.android.event;

import com.passwordboss.android.model.ItemType;

public class SecureItemChooseEvent extends BaseEvent {
    private final ItemType mItemType;

    public SecureItemChooseEvent(ItemType itemType) {
        mItemType = itemType;
    }

    public ItemType getItemType() {
        return mItemType;
    }
}
