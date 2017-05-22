package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.UserIdentity;
import com.passwordboss.android.database.bll.UserIdentityBll;
import com.passwordboss.android.dialog.alert.DeleteItemDialog;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.IdentitiesRefreshEvent;
import com.passwordboss.android.event.IdentityViewEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.AvatarResourceMap;
import com.passwordboss.android.utils.Utils;
import com.passwordboss.android.widget.decoration.PaddingItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IdentitiesFragment extends ToolbarFragment {

    @Bind(R.id.fr_id_button_add)
    Button btnAddBottom;
    @Bind(R.id.fr_id_button_add_fab)
    FloatingActionButton btnAddFab;
    @Bind(R.id.fr_iden_header)
    TextView header;
    @Bind(R.id.fr_iden_identity_list)
    RecyclerView identityList;
    @Bind(R.id.fr_iden_list_container)
    CoordinatorLayout listContainer;
    @Bind(R.id.fr_id_slpash_container)
    View splashContainer;
    private IdentityAdapter mAdapter;
    private View.OnClickListener onDeleteClickListener = v -> confirmDeleteIdentity((UserIdentity) v.getTag());
    private View.OnClickListener onItemClickListener = v -> {
        UserIdentity userIdentity = (UserIdentity) v.getTag();
        EventBus.getDefault().post(new IdentityViewEvent(userIdentity));
    };

    private void confirmDeleteIdentity(UserIdentity userIdentity) {
        new DeleteItemDialog(getContext()).show(() -> DatabaseHelperSecure.getObservable()
                .map(h -> {
                    try {
                        return new UserIdentityBll(h);
                    } catch (SQLException e) {
                        throw new IllegalStateException("Could not initialize database");
                    }
                })
                .subscribe(new BaseObserver<UserIdentityBll>(getActivity()) {
                    @Override
                    public void onNext(UserIdentityBll userIdentityBll) {
                        userIdentityBll.delete(userIdentity);
                        onEvent(new IdentitiesRefreshEvent());
                    }
                }), () -> mAdapter.closeAllItems());
    }

    private Ordering<UserIdentity> createOrdering() {
        return Ordering.natural().onResultOf(new Function<UserIdentity, Comparable>() {
            @Override
            public Comparable apply(UserIdentity input) {
                return !input.isDefault(); // false - moves it to top
            }
        }).compound(Ordering.natural().onResultOf(new Function<UserIdentity, Comparable>() {
            @Override
            public Comparable apply(UserIdentity input) {
                return input.getIdentityName();
            }
        }));
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        }
        toolbar.setTitle(R.string.NavIdentities);
    }

    private void loadData() {
        DatabaseHelperSecure.getObservable()
                .map(h -> {
                    try {
                        List<UserIdentity> identities = new UserIdentityBll(h).getIdentities();
                        Collections.sort(identities, createOrdering());
                        return identities;
                    } catch (SQLException e) {
                        throw new IllegalStateException("Could not initialize database");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserIdentity>>(getContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        updateUiVisibility(false);
                    }

                    @Override
                    public void onNext(List<UserIdentity> identities) {
                        mAdapter.setData(identities);
                    }
                });
    }

    @OnClick({R.id.fr_id_button_add_fab, R.id.fr_id_button_add})
    void onClickButtonAdd() {
        EventBus.getDefault().post(new AddItemEvent(ItemType.Identity));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_identities, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IdentitiesRefreshEvent event) {
        loadData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        identityList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final int paddingHalf = getResources().getDimensionPixelSize(R.dimen.padding_half);
        final int paddingQuarter = getResources().getDimensionPixelSize(R.dimen.padding_quarter);
        identityList.addItemDecoration(new PaddingItemDecoration(paddingHalf, paddingQuarter));
        mAdapter = new IdentityAdapter(getContext(), onItemClickListener, onDeleteClickListener);
        identityList.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateUiVisibility(mAdapter.getItemCount() > 0);
            }
        });
        loadData();
    }

    private void updateUiVisibility(boolean showList) {
        splashContainer.setVisibility(showList ? View.GONE : View.VISIBLE);
        btnAddBottom.setVisibility(showList ? View.GONE : View.VISIBLE);
        identityList.setVisibility(showList ? View.VISIBLE : View.GONE);
        header.setVisibility(showList ? View.VISIBLE : View.GONE);
        btnAddFab.setVisibility(showList ? View.VISIBLE : View.GONE);
    }

    public static class IdentityAdapter extends RecyclerSwipeAdapter<IdentityAdapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<UserIdentity> mObjects;
        private View.OnClickListener mOnDeleteClickListener;
        private View.OnClickListener mOnItemClickListener;
        private AvatarResourceMap resourceMap;

        public IdentityAdapter(Context c,
                               View.OnClickListener onItemClickListener,
                               View.OnClickListener onDeleteClickListener) {
            mContext = c;
            mInflater = LayoutInflater.from(c);
            mObjects = new ArrayList<>();
            resourceMap = Utils.getAvatarResourceMap();
            mOnItemClickListener = onItemClickListener;
            mOnDeleteClickListener = onDeleteClickListener;
        }

        @Override
        public int getItemCount() {
            return mObjects.size();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.it_iden_swipe;
        }

        @Override
        public void onBindViewHolder(ViewHolder vh, int position) {
            mItemManger.bindView(vh.swipeLayout, position);
            UserIdentity userIdentity = mObjects.get(position);
            String identityName = userIdentity.getIdentityName();
            String avatar = userIdentity.getAvatar();
            Glide.with(mContext)
                    .load(resourceMap.get(avatar))
                    .into(vh.imgAvatar);
            vh.txtName.setText(identityName);
            final String itemsCount = Integer.toString(userIdentity.getItemsCount()) + " " + mContext.getString(R.string.Items);
            vh.txtItemCount.setText(itemsCount);
            vh.itemView.setTag(position);

            vh.mStarView.setVisibility(userIdentity.isDefault() ? View.VISIBLE : View.INVISIBLE);

            vh.contentContainer.setTag(userIdentity);
            vh.contentContainer.setOnClickListener(mOnItemClickListener);

            vh.removeContainer.setTag(userIdentity);
            vh.removeContainer.setOnClickListener(mOnDeleteClickListener);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_identity, parent, false));
        }

        public void setData(List<UserIdentity> list) {
            closeAllItems();
            mObjects.clear();
            mObjects.addAll(list);
            notifyDataSetChanged();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final View mStarView;
            public View contentContainer;
            public ImageView imgAvatar;
            public View removeContainer;
            public SwipeLayout swipeLayout;
            public TextView txtItemCount;
            public TextView txtName;

            public ViewHolder(View itemView) {
                super(itemView);
                imgAvatar = (ImageView) itemView.findViewById(R.id.it_iden_avatar);
                txtName = (TextView) itemView.findViewById(R.id.it_iden_name);
                txtItemCount = (TextView) itemView.findViewById(R.id.it_iden_item_count);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.it_iden_swipe);
                contentContainer = itemView.findViewById(R.id.it_iden_content_container);
                removeContainer = itemView.findViewById(R.id.in_sd_delete);
                mStarView = itemView.findViewById(R.id.it_id_star);
            }
        }
    }
}
