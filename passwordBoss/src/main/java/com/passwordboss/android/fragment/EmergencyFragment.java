package com.passwordboss.android.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.adapter.TabPagerAdapter;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.widget.sliding_tab.SlidingTabLayout;
import com.passwordboss.android.widget.sliding_tab.TabColorizer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EmergencyFragment extends ToolbarFragment {


    @Bind(R.id.fr_emrg_sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @Bind(R.id.fr_emrg_pager)
    ViewPager mViewPager;

    public static EmergencyFragment newInstance() {
        return new EmergencyFragment();
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        }
        toolbar.setTitle(R.string.NavEmergency);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager()) {
            @SuppressWarnings("unchecked")
            @Override
            protected TabPagerAdapter.TabAdapterItem[] createItems() {
                return new TabAdapterItem[]{
                        new TabAdapterItem(TrustedPeopleFragment::new, R.string.EmergencyPeopleITrust),
                        new TabAdapterItem(TrustingPeopleFragment::newInstance, R.string.EmergencyPeopleWhoTrustMe)
                };
            }
        };
        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setCustomTabView(R.layout.view_tab, R.id.vw_tb_text);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new TabColorizer());
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
