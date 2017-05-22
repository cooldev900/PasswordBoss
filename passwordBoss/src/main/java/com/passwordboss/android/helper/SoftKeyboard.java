package com.passwordboss.android.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboard {
    public void hide(Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        hide(activity);
    }

    public void hide(Activity activity) {
        if (null == activity) return;
        View view = activity.getCurrentFocus();
        if (null == view) return;
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showImplicit(View view) {
        if (!view.requestFocus()) return;
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

}
