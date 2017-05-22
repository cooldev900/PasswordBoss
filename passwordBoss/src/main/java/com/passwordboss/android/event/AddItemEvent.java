package com.passwordboss.android.event;

import com.passwordboss.android.model.ItemType;

public class AddItemEvent extends BaseEvent {
    private final ItemType mItemType;

    public AddItemEvent(ItemType itemType) {
        mItemType = itemType;
    }

    public ItemType getItemType() {
        return mItemType;
    }
}
