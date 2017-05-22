package com.passwordboss.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.passwordboss.android.R;

public class AppDashboardButton extends FrameLayout {

    public AppDashboardButton(Context context) {
        super(context);
        initialize(null);
    }

    public AppDashboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public AppDashboardButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppDashboardButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs);
    }

    private void initialize(@Nullable AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_dashboard_button, this);
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.vw_db_button);
        if (!isInEditMode()) {
            button.setOnClickListener(v -> {
                performClick(); // fire click at parent, to deliver event into bound listeners
            });
        }
        if (null == attrs) return;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppDashboardButton);
        CharSequence text = typedArray.getText(R.styleable.AppDashboardButton_android_text);
        TextView textView = (TextView) view.findViewById(R.id.vw_db_text);
        textView.setText(text);
        Drawable drawable = typedArray.getDrawable(R.styleable.AppDashboardButton_android_src);
        button.setImageDrawable(drawable);
        typedArray.recycle();
    }


}
