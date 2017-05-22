package com.passwordboss.android.logback;


import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiError {
    private final Exception mException;

    public ApiError(Exception exception) {
        mException = exception;
    }

    public void log(@NonNull Class cls) {
        String methodName = mException.getStackTrace()[0].getMethodName();
        Logger logger = LoggerFactory.getLogger(cls);
        logger.error("API: " + methodName, mException);
    }
}
