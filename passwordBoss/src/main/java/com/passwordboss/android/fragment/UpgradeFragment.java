package com.passwordboss.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.helper.ToDo;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpgradeFragment extends BaseFragment {


    @OnClick({R.id.fr_upgr_btn_1_year, R.id.fr_upgr_btn_2_year, R.id.fr_upgr_btn_3_year})
    void onClickToDo() {
        new ToDo().show(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upgrade, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
