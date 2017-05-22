package com.passwordboss.android.dialog.alert;

import android.content.Context;
import android.content.DialogInterface;

import com.passwordboss.android.R;

public class ErrorDialog extends Dialog {

    public ErrorDialog(Context context) {
        super(context);
    }

    public void show(final CharSequence message, final DialogInterface.OnClickListener listener) {
        show(getContext().getText(R.string.MessageBoxErrorTitle), message, listener);
    }

    public void show(Throwable throwable) {
        show(throwable.getMessage(), null);
    }

    public void show(Throwable throwable, DialogInterface.OnClickListener listener) {
        show(throwable.getMessage(), listener);
    }
}
