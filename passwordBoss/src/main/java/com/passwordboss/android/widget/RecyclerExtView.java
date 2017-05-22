package com.passwordboss.android.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerExtView extends RecyclerView {
    private View mEmptyView;
    private final AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyStatus();
        }
    };

    public RecyclerExtView(Context context) {
        super(context);
    }

    public RecyclerExtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerExtView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAdapter(Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (getAdapter() != null) {
            getAdapter().registerAdapterDataObserver(mObserver);
        }
        updateEmptyStatus();
    }

    /**
     * Sets the empty view.
     *
     * @param emptyView the empty view.
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        updateEmptyStatus();
    }

    /**
     * Updates the status of the view based on the empty parameter.
     */
    private void updateEmptyStatus() {
        if (mEmptyView == null) {
            setVisibility(View.VISIBLE);
        } else {
            if (getAdapter() != null && getAdapter().getItemCount() > 0) {
                mEmptyView.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }
}
