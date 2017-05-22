package com.passwordboss.android.logback;


import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppError {
    private final Throwable mThrowable;
    private final String mTag;

    public AppError(Throwable throwable) {
        this("Error", throwable);
    }

    public AppError(String tag, Throwable throwable) {
        mThrowable = throwable;
        mTag = tag;
    }

    public void log(@NonNull Class cls) {
        String methodName = mThrowable.getStackTrace()[0].getMethodName();
        Logger logger = LoggerFactory.getLogger(cls);
        logger.error(mTag + ": " + methodName, mThrowable);
    }
}
