package com.passwordboss.android.helper;

import android.content.Context;
import android.widget.Toast;

public class ToDo {
    public void show(Context context) {
        show(context, "To be continued ...");
    }

    public void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
