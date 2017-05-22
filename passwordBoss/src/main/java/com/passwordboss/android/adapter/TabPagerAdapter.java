package com.passwordboss.android.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.passwordboss.android.app.App;
import com.passwordboss.android.fragment.BaseFragment;

public abstract class TabPagerAdapter extends FragmentStatePagerAdapter {
    private final TabAdapterItem[] mItems;

    public TabPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mItems = createItems();
    }

    protected abstract TabAdapterItem[] createItems();

    @Override
    public int getCount() {
        return mItems.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mItems[position].mFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return App.get().getText(mItems[position].mTabTitleResId);
    }

    public interface Fragment {
        BaseFragment newInstance();
    }

    public static class TabAdapterItem {
        private final Fragment mFragment;
        private final int mTabTitleResId;

        public TabAdapterItem(Fragment fragment, int tabTitleResId) {
            mFragment = fragment;
            mTabTitleResId = tabTitleResId;
        }
    }
}
