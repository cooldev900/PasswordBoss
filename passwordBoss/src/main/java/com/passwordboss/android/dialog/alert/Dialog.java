package com.passwordboss.android.dialog.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

import com.passwordboss.android.R;

class Dialog {
    private static final Handler HANDLER = new Handler();
    private final Context mContext;

    protected Dialog(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    protected void show(final CharSequence title, final CharSequence message, final DialogInterface.OnClickListener listener) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showImpl(title, message, listener);
            return;
        }
        HANDLER.post(() -> showImpl(title, message, listener));
    }

    private void showImpl(CharSequence title, CharSequence message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (null != title) builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton(R.string.Close, listener)
                .setCancelable(false)
                .show();
    }

    public interface OnClickListener{
        void onClick();

    }

}
