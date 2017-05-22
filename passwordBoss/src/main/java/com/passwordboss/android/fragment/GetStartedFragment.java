package com.passwordboss.android.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.activity.GetStartedActivity;

import org.greenrobot.eventbus.EventBus;

public class GetStartedFragment extends BaseFragment {

    private static final String KEY_LAYOUT = "keyLayout";

    public static GetStartedFragment newInstance(@LayoutRes int layout) {
        GetStartedFragment fragment = new GetStartedFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, layout);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getArguments().getInt(KEY_LAYOUT), null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GetStartedActivity.ShowNextGetStartedPageEvent());
            }
        });
        return view;
    }

}
