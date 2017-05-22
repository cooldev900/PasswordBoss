package com.passwordboss.android.helper;

import android.view.View;
import android.view.ViewGroup;

public class EnableChildren {

    public void process(View view, boolean value) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                process(child, value);
            }
        }
        view.setEnabled(value);
    }

}
