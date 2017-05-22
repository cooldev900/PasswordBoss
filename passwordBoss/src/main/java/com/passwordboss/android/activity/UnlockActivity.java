package com.passwordboss.android.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import com.passwordboss.android.R;
import com.passwordboss.android.app.ResetApp;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.fragment.BaseFragment;
import com.passwordboss.android.fragment.UnlockPasswordFragment;
import com.passwordboss.android.fragment.UnlockPinFragment;
import com.passwordboss.android.pin.PinStore;
import com.passwordboss.android.pin.PinStoreOld;
import com.passwordboss.android.rx.ApplySchedulers;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UnlockActivity extends EventBusActivity {


    private void addFragment(BaseFragment fragment) {
        // use replace instead add to be able to replace PIN or Fingerprint fragments,
        // when secure db is locked and we need master password to unlock it
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ac_fr_fragment, fragment)
                .commit();

    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.Logout)
                .setMessage(R.string.LogoutConfirmPrompt)
                .setPositiveButton(R.string.Logout, (dialog, which) -> {
                    new ResetApp().execute(getApplicationContext());
                })
                .setNegativeButton(R.string.Cancel, null)
                .show();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        if (DatabaseHelperSecure.instanceExists()) {
            DeviceBll.getObservable()
                    .map(deviceBll -> {
                        PinStore pinStore = new PinStoreOld(getApplicationContext(), deviceBll);
                        if (pinStore.isEnabled()) {
                            return LockType.Pin;
                        } else {
                            return LockType.Password;
                        }
                    })
                    .compose(new ApplySchedulers<>())
                    .subscribe(lockType -> {
                        switch (lockType) {
                            case Pin:
                                addFragment(new UnlockPinFragment());
                                break;
                            default:
                                addFragment(new UnlockPasswordFragment());
                                DatabaseHelperSecure
                                        .getObservable()
                                        .subscribe(DatabaseHelperSecure::close);
                                break;
                        }
                    });
        } else {
            addFragment(new UnlockPasswordFragment());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnlockedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LogoutEvent event) {
        logout();
    }


    private enum LockType {
        Pin,
        Password,
    }

    public static class UnlockedEvent extends BaseEvent {
    }

    public static class LogoutEvent extends BaseEvent {
    }


}
