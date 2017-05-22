package com.passwordboss.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.fragment.GetStartedFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetStartedActivity extends BaseActivity {

    @Bind(R.id.ac_gs_pagination_indicator)
    RadioGroup mIndicatorView;
    @Bind(R.id.ac_gs_pager)
    ViewPager mPagerView;
    @Bind(R.id.ac_gs_skip)
    TextView mSkipView;
    private PagerAdapter mPagerAdapter;

    private void initializePager() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerView.setAdapter(mPagerAdapter);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            View.inflate(this, R.layout.view_pagination_indicator, mIndicatorView);
        }
        onPageSelected(0);
        mPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GetStartedActivity.this.onPageSelected(position);
            }
        });
    }

    @OnClick(R.id.ac_gs_skip)
    void onClickSkip() {
        int currentItem = mPagerView.getCurrentItem();
        if (currentItem <= mPagerAdapter.getCount() - 1) { // Skip
            AnalyticsHelperSegment.logProductTour(this,
                    currentItem + 1,
                    mPagerAdapter.getScreeName(currentItem),
                    AnalyticsHelperSegment.BUTTON_CLICKED_VALUE_CLOSE);
        } else { // Continue
            AnalyticsHelperSegment.logProductTour(this,
                    currentItem + 1,
                    mPagerAdapter.getScreeName(currentItem),
                    AnalyticsHelperSegment.BUTTON_CLICKED_VALUE_COUNTINUE);
        }
        startActivity(new Intent(this, SignUpEmailActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        ButterKnife.bind(this);
        initializePager();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShowNextGetStartedPage(ShowNextGetStartedPageEvent event) {
        int nextItem = mPagerView.getCurrentItem() + 1;
        if (nextItem > mPagerAdapter.getCount() - 1) {
            onClickSkip();
            return;
        }
        mPagerView.setCurrentItem(nextItem);
    }

    private void onPageSelected(int position) {
        RadioButton radioButton = (RadioButton) mIndicatorView.getChildAt(position);
        radioButton.setChecked(true);
        int skipTextId = position < mPagerAdapter.getCount() - 1 ? R.string.SkipTour : R.string.Continue;
        mSkipView.setText(skipTextId);
        // creates analytic event about that continue was clicked at previous screen,
        // exclude initial show of screen
        int previousPosition = position - 1;
        if (previousPosition >= 0) {
            AnalyticsHelperSegment.logProductTour(this,
                    previousPosition + 1,
                    mPagerAdapter.getScreeName(previousPosition),
                    AnalyticsHelperSegment.BUTTON_CLICKED_VALUE_COUNTINUE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final PagerData[] mData = {new PagerData(R.layout.fragment_get_started_01, "Tour 1"),
                new PagerData(R.layout.fragment_get_started_02, "Tour 2"),
                new PagerData(R.layout.fragment_get_started_03, "Tour 3"),
                new PagerData(R.layout.fragment_get_started_04, "Tour 4"),
                new PagerData(R.layout.fragment_get_started_05, "Tour 5"),
        };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public Fragment getItem(int position) {
            return GetStartedFragment.newInstance(mData[position].mLayoutId);
        }

        public String getScreeName(int position) {
            return mData[position].mScreenName;
        }


    }

    private static class PagerData {
        private final int mLayoutId;
        private final String mScreenName;

        private PagerData(@LayoutRes int layoutId, String screenNameForAnalytic) {
            mLayoutId = layoutId;
            mScreenName = screenNameForAnalytic;
        }
    }

    public static class ShowNextGetStartedPageEvent extends BaseEvent {
    }


}
