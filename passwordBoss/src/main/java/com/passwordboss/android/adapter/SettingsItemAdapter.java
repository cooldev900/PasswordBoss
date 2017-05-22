package com.passwordboss.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.passwordboss.android.R;

import java.util.List;

public final class SettingsItemAdapter extends RecyclerView.Adapter<SettingsItemAdapter.SettingsItemViewHolder>{

    private LayoutInflater mInflater;
    private String mCurrentItem;
    private List<String> mITemList;
    private View.OnClickListener onItemClickListener;
    private RadioButton lastChecked;
    private volatile boolean isLocked;

    public SettingsItemAdapter(@NonNull Context c,
                               @NonNull String currentLanguage,
                               @NonNull List<String> languageList,
                               @NonNull View.OnClickListener onItemClickListener){
        mInflater = LayoutInflater.from(c);
        mCurrentItem = currentLanguage;
        mITemList = languageList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SettingsItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new SettingsItemViewHolder(mInflater.inflate(R.layout.item_single_choice, parent, false));
    }

    @Override
    public void onBindViewHolder(SettingsItemViewHolder viewHolder, int posittion) {
        String language = mITemList.get(posittion);
        viewHolder.text.setText(language);
        viewHolder.itemView.setTag(posittion);
        viewHolder.itemView.setOnClickListener(v -> {
            if(isLocked){
                return;
            }
            isLocked = true;
            v.postDelayed(() -> {
                Integer position = (Integer) v.getTag();
                mCurrentItem = mITemList.get(position);
                notifyDataSetChanged();
                onItemClickListener.onClick(v);
                isLocked = false;
            }, 300);
        });
    }

    @Override
    public int getItemCount() {
        return mITemList.size();
    }

    public static final class SettingsItemViewHolder extends RecyclerView.ViewHolder{

        public TextView text;

        public SettingsItemViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.it_pk_text);
        }
    }
}
