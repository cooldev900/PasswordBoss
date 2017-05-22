package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.event.ItemTypeAddEvent;
import com.passwordboss.android.model.ItemType;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonaInfoSplashFragment extends BaseFragment {
    @OnClick({R.id.fr_pisp_button_add, R.id.fr_pisp_content})
    void onClickButtonAdd() {
        EventBus.getDefault().post(new ItemTypeAddEvent(ItemType.PersonalInfo));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_info_splash, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
