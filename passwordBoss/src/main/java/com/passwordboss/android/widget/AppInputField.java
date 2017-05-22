package com.passwordboss.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class AppInputField extends EditText {

    public AppInputField(Context context) {
        super(context);
        initialize();
    }

    public AppInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AppInputField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        setClickable(true);
        setCursorVisible(false);
        setLongClickable(false);
        setFocusableInTouchMode(true);
        setSingleLine();
        setInputType(EditorInfo.TYPE_NULL);
        setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP && !v.isFocused()) {
                    setError(null);
                    v.callOnClick();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyEvent.KEYCODE_ENTER == keyCode && super.onKeyDown(keyCode, event);
    }
}
