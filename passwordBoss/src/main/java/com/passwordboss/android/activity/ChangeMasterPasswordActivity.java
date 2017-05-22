package com.passwordboss.android.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.passwordboss.android.R;
import com.passwordboss.android.event.MasterPasswordChangedEvent;
import com.passwordboss.android.fragment.ChangeMasterPasswordFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChangeMasterPasswordActivity extends AutoLockActivity {

    private boolean handleBackPress() {
        ChangeMasterPasswordFragment fragment = (ChangeMasterPasswordFragment) getSupportFragmentManager()
                .findFragmentByTag(ChangeMasterPasswordFragment.TAG);
        return null != fragment && fragment.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        boolean isHandled = handleBackPress();
        if (!isHandled) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_master_password);
        setTitle("");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ac_chps_fragment_container,
                            new ChangeMasterPasswordFragment(),
                            ChangeMasterPasswordFragment.TAG)
                    .commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(MasterPasswordChangedEvent event) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (handleBackPress()) return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
