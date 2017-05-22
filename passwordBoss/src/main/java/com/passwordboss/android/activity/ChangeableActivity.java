package com.passwordboss.android.activity;

import android.support.v4.app.Fragment;

import com.passwordboss.android.dialog.alert.DiscardChangesDialog;
import com.passwordboss.android.fragment.Changeable;

public abstract class ChangeableActivity extends WrappedFragmentActivity {
    @Override
    public void onBackPressed() {
        if (showDiscardChangesIfNeeded()) return;
        super.onBackPressed();
    }

    @Override
    protected boolean onUpPressed() {
        return showDiscardChangesIfNeeded() || super.onUpPressed();
    }

    private boolean showDiscardChangesIfNeeded() {
        Fragment fragment = getWrappedFragment();
        if (null != fragment && fragment instanceof Changeable) {
            Changeable changeable = (Changeable) fragment;
            if (changeable.hasChanges()) {
                new DiscardChangesDialog(this).show(this::finish);
                return true;
            }
        }
        return false;
    }
}
