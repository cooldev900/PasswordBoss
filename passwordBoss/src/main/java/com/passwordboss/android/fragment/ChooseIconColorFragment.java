package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.event.ChooseIconColorResultEvent;
import com.passwordboss.android.model.ItemType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseIconColorFragment extends BaseFragment {

    @Bind(R.id.fr_cic_recycler)
    RecyclerView mRecyclerView;

    private void addColors(List<Integer> colors, ItemType[] itemTypes) {
        for (ItemType itemType : itemTypes) {
            int color = itemType.getColor();
            if (color == Color.TRANSPARENT) continue;
            colors.add(color);
        }
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_icon_color, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        List<Integer> colors = new ArrayList<>();
        addColors(colors, ItemType.Password.getChildren());
        addColors(colors, ItemType.PersonalInfo.getChildren());
        addColors(colors, ItemType.DigitalWallet.getChildren());
        addColors(colors, ItemType.SecureNotes.getChildren());
        mRecyclerView.setAdapter(new ColorsAdapter(colors));
    }

    private static class ColorsAdapter extends RecyclerView.Adapter<ChooseIconColorFragment.ViewHolder> {
        final List<Integer> mColors;

        private ColorsAdapter(List<Integer> colors) {
            mColors = colors;
        }

        @Override
        public int getItemCount() {
            return mColors.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Resources resources = holder.mContentView.getResources();
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(resources.getDimension(R.dimen.padding_quarter));
            Integer color = mColors.get(position);
            drawable.setColor(color);
            holder.mColorView.setBackground(drawable);
            holder.mContentView.setTag(color);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.recycler_view_icon_color, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int color = (int) v.getTag();
                    // use sticky to be able delivery result to requester
                    EventBus.getDefault().postSticky(new ChooseIconColorResultEvent(color));
                }
            });
            return new ViewHolder(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mContentView;
        private final View mColorView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContentView = itemView;
            mColorView = itemView.findViewById(R.id.rv_ic_color);
        }
    }

}

