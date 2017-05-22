package com.passwordboss.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.passwordboss.android.R;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int[] mAvatarArray;
    private View.OnClickListener mOnClickListener;

    public AvatarAdapter(Context c, int[] avatarArray, View.OnClickListener onClickListener){
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mAvatarArray = avatarArray;
        mOnClickListener = onClickListener;
    }

    @Override
    public AvatarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvatarViewHolder(mInflater.inflate(R.layout.view_avatar_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AvatarViewHolder vh, int position) {
        int avatarResId = mAvatarArray[position];
        Glide.with(mContext)
                .load(avatarResId)
                .into(vh.imgAvatar);
        vh.itemView.setTag(position);
        vh.imgAvatar.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mAvatarArray.length;
    }

    public static class AvatarViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgAvatar;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.view_av_img_avatar);
        }
    }
}
