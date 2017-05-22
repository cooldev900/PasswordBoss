package com.passwordboss.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.passwordboss.android.R;

/**
 * Created by Boris on 24/05/16.
 */
public class FeatureView extends LinearLayout {

    private Context mContext;

    public FeatureView(Context context) {
        super(context);
        mContext = context;
    }

    public FeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public FeatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public FeatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(mContext).inflate(R.layout.view_premium_feature, this, true);
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FeatureView, -1, -1);
        String textTitle = a.getString(R.styleable.FeatureView_textTitle);
        String textDescription = a.getString(R.styleable.FeatureView_textDescription);
        Drawable featureDrawable = a.getDrawable(R.styleable.FeatureView_featureDrawable);
        a.recycle();

        ((ImageView) findViewById(R.id.feature_thumbnail)).setImageDrawable(featureDrawable);
        ((TextView) findViewById(R.id.feature_title)).setText(textTitle);
        ((TextView) findViewById(R.id.feature_descriprion)).setText(textDescription);
    }
}
