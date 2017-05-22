package com.passwordboss.android.actionqueue;

import android.content.Context;

import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Action;
import com.passwordboss.android.database.bll.ActionBll;
import com.passwordboss.android.utils.Pref;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ActionQueue {

    public static HashMap<String, Action> actionMap = new HashMap<>();
    public static ArrayList<ActionItem> listAction = new ArrayList<>();

    public static void AddItemQueue(Context mContext, Action action) {
        DateTime startDate = new DateTime(action.getStartDate());
        if ((startDate.getMillis() - DateTime.now().getMillis()) > 0) {
            if (action.getActionType().equals("SYNC")) {
                actionMap.put(action.getId(), action);
                listAction.add(new ActionItemSync(mContext, action.getId()));
            }
        }
    }

    public static void addActionSync(Context mContext) {
        addActionSync(mContext, 300);
    }

    public static void addActionSync(Context mContext, DateTime now, DateTime nextMobileSync) {
        if (nextMobileSync == null) {
            addActionSync(mContext, (int) ((300000 - now.getMillis()) / 1000));
        } else {
            addActionSync(mContext, (int) ((nextMobileSync.getMillis() - now.getMillis()) / 1000));
        }
    }

    private static void addActionSync(Context mContext, int sec) {
        try {
            DatabaseHelperNonSecure mDatabseHelper = DatabaseHelperNonSecure.getHelper(mContext);
            ActionBll actionBll = new ActionBll(mDatabseHelper);
            Action action = actionBll.getActionByName("Sync");
            if (action == null) {
                String uuid = UUID.randomUUID().toString();
                action = new Action();
                action.setId(uuid);
                action.setUuid(uuid);
                action.setCreatedDate(new DateTime(DateTimeZone.UTC).toDateTimeISO().toString());
                action.setAccountEmail(Pref.EMAIL);
                action.setActionType("SYNC");
                action.setName("Sync");
                action.setActive(true);
                action.setOrder(0);
            }
            action.setLastModifiedDate(new DateTime(DateTimeZone.UTC).toDateTimeISO().toString());
            action.setStatus(DatabaseConstants.INCOMPLITED);
            DateTime dateTime = new DateTime();
            dateTime = dateTime.plusSeconds(sec);
            action.setStartDate(dateTime.toDateTime(DateTimeZone.UTC).toDateTimeISO().toString());
            actionBll.insertOrUpdateRow(action);

            ActionQueue.AddItemQueue(mContext, action);
        } catch (Exception ignored) {
        }
    }

    public static void clearActionQueue() {
        for (ActionItem ai : listAction) {
            ai.getTimer().cancel();
        }
    }

}