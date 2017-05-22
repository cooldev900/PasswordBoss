package com.passwordboss.android.dialog;

import android.app.Activity;
import android.support.v4.app.DialogFragmentExt;

import com.passwordboss.android.activity.BaseActivity;


abstract class BaseDialogFragment extends DialogFragmentExt {
    protected void checkActivityImplementation(Class<?> cls) throws ClassCastException {
        checkActivityImplementation(cls, "Activity must implement ");
    }

    protected void checkActivityImplementation(Class<?> cls, String message) {
        if (null == getActivity() || !cls.isInstance(getActivity())) {
            throw new ClassCastException(message + cls.getSimpleName() + ".");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        checkActivityImplementation(BaseActivity.class);
    }

}
