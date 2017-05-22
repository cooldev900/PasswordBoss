package com.passwordboss.android.actionqueue;

import android.content.Context;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Action;
import com.passwordboss.android.database.bll.ActionBll;
import com.passwordboss.android.task.SyncTask;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

import org.joda.time.DateTime;

import java.util.Timer;
import java.util.TimerTask;

public class ActionItemSync implements ActionItem {
    private Action mAction;
    private Context mContext;
    private Timer timer;

    public ActionItemSync(Context mContext, String id) {
        try {
            this.mContext = mContext;
            this.timer = null;
            DatabaseHelperNonSecure dbNonSecure = DatabaseHelperNonSecure.getHelper(mContext);
            ActionBll actionBll = new ActionBll(dbNonSecure);
            mAction = actionBll.getActionById(id);
            doTask();
        } catch (Exception e) {
        }
    }

    @Override
    public void doTask() {
        if (mAction != null && timer == null) {
            timer = new Timer();
            DateTime actionDateTime = new DateTime(mAction.getStartDate());
            long when = actionDateTime.getMillis() - DateTime.now().getMillis();
            if (when > 0) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (Utils.isOnline(mContext)) {
                                if (!Pref.SYNC_DEVICE) {
                                    Pref.SYNC_DEVICE = true;
                                    new SyncTask(mContext).start();
                                }
                            }
                        } catch (Exception e) {
                            ActionQueue.addActionSync(mContext);
                        }
                    }
                }, when);
            }
        }

    }

    @Override
    public Timer getTimer() {
        return timer;

    }
}
