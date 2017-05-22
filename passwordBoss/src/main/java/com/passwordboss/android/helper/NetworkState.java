package com.passwordboss.android.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.passwordboss.android.R;

public class NetworkState {
    private final Context mContext;

    public NetworkState(Context context) {
        mContext = context;
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean isDisconnected() {
        return !isConnected();
    }

    public void throwNoConnectionExceptionIfNeeded() throws NoConnectionException {
        if (isDisconnected()) throw new NoConnectionException(mContext);
    }

    public static class NoConnectionException extends Exception {
        public NoConnectionException(Context context) {
            super(context.getString(R.string.NoInternetConnection));
        }
    }
}
