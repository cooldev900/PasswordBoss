package com.passwordboss.android.app;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.beans.AccountPostHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.utils.LanguagesUtils;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

public class Installation {
    public String getUuid() {
        return Pref.get().getString(Constants.INSTALLATION_UUID, "");
    }

    public boolean isRegistered() {
        return !Strings.isNullOrEmpty(getUuid());
    }

    public boolean register(Context context) {
        ServerAPI api = new ServerAPI();
        LanguagesUtils changeLanguage = new LanguagesUtils(context);
        String shortLanguage = context.getResources().getConfiguration().locale.getLanguage();
        String language = changeLanguage.getLanguageByLetterCode(shortLanguage);
        AccountPostHttpBean accountPost = new AccountPostHttpBean();
        accountPost.setLanguage(language);
        accountPost.setDeviceCategory(Constants.MOBILE);
        accountPost.setDeviceMarketingName(Build.MODEL);
        accountPost.setDeviceType(context.getResources().getBoolean(R.bool.tablet) ? Constants.TABLET_VERSION : Constants.MOBILE_VERSION);
        accountPost.setIdentifier(Settings.Secure.ANDROID_ID);
        accountPost.setManufacturer(Build.MANUFACTURER);
        accountPost.setOs("Android " + Build.VERSION.RELEASE);
        accountPost.setResolutionHeight(Utils.getDeviceHeight(context));
        accountPost.setResolutionWidth(Utils.getDeviceWidth(context));
        accountPost.setScreenHeight(Utils.getDeviceHeight(context));
        accountPost.setScreenWidth(Utils.getDeviceWidth(context));
        accountPost.setSoftwareVersion(Pref.getAppVersion(context));
        accountPost.setTimezone("UTC" + Utils.timeZone());
        ServerResponseHttpBean serverResponseHttpBean = api.installationDevice(accountPost);
        if (serverResponseHttpBean != null &&
                serverResponseHttpBean.getError() == null &&
                serverResponseHttpBean.getInstallations() != null &&
                serverResponseHttpBean.getInstallations()[0] != null) {
            String uuid = serverResponseHttpBean.getInstallations()[0].getUuid();
            Pref.INSTALLATION_UUID = uuid;
            Pref.setValue(context, Constants.INSTALLATION_UUID, uuid);
            Pref.setValue(context, Constants.CHANNEL_ID, serverResponseHttpBean.getInstallations()[0].getChannel().getChannelId());
            AnalyticsHelperSegment.logInstallEvents(context, serverResponseHttpBean);
            return true;
        }

        return false;
    }
}
