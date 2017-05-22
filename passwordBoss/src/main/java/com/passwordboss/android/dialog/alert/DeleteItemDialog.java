package com.passwordboss.android.dialog.alert;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.passwordboss.android.R;

public class DeleteItemDialog extends Dialog {

    public DeleteItemDialog(Context context) {
        super(context);
    }


    public void show(OnClickListener onClickDelete) {
        show(onClickDelete, null);
    }

    public void show(OnClickListener onClickDelete, OnClickListener onClickCancel) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.DeleteSecureItemHeader)
                .setMessage(R.string.DeleteSecureItemBody)
                .setPositiveButton(R.string.Delete, (dialog, which) -> {
                    if (null == onClickDelete) return;
                    onClickDelete.onClick();
                })
                .setNegativeButton(R.string.Cancel, (dialog1, which1) -> {
                    if (null == onClickCancel) return;
                    onClickCancel.onClick();
                })
                .setOnCancelListener(listener -> {
                    if (null == onClickCancel) return;
                    onClickCancel.onClick();
                })
                .show();

    }

}
