package com.passwordboss.android.rx;

import android.content.Context;

import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.logback.AppError;

import rx.Observer;

public abstract class BaseObserver<T> implements Observer<T> {

    private Context mContext;

    public BaseObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        new AppError(e).log(getClass());
        new ErrorDialog(mContext).show(e);
    }

    @Override
    public abstract void onNext(T t);
}
