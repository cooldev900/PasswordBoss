package com.passwordboss.android.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.event.ChangeMasterPasswordEvent;
import com.passwordboss.android.event.ChangePinEvent;
import com.passwordboss.android.event.CheckPinEvent;
import com.passwordboss.android.event.FolderManageEvent;
import com.passwordboss.android.event.ForceChangePinEvent;
import com.passwordboss.android.event.GeneratePasswordEvent;
import com.passwordboss.android.event.RegisteredDevicesEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.event.UpgradeAccountEvent;
import com.passwordboss.android.fragment.ChangePinFragment;
import com.passwordboss.android.fragment.SettingsFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SettingsActivity extends ToolbarWrappedFragmentActivity {

    @Nullable
    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TwoStepVerificationWizardNextStepEvent event) {
        startActivity(new Intent(this, TwoStepVerificationActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderManageEvent event) {
        startActivity(new Intent(this, ManagerFoldersActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SelectSettingEvent event) {
        Intent intent = new Intent(this, SettingItemListActivity.class);
        intent.putExtra(SettingItemListActivity.EXTRA_TYPE, event.getType());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeneratePasswordEvent event) {
        startActivity(new Intent(this, PasswordGeneratorActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RegisteredDevicesEvent event) {
        Intent intent = new Intent(this, MyDevicesActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpgradeAccountEvent event) {
        startActivity(new Intent(this, UpgradeActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangePinEvent event) {
        startActivity(new Intent(this, ChangePinActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ForceChangePinEvent event) {
        Intent intent = new Intent(this, ChangePinActivity.class);
        intent.putExtra(ChangePinActivity.EXTRA_STEP, ChangePinFragment.STEP_INPUT_NEW_PIN);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CheckPinEvent event) {
        Intent intent = new Intent(this, ChangePinActivity.class);
        intent.putExtra(ChangePinActivity.EXTRA_STEP, ChangePinFragment.STEP_CHECK_PIN);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeMasterPasswordEvent event) {
        startActivity(new Intent(this, ChangeMasterPasswordActivity.class));
    }
}

