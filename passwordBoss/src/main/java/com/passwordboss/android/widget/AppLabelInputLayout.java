package com.passwordboss.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.passwordboss.android.R;

public class AppLabelInputLayout extends LinearLayout {
    public AppLabelInputLayout(Context context) {
        super(context);
        initialize();
    }

    public AppLabelInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        processAttributeSet(attrs);
    }

    public AppLabelInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        processAttributeSet(attrs);
    }

    private void initialize() {
        setOrientation(VERTICAL);
    }

    private void processAttributeSet( AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppLabelInputLayout);
        CharSequence text = typedArray.getText(R.styleable.AppLabelInputLayout_android_text);
        setText(text);
        typedArray.recycle();
    }

    public void setText(CharSequence label) {
        TextView labelView = (TextView) findViewById(R.id.vw_lll_label);
        if (TextUtils.isEmpty(label)) {
            removeView(labelView);
        } else {
            if (null == labelView) {
                labelView = new TextView(getContext());
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                labelView.setLayoutParams(layoutParams);
                int left = getResources().getDimensionPixelOffset(R.dimen.padding_half);
                labelView.setPadding(left, 0, 0, 0);
                labelView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorAccent));
                labelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.fr_si_input_label_text_size));
                addView(labelView, 0);
            }
            labelView.setText(label);
        }
    }


}
