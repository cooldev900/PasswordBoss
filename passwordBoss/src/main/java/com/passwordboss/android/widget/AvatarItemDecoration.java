package com.passwordboss.android.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Boris on 31/05/16.
 */
public class AvatarItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    private int mSpaceHalf;

    public AvatarItemDecoration(int space) {
        mSpace = space;
        mSpaceHalf = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if(position >= 0 || position <= 3){
            outRect.top = mSpace;
            outRect.bottom = mSpaceHalf;
        } else if(position >= 16 && position <= 19){
            outRect.top = mSpaceHalf;
            outRect.bottom = mSpace;
        } else {
            outRect.top = mSpaceHalf;
            outRect.bottom = mSpaceHalf;
        }

        if(position % 4 == 0){
            outRect.left = mSpace;
            outRect.right = mSpaceHalf;
        } else if(((position - 3) % 4) == 0){
            outRect.left = mSpaceHalf;
            outRect.right = mSpace;
        } else {
            outRect.left = mSpaceHalf;
            outRect.right = mSpaceHalf;
        }
    }
}
