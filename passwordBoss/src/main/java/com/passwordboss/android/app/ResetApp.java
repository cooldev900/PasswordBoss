package com.passwordboss.android.app;

import android.content.Context;
import android.content.Intent;

import com.passwordboss.android.activity.SignInActivity;
import com.passwordboss.android.utils.Utils;

public class ResetApp {
    public void execute(Context context) {
        Utils.clearData(context);
        Intent intent = new Intent(context, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
