package com.passwordboss.android.toolbar;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import com.passwordboss.android.R;
import com.passwordboss.android.event.UpNavigationEvent;

import org.greenrobot.eventbus.EventBus;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class AppToolbar extends Toolbar {
    public AppToolbar(Context context) {
        super(context);
    }

    public AppToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void displayCloseNavigation() {
        displayUpNavigation(R.drawable.ic_tb_close);
    }

    public void displayUpNavigation() {
        displayUpNavigation(R.drawable.ic_tb_back);
    }

    private void displayUpNavigation(@DrawableRes int resId) {
        setNavigationIcon(resId);
        setNavigationOnClickListener(v -> EventBus.getDefault().post(new UpNavigationEvent()));
    }

    public void reset() {
        getMenu().clear();
        setOnMenuItemClickListener(null);
        setTitle(null);
        setNavigationIcon(null);
        setNavigationOnClickListener(null);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null != title) {
            SpannableStringBuilder builder = new SpannableStringBuilder(title);
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getContext().getAssets(), getContext().getString(R.string.font_regular)));
            builder.setSpan(typefaceSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            title = builder;
        }
        super.setTitle(title);
    }
}
