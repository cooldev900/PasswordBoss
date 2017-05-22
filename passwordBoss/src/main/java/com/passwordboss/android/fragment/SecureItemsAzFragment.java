package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.event.SecureItemClickEvent;
import com.passwordboss.android.event.SecureItemsRefreshEvent;
import com.passwordboss.android.event.ViewModeChangedEvent;
import com.passwordboss.android.helper.SecureItemSubTitle;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.widget.AppItemIconView;
import com.passwordboss.android.widget.RecyclerExtView;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.LinearSLM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class SecureItemsAzFragment extends EventBusFragment {
    protected static final String KEY_ITEM_TYPE = "keyItemType";
    @Bind(R.id.fr_rc_empty)
    View mEmptyView;
    @Bind(R.id.fr_rc_progress)
    View mProgressView;
    @Bind(R.id.fr_rc_recycler)
    RecyclerExtView mRecyclerView;
    private SecureItemsAdapter mAdapter;

    public static SecureItemsAzFragment newInstance(ItemType itemType) {
        SecureItemsAzFragment fragment = new SecureItemsAzFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_ITEM_TYPE, itemType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    protected SecureItemsAdapter createAdapter() {
        return new SecureItemsAdapter(getContext(), Pref.getViewMode());
    }

    @Nullable
    private ItemType getItemType() {
        if (null == getArguments()) return null;
        return (ItemType) getArguments().getSerializable(KEY_ITEM_TYPE);
    }

    protected Observable<SecureItem> getSecureItemsObservable() {
        return DatabaseHelperSecure.getObservable()
                .flatMap(helperSecure -> {
                    SecureItemBll secureItemBll = null;
                    try {
                        secureItemBll = new SecureItemBll(helperSecure);
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                    return secureItemBll.getSecureItemsByType(getItemType());
                });
    }

    private void loadData() {
        getSecureItemsObservable()
                .toList()
                .compose(new ApplySchedulers<>())
                .subscribe(items -> {
                    mAdapter.setData(items);
                    mProgressView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setEmptyView(mEmptyView);
                });
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemsRefreshEvent event) {
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ViewModeChangedEvent event) {
        mAdapter.setViewMode(event.getViewMode());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setEmptyView(mProgressView);
        mAdapter = createAdapter();
        mRecyclerView.setLayoutManager(new LayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    public static class SecureItemsAdapter extends RecyclerView.Adapter<SecureItemsAdapter.ItemViewHolder> {

        final static Ordering<SecureItem> ORDERING_NAME = Ordering.natural().nullsFirst().onResultOf(new Function<SecureItem, Comparable>() {
            @Override
            public Comparable apply(SecureItem input) {
                return Strings.nullToEmpty(input.getName()).toLowerCase();
            }
        });
        private static final int VIEW_TYPE_ITEM_TILE = 0;
        private static final int VIEW_TYPE_ITEM_ROW = 1;
        protected final ArrayList<Item> mItems = new ArrayList<>();
        private final int mGridColumnWidth;
        private Pref.ViewMode mViewMode;

        public SecureItemsAdapter(Context context, Pref.ViewMode viewMode) {
            mGridColumnWidth = context.getResources().getDimensionPixelSize(R.dimen.grid_column_width);
            mViewMode = viewMode;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mViewMode == Pref.ViewMode.Grid ? VIEW_TYPE_ITEM_TILE : VIEW_TYPE_ITEM_ROW;
        }

        private LayoutManager.LayoutParams getLayoutParams(ItemViewHolder holder) {
            GridSLM.LayoutParams layoutParams = GridSLM.LayoutParams.from(holder.mView.getLayoutParams());
            if (mViewMode == Pref.ViewMode.Grid) {
                layoutParams.setSlm(GridSLM.ID);
                layoutParams.setColumnWidth(mGridColumnWidth);
            } else {
                layoutParams.setSlm(LinearSLM.ID);
            }
            return layoutParams;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Item item = mItems.get(position);
            switch (getItemViewType(position)) {
                case VIEW_TYPE_ITEM_ROW:
                    onBindViewHolderRow(holder, item);
                    break;
                case VIEW_TYPE_ITEM_TILE:
                    onBindViewHolderTile(holder, item);
                    break;
            }
            LayoutManager.LayoutParams layoutParams = getLayoutParams(holder);
            layoutParams.setFirstPosition(item.mSectionFirstPosition);
            holder.mView.setLayoutParams(layoutParams);
        }

        private void onBindViewHolderRow(ItemViewHolder holder, Item item) {
            SecureItemRowViewHolder secureItemViewHolder = (SecureItemRowViewHolder) holder;
            SecureItem secureItem = item.mSecureItem;
            @ColorInt
            Integer color = null == secureItem ? null : secureItem.getColor();
            secureItemViewHolder.mIconView.setItemType(ItemType.from(secureItem), color);
            secureItemViewHolder.mTitleView.setText(null == secureItem ? null : secureItem.getName());
            secureItemViewHolder.mSubTitleView.setText(item.mSubTitle);
            secureItemViewHolder.mSubTitleView.setVisibility(Strings.isNullOrEmpty(item.mSubTitle) ? View.GONE : View.VISIBLE);
            holder.mView.setTag(secureItem);
        }

        private void onBindViewHolderTile(ItemViewHolder holder, Item item) {
            SecureItemTileViewHolder secureItemViewHolder = (SecureItemTileViewHolder) holder;
            SecureItem secureItem = item.mSecureItem;
            @ColorInt
            Integer color = null == secureItem ? null : secureItem.getColor();
            secureItemViewHolder.mIconView.setItemType(ItemType.from(secureItem), color);
            secureItemViewHolder.mNameView.setText(null == secureItem ? null : secureItem.getName());
            holder.mView.setTag(secureItem);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_ITEM_ROW:
                    return new SecureItemRowViewHolder(LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_secure_item_row, parent, false));
                case VIEW_TYPE_ITEM_TILE:
                    return new SecureItemTileViewHolder(LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_secure_item_tile, parent, false));
                default:
                    return null;
            }
        }

        public void setData(@NonNull List<SecureItem> secureItems) {
            mItems.clear();
            Collections.sort(secureItems, ORDERING_NAME);
            for (SecureItem secureItem : secureItems) {
                mItems.add(new Item(secureItem, 0));
            }
            notifyDataSetChanged();
        }

        public void setViewMode(Pref.ViewMode viewMode) {
            mViewMode = viewMode;
            notifyDataSetChanged();
        }

        public static class SecureItemTileViewHolder extends ItemViewHolder {
            @Bind(R.id.it_sitl_icon)
            AppItemIconView mIconView;
            @Bind(R.id.it_sitl_name)
            TextView mNameView;

            public SecureItemTileViewHolder(View itemView) {
                super(itemView);
            }
        }

        public static class SecureItemRowViewHolder extends ItemViewHolder {
            private final AppItemIconView mIconView;
            private final TextView mTitleView;
            private final TextView mSubTitleView;
            private final View mView;

            public SecureItemRowViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mView.setOnClickListener(v -> {
                    SecureItem secureItem = (SecureItem) v.getTag();
                    if (null == secureItem) return;
                    EventBus.getDefault().post(new SecureItemClickEvent(secureItem));
                });
                mIconView = (AppItemIconView) itemView.findViewById(R.id.it_sirw_icon);
                mTitleView = (TextView) itemView.findViewById(R.id.it_sirw_title);
                mSubTitleView = (TextView) itemView.findViewById(R.id.it_sirw_sub_title);
            }
        }


        abstract public static class ItemViewHolder extends RecyclerView.ViewHolder {
            protected final View mView;

            public ItemViewHolder(View view) {
                super(view);
                mView = view;
                mView.setOnClickListener(v -> {
                    SecureItem secureItem = (SecureItem) v.getTag();
                    if (null == secureItem) return;
                    EventBus.getDefault().post(new SecureItemClickEvent(secureItem));
                });
                ButterKnife.bind(this, view);
            }
        }
    }

    protected static class Item {
        protected final SecureItem mSecureItem;
        private final String mSubTitle;
        private final int mSectionFirstPosition;

        protected Item(SecureItem secureItem, int sectionFirstPosition) {
            mSecureItem = secureItem;
            mSectionFirstPosition = sectionFirstPosition;
            mSubTitle = new SecureItemSubTitle(secureItem).get();
        }

    }


}
