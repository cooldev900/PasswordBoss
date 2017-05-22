package com.passwordboss.android.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.SyncProgressEvent;
import com.passwordboss.android.event.SyncResultEvent;
import com.passwordboss.android.task.SyncTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpdateActivity extends EventBusActivity {
    private final static Object LOCK = new Object();
    private static SyncTask sTask;
    @Bind(R.id.ac_up_label)
    TextView mLabelView;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        startSyncTask();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SyncProgressEvent event) {
        if (event.hasError()) return;
        if (event.Progress > 50) mLabelView.setText(R.string.DecryptingData);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SyncResultEvent event) {
        synchronized (LOCK) {
            sTask = null;
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                new ErrorDialog(this).show(event.getError(), (dialog, which) -> {
                    finish();
                });
            }
            MainActivity.start(this);
        }
    }

    private void startSyncTask() {
        synchronized (LOCK) {
            if (null != sTask) return;
            sTask = new SyncTask(getApplicationContext());
            sTask.start();
        }
    }
}
