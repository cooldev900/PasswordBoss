package com.passwordboss.android.fragment;

import android.os.Bundle;

import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.model.ItemType;

import rx.Observable;

public class SecureItemsFavoritesFragment extends SecureItemsAzFragment {
    public static SecureItemsFavoritesFragment newInstance(ItemType itemType) {
        SecureItemsFavoritesFragment fragment = new SecureItemsFavoritesFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_ITEM_TYPE, itemType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected Observable<SecureItem> getSecureItemsObservable() {
        return super.getSecureItemsObservable()
                .filter(SecureItem::isFavorite);
    }
}
