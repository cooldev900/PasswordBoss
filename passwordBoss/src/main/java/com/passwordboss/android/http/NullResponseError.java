package com.passwordboss.android.http;

import android.content.Context;

import com.passwordboss.android.R;

public class NullResponseError extends ServerException {
    public NullResponseError(Context context) {
        super(context.getString(R.string.ErrorUnexpectedError) + " NULL");
    }
}
