package com.passwordboss.android.widget.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint mPaint;
    private Rect mPadding = new Rect(0, 0, 0, 0);

    public HorizontalDividerItemDecoration(Context context, @ColorRes int colorResId, @DimenRes int heightDimenId) {
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, colorResId));
        mPaint.setStrokeWidth(context.getResources().getDimensionPixelOffset(heightDimenId));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int startX = parent.getPaddingLeft() + mPadding.left;
        int stopX = parent.getWidth() - parent.getPaddingRight() - mPadding.right;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int y = child.getBottom() + params.bottomMargin;

            c.drawLine(startX, y, stopX, y, mPaint);
        }
    }

    public void setPadding(int left, int right) {
        mPadding.left = left;
        mPadding.right = right;
    }

}