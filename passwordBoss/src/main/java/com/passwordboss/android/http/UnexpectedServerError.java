package com.passwordboss.android.http;

import android.content.Context;

import com.passwordboss.android.R;
import com.passwordboss.android.http.beans.ErrorHttpBean;

public class UnexpectedServerError extends ServerException {
    public UnexpectedServerError(Context context, ErrorHttpBean error) {
        super(context.getString(R.string.ErrorUnexpectedError) + " Code: " + error.getCode() + " ID: " + error.getId());
    }
}
