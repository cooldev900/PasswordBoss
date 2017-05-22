package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.widget.AppItemIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemTypesFragment extends EventBusFragment {

    public static final String KEY_ITEM_TYPE = "keyItemType";
    private static final ItemType[] ROOT_ITEM_TYPES =
            {
                    ItemType.Password,
                    ItemType.DigitalWallet,
                    ItemType.PersonalInfo,
                    ItemType.SecureNotes,
                    ItemType.Identity,
                    ItemType.SharedItem,
                    ItemType.EmergencyContact,
                    ItemType.Folder

            };
    @Bind(R.id.fr_it_header)
    TextView mHeaderView;
    @Bind(R.id.fr_it_recycler)
    RecyclerView mRecyclerView;
    private ItemType mItemType;

    public static ItemTypesFragment newInstance(@Nullable ItemType itemType) {
        ItemTypesFragment fragment = new ItemTypesFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_ITEM_TYPE, itemType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @StringRes
    private int getHeaderTextId(ItemType itemType) {
        if (null == itemType) return R.string.AddDialogueCategoryHeadline;
        if (itemType == ItemType.Password) return R.string.AddDialoguePasswordBody;
        return R.string.AddDialogueOtherBody;
    }

    @Nullable
    private ItemType getItemType() {
        Bundle arguments = getArguments();
        if (null == arguments) return null;
        return (ItemType) arguments.getSerializable(KEY_ITEM_TYPE);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_types, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowItemTypesEvent event) {
        showItemTypes(event.mItemType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddDifferentItemEvent event) {
        showItemTypes(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ITEM_TYPE, mItemType);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), isTablet() ? 4 : 3));
        ItemType itemType;
        if (null == savedInstanceState) {
            itemType = getItemType();
        } else {
            itemType = (ItemType) savedInstanceState.getSerializable(KEY_ITEM_TYPE);
        }
        showItemTypes(itemType);
    }

    private void showItemTypes(ItemType itemType) {
        mItemType = itemType;
        mHeaderView.setText(getHeaderTextId(itemType));
        ItemType[] itemTypes = null == itemType ? ROOT_ITEM_TYPES : itemType.getChildren();
        mRecyclerView.setAdapter(new ItemTypesAdapter(itemTypes));
    }

    private static class ShowItemTypesEvent extends BaseEvent {
        private final ItemType mItemType;

        public ShowItemTypesEvent(ItemType itemType) {
            mItemType = itemType;
        }
    }

    private static class AddDifferentItemEvent extends BaseEvent {
    }

    private static class ItemTypesAdapter extends RecyclerView.Adapter<ItemTypesAdapter.ViewHolder> {
        @NonNull
        private final ItemType[] mItemTypes;

        private ItemTypesAdapter(ItemType[] itemTypes) {
            mItemTypes = null == itemTypes ? new ItemType[0] : itemTypes;
        }

        private BaseEvent createEvent(ItemType itemType) {
            if (itemType == ItemType.AddDifferentItem) {
                return new ItemTypesFragment.AddDifferentItemEvent();
            }
            ItemType[] children = itemType.getChildren();
            if (null != children && children.length > 0)
                return new ItemTypesFragment.ShowItemTypesEvent(itemType);
            return new AddItemEvent(itemType);
        }

        @Override
        public int getItemCount() {
            return mItemTypes.length;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ItemType itemType = mItemTypes[position];
            holder.mContentView.setTag(itemType);
            holder.mNameView.setText(itemType.getNameId());
            holder.mIconView.setItemType(itemType);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_item_type, parent, false);
            view.setOnClickListener(v -> {
                ItemType itemType = (ItemType) v.getTag();
                if (null == itemType) return;
                EventBus.getDefault().postSticky(createEvent(itemType));
            });
            return new ViewHolder(view);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final View mContentView;
            private final TextView mNameView;
            private final AppItemIconView mIconView;

            public ViewHolder(View itemView) {
                super(itemView);
                mContentView = itemView;
                mNameView = (TextView) itemView.findViewById(R.id.rc_it_name);
                mIconView = (AppItemIconView) itemView.findViewById(R.id.rc_it_icon);
            }
        }

    }

}
