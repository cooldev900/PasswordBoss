package com.passwordboss.android.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class ProgressDialogFragment extends BaseDialogFragment {

    private static final String KEY_MESSAGE = "keyMessage";

    public static ProgressDialogFragment newInstance(String message) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_MESSAGE, message);
        fragment.setArguments(arguments);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getArguments().getString(KEY_MESSAGE));
        return dialog;
    }


}
