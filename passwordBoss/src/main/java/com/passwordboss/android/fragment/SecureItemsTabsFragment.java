package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.adapter.TabPagerAdapter;
import com.passwordboss.android.event.ItemTypeAddEvent;
import com.passwordboss.android.event.ViewModeChangedEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.widget.sliding_tab.SlidingTabLayout;
import com.passwordboss.android.widget.sliding_tab.TabColorizer;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecureItemsTabsFragment extends ToolbarFragment implements Toolbar.OnMenuItemClickListener {
    private static final String KEY_ITEM_TYPE = "keyItemType";
    @Bind(R.id.fr_sit_sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @Bind(R.id.fr_sit_pager)
    ViewPager mViewPager;

    public static SecureItemsTabsFragment newInstance(ItemType itemType) {
        SecureItemsTabsFragment fragment = new SecureItemsTabsFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_ITEM_TYPE, itemType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    private ItemType getItemType() {
        Bundle arguments = getArguments();
        if (null == arguments) return null;
        return (ItemType) arguments.getSerializable(KEY_ITEM_TYPE);
    }

    private void invalidateToolbar(Pref.ViewMode viewMode) {
        setToolbarMenuVisibility(R.id.menu_view_grid, viewMode == Pref.ViewMode.List);
        setToolbarMenuVisibility(R.id.menu_view_list, viewMode == Pref.ViewMode.Grid);
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        // do not call super method, because toolbar is cleared and invalided by parent fragment
        toolbar.inflateMenu(R.menu.fragment_secure_items_tab);
        toolbar.setOnMenuItemClickListener(this);
        invalidateToolbar(Pref.getViewMode());
    }

    @OnClick(R.id.fr_sit_button_new)
    void onClickButtonNew() {
        EventBus.getDefault().post(new ItemTypeAddEvent(getItemType()));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secure_items_tabs, null);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_grid:
                setViewMode(Pref.ViewMode.Grid);
                return true;
            case R.id.menu_view_list:
                setViewMode(Pref.ViewMode.List);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager()) {
            @SuppressWarnings("unchecked")
            @Override
            protected TabPagerAdapter.TabAdapterItem[] createItems() {
                return new TabAdapterItem[]{
                        new TabAdapterItem(() -> SecureItemsAzFragment.newInstance(getItemType()), R.string.AZ),
                        new TabAdapterItem(() -> SecureItemsFolderFragment.newInstance(getItemType()), R.string.ItemFolder),
                        new TabAdapterItem(() -> SecureItemsFavoritesFragment.newInstance(getItemType()), R.string.Favorites)
                };
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(adapter.getCount()); // keep all tabs at memory, for quick switching
        mSlidingTabLayout.setCustomTabView(R.layout.view_tab, R.id.vw_tb_text);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new TabColorizer());
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    public void setViewMode(Pref.ViewMode viewMode) {
        Pref.setViewMode(viewMode);
        EventBus.getDefault().post(new ViewModeChangedEvent(viewMode));
        invalidateToolbar(viewMode);
    }
}
