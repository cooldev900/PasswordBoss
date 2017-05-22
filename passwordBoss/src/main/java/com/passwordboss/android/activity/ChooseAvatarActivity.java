package com.passwordboss.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.event.AvatarChosenEvent;
import com.passwordboss.android.fragment.ChooseAvatarFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChooseAvatarActivity extends WrappedFragmentActivity {

    @Nullable
    @Override
    protected Fragment createFragment() {
        return new ChooseAvatarFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.IdentitiesChoosePicture);
        displayHomeAsUp();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(AvatarChosenEvent event) {
        finish();
    }
}
