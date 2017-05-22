package com.passwordboss.android.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.adapter.AvatarAdapter;
import com.passwordboss.android.event.AvatarChosenEvent;
import com.passwordboss.android.widget.AvatarItemDecoration;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ChooseAvatarFragment extends BaseFragment {

    public int[] SMALL_AVATARS = new int[]{
            R.drawable.ic_01_big,
            R.drawable.ic_02_big,
            R.drawable.ic_03_big,
            R.drawable.ic_04_big,
            R.drawable.ic_05_big,
            R.drawable.ic_06_big,
            R.drawable.ic_07_big,
            R.drawable.ic_08_big,
            R.drawable.ic_09_big,
            R.drawable.ic_10_big,
            R.drawable.ic_11_big,
            R.drawable.ic_12_big,
            R.drawable.ic_13_big,
            R.drawable.ic_14_big,
            R.drawable.ic_15_big,
            R.drawable.ic_16_big,
            R.drawable.ic_17_big,
            R.drawable.ic_18_big,
            R.drawable.ic_19_big,
            R.drawable.ic_20_big
    };
    @Bind(R.id.ac_chav_avatar_list)
    RecyclerView avatarList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_avatar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getResources();
        Resources resources = getResources();

        GridLayoutManager glm = new GridLayoutManager(getActivity(), isTablet() ? 5 : 4);
        avatarList.setLayoutManager(glm);
        avatarList.addItemDecoration(new AvatarItemDecoration((int) resources.getDimension(R.dimen.space_between_avatars)));
        avatarList.setAdapter(new AvatarAdapter(getActivity(), SMALL_AVATARS, v -> {
            int position = (int) ((View) v.getParent()).getTag();
            AvatarChosenEvent event = new AvatarChosenEvent(position + 1, SMALL_AVATARS[position]);
            EventBus.getDefault().postSticky(event);
        }));
    }
}
