package com.passwordboss.android.helper;

import android.support.annotation.NonNull;
import android.view.View;

public class UniqueViewId {
    public int get(@NonNull View view) {
        int id = view.getId();
        if (id == View.NO_ID) {
            id = View.generateViewId();
            view.setId(id);
        }
        return id;
    }
}
