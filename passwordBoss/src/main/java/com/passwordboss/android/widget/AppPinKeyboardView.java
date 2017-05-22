package com.passwordboss.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.passwordboss.android.R;

public class AppPinKeyboardView extends FrameLayout {
    private OnClickKeypadListener mOnClickKeypadListener;
    private final OnClickListener mOnTextViewClickListner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mOnClickKeypadListener) return;
            TextView textView = (TextView) v;
            if (textView.length() == 0) return;
            mOnClickKeypadListener.onClickKeypad(textView.getText().charAt(0));
        }
    };
    public AppPinKeyboardView(Context context) {
        super(context);
        initialize();
    }


    public AppPinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AppPinKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void bindOnClickListenerToTextViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                bindOnClickListenerToTextViews(viewGroup.getChildAt(i));
            }
        } else if (view instanceof TextView && view.getVisibility() == VISIBLE) {
            view.setClickable(true);
            view.setOnClickListener(mOnTextViewClickListner);
        }
    }

    private void initialize() {
        View view = View.inflate(getContext(), R.layout.view_pin_keyboard, this);
        bindOnClickListenerToTextViews(view);
    }

    public void setOnClickKeypadListener(OnClickKeypadListener onClickKeypadListener) {
        mOnClickKeypadListener = onClickKeypadListener;
    }


    public interface OnClickKeypadListener {
        void onClickKeypad(char number);
    }

}
