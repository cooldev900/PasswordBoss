package com.passwordboss.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.passwordboss.android.R;

public class AppPasswordView extends EditText {
    public AppPasswordView(Context context) {
        super(context);
        initialize();
    }

    public AppPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AppPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setSingleLine(true);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
        setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.padding_quarter));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Drawable drawable = getCompoundDrawables()[2];
        if (drawable == null) return super.onTouchEvent(event);
        if (event.getAction() != MotionEvent.ACTION_UP) return super.onTouchEvent(event);
        if (event.getX() > getWidth() - getPaddingRight() - drawable.getIntrinsicWidth()) {
            TransformationMethod transformationMethod = getTransformationMethod();
            if (null == transformationMethod) {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye);
                setTransformationMethod(new PasswordTransformationMethod());
            } else {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye_off);
                setTransformationMethod(null);
            }
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            setSelection(length());
            return true;
        }
        return super.onTouchEvent(event);
    }
}
