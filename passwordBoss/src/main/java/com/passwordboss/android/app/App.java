package com.passwordboss.android.app;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.instabug.library.Feature;
import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;
import com.passwordboss.android.BuildConfig;
import com.passwordboss.android.R;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.logback.AppSqlError;
import com.passwordboss.android.utils.LanguagesUtils;
import com.passwordboss.android.utils.Pref;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends MultiDexApplication {
    private static App sInstance;

    public static App get() {
        return sInstance;
    }

    private void changeAppsLanguage() {
        if (!DatabaseHelperNonSecure.exists()) return; // controller.db will be copied during splash
        try {
            DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(this);
            ConfigurationBll configurationBll = new ConfigurationBll(databaseHelperNonSecure);
            String email = Pref.getValue(this, Constants.EMAIL, null);
            String language = null;
            Configuration configuration;
            if (null != email) {
                configuration = configurationBll.getConfigurationByEmailAndKey(email, DatabaseConstants.LANGUAGE);
                if (configuration != null) {
                    language = configuration.getValue();
                }
            }
            LanguagesUtils changeLanguage = new LanguagesUtils(this);
            if (null == language) {
                String languageCode = getResources().getConfiguration().locale.getLanguage();
                language = changeLanguage.getLanguageByLetterCode(languageCode);
                if (null != email) {
                    configurationBll.updateOrInsertItem(email, DatabaseConstants.LANGUAGE, language);
                }
            }
            changeLanguage.changeLanguage(language);
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void initCrashlytics() {
        Crashlytics kit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG)
                        .build())
                .build();
        Fabric.with(this, kit);
    }

    private void initInstabug() {
        new Instabug.Builder(this, "57b988223c681f19ecde13e9a5ed6df6")
                .setPushNotificationState(Feature.State.DISABLED)
                .setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
                .setShouldShowIntroDialog(false)
                .setCrashReportingState(Feature.State.DISABLED)
                .build();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeAppsLanguage();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initCrashlytics();
        initInstabug();
        initCalligraphy();
        SQLiteDatabase.loadLibs(this);
        changeAppsLanguage();
        Pref.INSTALLATION_UUID = Pref.getValue(this, Constants.INSTALLATION_UUID, "");
    }
}


