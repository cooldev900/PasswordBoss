package com.passwordboss.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.utils.Pref;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class SecureItemsFolderFragment extends SecureItemsAzFragment {

    public static SecureItemsFolderFragment newInstance(ItemType itemType) {
        SecureItemsFolderFragment fragment = new SecureItemsFolderFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_ITEM_TYPE, itemType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    protected SecureItemsAdapter createAdapter() {
        return new FolderSecureItemsAdapter(getContext(), Pref.getViewMode());
    }

    public static class FolderSecureItemsAdapter extends SecureItemsAdapter {
        private static final int VIEW_TYPE_ITEM_FOLDER = 2;
        private static final Ordering<SecureItem> ORDERING_FOLDER = Ordering.natural().nullsFirst().onResultOf(new Function<SecureItem, Comparable>() {
            @Override
            public Comparable apply(SecureItem input) {
                return input.getFolder();
            }
        }).compound(ORDERING_NAME);

        public FolderSecureItemsAdapter(Context context, Pref.ViewMode viewMode) {
            super(context, viewMode);
        }

        @Override
        public int getItemViewType(int position) {
            SecureItemsAzFragment.Item item = mItems.get(position);
            if (item instanceof FolderItem) return VIEW_TYPE_ITEM_FOLDER;
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            if (getItemViewType(position) == VIEW_TYPE_ITEM_FOLDER) {
                onBindViewHolderFolder(holder, (FolderItem) mItems.get(position));
                // call super implementation to setup SuperSLIM stuff
            }
            super.onBindViewHolder(holder, position);
        }

        private void onBindViewHolderFolder(ItemViewHolder holder, FolderItem item) {
            FolderViewHolder folderViewHolder = (FolderViewHolder) holder;
            folderViewHolder.mTitleView.setText(item.mFolderName);
            holder.mView.setTag(null);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM_FOLDER) {
                return new FolderViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_header_folder, parent, false));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void setData(@NonNull List<SecureItem> secureItems) {
            mItems.clear();
            Collections.sort(secureItems, ORDERING_FOLDER);
            String folderId = "";
            int sectionFirstPosition = 0;
            for (SecureItem secureItem : secureItems) {
                final Folder itemFolder = secureItem.getFolder();
                final String itemFolderId = null == itemFolder ? "" : itemFolder.getId();
                if (!folderId.equals(itemFolderId)) {
                    folderId = itemFolderId;
                    sectionFirstPosition = mItems.size();
                    final String folderName = null == itemFolder ? "" : itemFolder.getName();
                    mItems.add(new FolderItem(folderName, sectionFirstPosition));
                }
                mItems.add(new Item(secureItem, sectionFirstPosition));
            }
            notifyDataSetChanged();
        }

        public static class FolderViewHolder extends ItemViewHolder {
            @Bind(R.id.im_hf_title)
            TextView mTitleView;

            public FolderViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

    private static class FolderItem extends SecureItemsAzFragment.Item {
        private final String mFolderName;

        private FolderItem(String folderName, int sectionFirstPosition) {
            super(null, sectionFirstPosition);
            mFolderName = folderName;
        }

    }
}