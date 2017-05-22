package com.passwordboss.android.widget.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PaddingItemDecoration extends RecyclerView.ItemDecoration {
    protected final int mLeft;
    protected final int mTop;
    protected final int mRight;
    protected final int mBottom;

    public PaddingItemDecoration(int padding) {
        this(padding, padding);
    }

    public PaddingItemDecoration(int horizontal, int vertical) {
        this(horizontal, vertical, horizontal, vertical);
    }

    public PaddingItemDecoration(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mLeft;
        outRect.top = mTop;
        outRect.right = mRight;
        outRect.bottom = mBottom;
    }
}
