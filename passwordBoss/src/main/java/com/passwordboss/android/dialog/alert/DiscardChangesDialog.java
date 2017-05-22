package com.passwordboss.android.dialog.alert;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.passwordboss.android.R;

public class DiscardChangesDialog extends Dialog {

    public DiscardChangesDialog(Context context) {
        super(context);
    }

    public void show(OnClickListener onClickDiscard) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.CloseRightPanelTitle)
                .setMessage(R.string.DiscardChangesBody)
                .setPositiveButton(R.string.Discard, (dialog, which) -> {
                    if (onClickDiscard == null) return;
                    onClickDiscard.onClick();
                })
                .setNegativeButton(R.string.KeepEditing, null)
                .show();

    }

}
