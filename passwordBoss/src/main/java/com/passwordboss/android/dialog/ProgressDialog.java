package com.passwordboss.android.dialog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.passwordboss.android.R;
import com.passwordboss.android.activity.BaseActivity;
import com.passwordboss.android.fragment.BaseFragment;

public class ProgressDialog {
    private static final String TAG_DIALOG_PROGRESS = "tagDialogProgress";
    private final FragmentManager mFragmentManager;
    private final Context mContext;

    public ProgressDialog(BaseActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
        mContext = activity;
    }

    public ProgressDialog(BaseFragment fragment) {
        mFragmentManager = fragment.getActivity().getSupportFragmentManager();
        mContext = fragment.getContext();
    }

    public void dismiss() {
        ProgressDialogFragment fragment = (ProgressDialogFragment) mFragmentManager.findFragmentByTag(TAG_DIALOG_PROGRESS);
        if (null == fragment) return;
        fragment.dismissAllowingStateLoss();
    }

    public void show(CharSequence message) {
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG_DIALOG_PROGRESS);
        if (null != fragment && fragment.isVisible()) return;
        ProgressDialogFragment dialogFragment = ProgressDialogFragment.newInstance(message.toString());
        dialogFragment.showAllowingStateLoss(mFragmentManager, TAG_DIALOG_PROGRESS);
    }

    public void showLoading() {
        show(mContext.getText(R.string.Loading));
    }
}
