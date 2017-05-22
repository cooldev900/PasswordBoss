package com.passwordboss.android.event;

import com.passwordboss.android.model.ItemType;

public class ItemTypeAddEvent extends BaseEvent {
    private final ItemType mItemType;

    public ItemTypeAddEvent(ItemType itemType) {
        mItemType = itemType;
    }

    public ItemType getItemType() {
        return mItemType;
    }
}
