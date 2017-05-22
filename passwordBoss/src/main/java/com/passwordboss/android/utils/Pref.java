package com.passwordboss.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;

import com.passwordboss.android.app.App;
import com.passwordboss.android.constants.Constants;


public class Pref {

    public static final String LAST_SYNC_TIME = "last_sync_time";
    public static final String KEY_VIEW_MODE = "view_mode";
    public static String DATABASE_KEY = ""; // TODO: 3/25/2016 candidate to refactor in proper way
    public static String EMAIL = "UNKNOWN"; // TODO: 5/23/2016 review its usage and refactor it proper way (it applies on path, do search)
    public static String DEVICE_UUID = "";
    public static boolean SYNC_DEVICE = false;
    public static String INSTALLATION_UUID = ""; // TODO: 3/17/2016 use OOP approach
    public static String DAILY_ANALYTICS_UPDATE_DATE = "daily_analytics";
    public static String SEGMENT_FLUSH = "segment_flush";
    public static String MESSAGE_NUMBER = "message_number";
    private static SharedPreferences sInstance;

    public static String generatePassword(String email, String pass) {
        if (email == null) {
            email = "";
        }
        return Utils.getMD5(email.toLowerCase().trim() + pass.trim());
    }

    public static SharedPreferences get() {
        return get(App.get());
    }

    public static SharedPreferences get(Context context) {
        if (null == sInstance) {
            sInstance = context.getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
        }
        return sInstance;
    }

    public static String getAppVersion(Context mContext) {
        String versionName = null;
        try {
            versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getMessageNumber(Context mContext) {
        return getValue(mContext, MESSAGE_NUMBER, "0");
    }

    public static String getValue(Context context, String key, String defaultValue) {
        return Pref.get(context).getString(key, defaultValue);
    }

    public static boolean getValue(Context context, String key,
                                   boolean defaultValue) {
        return Pref.get(context).getBoolean(key, defaultValue);
    }

    public static ViewMode getViewMode() {
        String name = Pref.get().getString(KEY_VIEW_MODE, Pref.ViewMode.Grid.name());
        return Pref.ViewMode.valueOf(name);
    }

    public static void setViewMode(ViewMode viewMode) {
        Pref.get().edit()
                .putString(KEY_VIEW_MODE, viewMode.name())
                .apply();
    }

    public static void incrementMessageNumber(Context mContext) {
        try {
            String number = getValue(mContext, MESSAGE_NUMBER, "0");
            int num = Integer.parseInt(number);
            num++;
            setValue(mContext, MESSAGE_NUMBER, num + "");
        } catch (Exception ignored) {
        }
    }

    public static void setValue(Context context, String key, String value) {
        Editor prefsPrivateEditor = Pref.get(context).edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.apply();
    }

    public static void setValue(Context context, String key, boolean value) {
        Editor prefsPrivateEditor = Pref.get(context).edit();
        prefsPrivateEditor.putBoolean(key, value);
        prefsPrivateEditor.apply();
    }


    public enum ViewMode {
        Grid,
        List
    }
}
