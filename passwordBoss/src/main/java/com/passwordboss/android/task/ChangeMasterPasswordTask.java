package com.passwordboss.android.task;

import android.content.Context;

import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.event.MasterPasswordTaskResultEvent;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;

public class ChangeMasterPasswordTask extends Thread {
    private final Context mContext;
    private final String mNewPassword;
    private final String mOldPassword;

    public ChangeMasterPasswordTask(Context context, String newPassword, String oldPassword) {
        mContext = context;
        mNewPassword = newPassword;
        mOldPassword = oldPassword;
    }

    @Override
    public void run() {
        try {
            runImpl();
            EventBus.getDefault().postSticky(new MasterPasswordTaskResultEvent());
        } catch (Exception e) {
            EventBus.getDefault().postSticky(new MasterPasswordTaskResultEvent(e));
        }
    }

    private void runImpl() throws Exception {
        DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
        if (null == helperSecure) throw new IllegalStateException("Database is null");
        helperSecure.changePassword(mOldPassword, mNewPassword);
        Pref.DATABASE_KEY = Pref.generatePassword(Pref.EMAIL, mNewPassword);
        ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext));
        configurationBll.updateOrInsertItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_CHANGE, "1");
        new SyncTask(mContext).blockingRun();
    }
}
