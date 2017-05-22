package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.fragment.UpgradeFragment;

public class UpgradeActivity extends WrappedFragmentActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, UpgradeActivity.class));
    }

    @Nullable
    @Override
    protected Fragment createFragment() {
        return new UpgradeFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.UpgradeToPremium);
        displayHomeAsUp();
    }
}
