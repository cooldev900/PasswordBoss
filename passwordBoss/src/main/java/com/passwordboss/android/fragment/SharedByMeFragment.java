package com.passwordboss.android.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.ToDo;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharedByMeFragment extends BaseFragment {

    @OnClick({R.id.fr_sbm_button_share_an_item, R.id.fr_sbm_empty_content})
    public void onClickButtonShare() {
        new ToDo().show(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shared_by_me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
