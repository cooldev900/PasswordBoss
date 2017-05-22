package com.passwordboss.android.activity;

import android.os.Bundle;

import com.passwordboss.android.R;
import com.passwordboss.android.event.ChooseIconColorResultEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChooseIconColorActivity extends AutoLockActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_icon_color);
        setTitle(R.string.ChooseIconColor);
        displayHomeAsUp();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(ChooseIconColorResultEvent event) {
        finish();
    }
}
