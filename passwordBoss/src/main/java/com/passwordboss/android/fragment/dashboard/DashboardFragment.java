package com.passwordboss.android.fragment.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

abstract public class DashboardFragment extends BaseFragment {
    private View.OnClickListener mOnClickListener = v -> {
        Action[] values = Action.values();
        for (Action action : values) {
            if (action.mId == v.getId()) {
                EventBus.getDefault().post(new DashboardEvent(action));
                return;
            }
        }
    };

    abstract protected int getLayoutId();

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                EventBus.getDefault().post(new DashboardEvent(Action.Notifications));
                return true;
            case R.id.menu_notifications:
                EventBus.getDefault().post(new DashboardEvent(Action.Notifications));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Action[] values = Action.values();
        for (Action value : values) {
            View viewById = view.findViewById(value.mId);
            if (null == viewById) continue;
            viewById.setOnClickListener(mOnClickListener);
        }
    }

    public enum Action {
        DigitalWallet(R.id.fr_ds_button_digital_wallet),
        Emergency(R.id.fr_ds_button_emergency),
        Identities(R.id.fr_ds_button_identities),
        Lock(R.id.fr_ds_button_lock),
        Notifications(0),
        Passwords(R.id.fr_ds_button_passwords),
        PersonalInfo(R.id.fr_ds_button_personal_info),
        Search(0),
        SecureBrowser(R.id.fr_ds_button_browser),
        SecureNotes(R.id.fr_ds_button_secure_notes),
        Settings(R.id.fr_ds_button_settings),
        ShareCenter(R.id.fr_ds_button_share_center);

        private final int mId;

        Action(@IdRes int id) {
            mId = id;
        }
    }


    public static class DashboardEvent extends BaseEvent {
        public final Action Action;

        public DashboardEvent(DashboardFragment.Action action) {
            Action = action;
        }
    }


}
