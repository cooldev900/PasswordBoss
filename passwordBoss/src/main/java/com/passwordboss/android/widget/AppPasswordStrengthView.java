package com.passwordboss.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.PasswordScanner;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AppPasswordStrengthView extends RelativeLayout {
    private final PasswordScanner mPasswordScanner = new PasswordScanner();
    @Bind(R.id.vw_ps_progress)
    ProgressBar mProgressBar;
    @Bind(R.id.vw_ps_strength)
    TextView mStrengthView;
    @Bind(R.id.vw_ps_label_strength)
    TextView mLabelStrengthView;

    public AppPasswordStrengthView(Context context) {
        super(context);
        initialize();
    }

    public AppPasswordStrengthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AppPasswordStrengthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void changeProgressBarColor(@ColorInt int color) {
        Drawable backgroundDrawable = createShapeDrawable(Color.GRAY);
        @SuppressLint("RtlHardcoded")
        Drawable progressDrawable = new ScaleDrawable(createShapeDrawable(color), Gravity.LEFT, 1, -1);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, progressDrawable});
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);
        mProgressBar.setProgressDrawable(layerDrawable);
    }

    private Drawable createShapeDrawable(@ColorInt int color) {
        int radius = getResources().getDimensionPixelSize(R.dimen.vw_ps_corner_radius);
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, radius);
        ShapeDrawable drawable = new ShapeDrawable(new RoundRectShape(outerRadii, null, null));
        drawable.getPaint().setColor(color);
        return drawable;
    }

    private int getStrengthColor(PasswordScanner.Strength strength) {
        switch (strength) {
            case Medium:
                return 0xffffc600;
            case Strong:
                return 0x4000ff00;
            case VeryStrong:
                return 0xff00ff00;
            default:
                return 0xffff0000;
        }
    }

    private void initialize() {
        View view = View.inflate(getContext(), R.layout.view_password_strength, this);
        ButterKnife.bind(this, view);
        changeProgressBarColor(Color.RED);
    }

    public void linkTo(AppPasswordView passwordView) {
        if (null == passwordView) return;
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateValues(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        updateValues(passwordView.getText());
    }

    private void updateValues(Editable s) {
        String password = s.toString();
        mPasswordScanner.scanPassword(password);
        PasswordScanner.Strength strength = mPasswordScanner.getStrength();
        mProgressBar.setProgress(strength.getLength());
        changeProgressBarColor(getStrengthColor(strength));
        mStrengthView.setText(strength.getStringId());
        int strengthVisibility = TextUtils.isEmpty(password) ? GONE : VISIBLE;
        mLabelStrengthView.setVisibility(strengthVisibility);
        mStrengthView.setVisibility(strengthVisibility);
    }

}
