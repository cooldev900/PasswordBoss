package com.passwordboss.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.passwordboss.android.R;

public class BuyButton extends FrameLayout {

    private Context mContext;

    public BuyButton(Context context) {
        super(context);
        mContext = context;
    }

    public BuyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public BuyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs){
        LayoutInflater.from(mContext).inflate(R.layout.view_button_buy, this, true);
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.BuyButton, -1, -1);
        String textPeriod = a.getString(R.styleable.BuyButton_textPeriod);
        String textPrice = a.getString(R.styleable.BuyButton_textPrice);
        boolean isTextPerYearVisible = a.getBoolean(R.styleable.BuyButton_isTextPerYearVisible, false);
        Drawable discountDrawable = a.getDrawable(R.styleable.BuyButton_discountDrawable);
        a.recycle();

        ((TextView) findViewById(R.id.btn_buy_text_period)).setText(textPeriod + " ");
        ((TextView) findViewById(R.id.btn_buy_text_price)).setText(textPrice);

        ImageView discountImageView = (ImageView) findViewById(R.id.btn_buy_img_discount);
        if(discountDrawable != null){
            discountImageView.setVisibility(View.VISIBLE);
            discountImageView.setImageDrawable(discountDrawable);
        } else {
            discountImageView.setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.btn_buy_text_per_year)
                .setVisibility(isTextPerYearVisible ? View.VISIBLE : View.GONE);
    }
}
