package com.passwordboss.android.dialog.alert;

import android.content.Context;
import android.content.DialogInterface;

import com.passwordboss.android.R;

public class InfoDialog extends Dialog {
    public InfoDialog(Context context) {
        super(context);
    }

    public void show(CharSequence message, DialogInterface.OnClickListener listener) {
        show(getContext().getString(R.string.Information), message, listener);
    }

    public void show(CharSequence message) {
        show(message, null);
    }

}
