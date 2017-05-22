package com.passwordboss.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.event.PinResultEvent;
import com.passwordboss.android.fragment.ChangePinFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChangePinActivity extends ToolbarWrappedFragmentActivity {

    public static final String EXTRA_STEP = "extra_step";

    @Nullable
    @Override
    protected Fragment createFragment() {
        int step = ChangePinFragment.STEP_INPUT_OLD_PIN;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            step = extras.getInt(EXTRA_STEP, ChangePinFragment.STEP_INPUT_OLD_PIN);
        }
        return ChangePinFragment.newInstance(step);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(PinResultEvent e) {
        finish();
    }

}
