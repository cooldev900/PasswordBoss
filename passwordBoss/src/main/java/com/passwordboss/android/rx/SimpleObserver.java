package com.passwordboss.android.rx;

import android.content.Context;

public class SimpleObserver<T> extends BaseObserver<T> {
    public SimpleObserver(Context context) {
        super(context);
    }

    @Override
    public void onNext(T o) {
    }
}
