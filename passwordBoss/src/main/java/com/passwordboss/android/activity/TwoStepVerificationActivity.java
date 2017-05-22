package com.passwordboss.android.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.event.TwoStepVerificationWizardFinishEvent;
import com.passwordboss.android.fragment.TwoStepVerificationFragment;

import org.greenrobot.eventbus.Subscribe;

public class TwoStepVerificationActivity extends ToolbarWrappedFragmentActivity {

    @Nullable
    @Override
    protected Fragment createFragment() {
        return new TwoStepVerificationFragment();
    }

    @Subscribe
    public void onEvent(TwoStepVerificationWizardFinishEvent event) {
        finish();
    }
}
