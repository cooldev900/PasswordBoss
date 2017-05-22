package com.passwordboss.android.helper;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.passwordboss.android.R;

public class SafeIntent {
    private final Context mContext;
    private final Intent mIntent;

    public SafeIntent(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
    }

    public boolean check() {
        return check(true);
    }

    public boolean check(boolean showToast) {
        if (null == mIntent.resolveActivity(mContext.getPackageManager())) {
            if (showToast) {
                Toast.makeText(mContext, R.string.ErrorNoApplicationAvailable, Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }
}

