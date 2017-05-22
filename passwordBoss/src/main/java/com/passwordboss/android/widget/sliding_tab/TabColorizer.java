package com.passwordboss.android.widget.sliding_tab;


import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.passwordboss.android.R;
import com.passwordboss.android.app.App;

public class TabColorizer implements SlidingTabLayout.TabColorizer {
    private static final int COLOR_TAB_INDICATOR = ContextCompat.getColor(App.get(), R.color.colorAccent);
    private static final int COLOR_TAB_DIVIDER = Color.TRANSPARENT;

    @Override
    public int getDividerColor(int position) {
        return COLOR_TAB_DIVIDER;
    }

    @Override
    public int getIndicatorColor(int position) {
        return COLOR_TAB_INDICATOR;
    }
}
