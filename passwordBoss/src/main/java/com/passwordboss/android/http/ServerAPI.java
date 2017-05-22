package com.passwordboss.android.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.passwordboss.android.Url;
import com.passwordboss.android.analytics.DailyAnalyticsHelper;
import com.passwordboss.android.app.App;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.constants.HttpConstants;
import com.passwordboss.android.http.beans.AccountPatchHttpBean;
import com.passwordboss.android.http.beans.AccountPostHttpBean;
import com.passwordboss.android.http.beans.AuthenticationHttpBean;
import com.passwordboss.android.http.beans.CountryDataHttpBean;
import com.passwordboss.android.http.beans.DeletedProfilesHttpBean;
import com.passwordboss.android.http.beans.DevicePostHttpBean;
import com.passwordboss.android.http.beans.LinksHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBeanTwoStep;
import com.passwordboss.android.http.beans.ShareHttpPost;
import com.passwordboss.android.http.beans.StorageRegionHttpPost;
import com.passwordboss.android.http.beans.TwoStepVerificationHttpBean;
import com.passwordboss.android.jsonbean.DateTimeTypeConverter;
import com.passwordboss.android.logback.ApiError;
import com.passwordboss.android.utils.Pref;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;

import java.util.HashMap;

public class ServerAPI {


    private static LinksHttpBean sLinks; // candidate for better approach, when API will be refactored
    private static String sSecureFile;

    public ServerResponseHttpBean accountAuthentication(
            AuthenticationHttpBean auth) {
        Http http = new Http();
        String response;
        try {
            response = http.runPostMethod(
                    getLinks().getAccount(), getResponseHeaders(false),
                    auth.toString());

            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean accountEmail(String email) {
        Http http = new Http();
        try {
            String response;
            JSONObject header = new JSONObject();
            header.put(HttpConstants.AUTHORIZATION, email);
            response = http.runPostMethod(
                    getLinks().getAccount(), null,
                    header.toString());

            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean accountPatch(AccountPatchHttpBean accountPatch) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            response = http.runPatchMethod(
                    getLinks().getAccount(), getResponseHeaders(true),
                    gson.toJson(accountPatch));
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    @SuppressWarnings("static-access")
    public ServerResponseHttpBean dailyAnalyticsPost(
            DailyAnalyticsHelper dailyAnalyticsHelper) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            JsonObject jsonStats = new JsonObject();
            jsonStats.addProperty("security-score",
                    dailyAnalyticsHelper.getTotal_pwd_score());
            jsonStats.addProperty("pv", dailyAnalyticsHelper.getPv_cnt());
            jsonStats.addProperty("dw", dailyAnalyticsHelper.getDw_cnt());
            jsonStats.addProperty("pi", dailyAnalyticsHelper.getPi_cnt());
            jsonStats.addProperty("logins",
                    dailyAnalyticsHelper.getTotal_logins());
            jsonStats.addProperty("date", new DateTime(DateTimeZone.UTC).now()
                    .toDateTimeISO().toString());
            JsonObject json = new JsonObject();
            json.addProperty("type", "user-stats");
            json.addProperty("device", Pref.DEVICE_UUID);
            json.addProperty("data", jsonStats.toString());

            response = http.runPostMethod(getLinks().getEvent(),
                    getResponseHeaders(true), json.toString());
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean deviceEndpointDelete(String[] uuids) {

        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            String request = "[";
            for (int i = 0, max = uuids.length; i < max; i++) {
                if (i == max - 1) {
                    request += "{\"uuid\": \"" + uuids[i] + "\"}";
                } else {
                    request += "{\"uuid\": \"" + uuids[i] + "\"},";
                }
            }
            request += "]";

            response = http.runDeleteMethod(
                    getLinks().getDevice(), getResponseHeaders(true), request);
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }

        return null;
    }

    public ServerResponseHttpBean deviceEndpointGet(String email) {
        Http http = new Http();
        String response;
        try {
            response = http.runGetMethod(getLinks().getDevice(),
                    getResponseHeaders(true));
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean deviceEndpointPost(DevicePostHttpBean device,
                                                     String email) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            HashMap<String, String> mHeaders = getResponseHeaders(false);
            mHeaders.put(HttpConstants.AUTHORIZATION, email);
            response = http.runPostMethod(
                    getLinks().getDevice(), mHeaders,
                    gson.toJson(device));

            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean getAccountEndpointGet() {
        Http http = new Http();
        String response;
        try {
            response = http.runGetMethod(
                    getLinks().getAccount(), getResponseHeaders(true));
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public CountryDataHttpBean getAllCountry() {
        Http http = new Http();
        String response;
        try {
            response = http.runGetMethod(
                    getLinks().getCountry(), null);
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(response,
                    CountryDataHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean getDownloadDBLink(String uri) {
        Http http = new Http();
        String response;
        try {
            response = http.runGetMethod(uri, getResponseHeaders(true));
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean getEntryPoint() {
        Http http = new Http();
        String response;
        try {
            response = http.runGetMethod(Url.API, null);

            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    private LinksHttpBean getLinks() {
        if (null == sLinks) {
            sLinks = readFromPersistentStorage();
            updateLinksFromServer();
        }
        return sLinks;
    }

    public DeletedProfilesHttpBean getPublicSync(String dateTime,
                                                 boolean seedSince) {
        Http http = new Http();
        String response;
        try {
            String link = getLinks().getSync();
            if (dateTime != null) {
                if (seedSince) {
                    link += "?seed_since=" + dateTime;
                } else {
                    link += "?since=" + dateTime;
                }
            }

            HashMap<String, String> mHeaders = getResponseHeaders(false);
            mHeaders.put(HttpConstants.AUTHORIZATION, Pref.INSTALLATION_UUID);
            response = http.runGetMethod(link, mHeaders);
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, DeletedProfilesHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    private HashMap<String, String> getResponseHeaders(boolean auth) {

        HashMap<String, String> mHeaders = new HashMap<>();
        mHeaders.put(HttpConstants.ACCEPT, HttpConstants.PASSWORDBOSS_VERSION);
        mHeaders.put(HttpConstants.ACCEPT_LANGUAGE, HttpConstants.LANGUAGE);
        if (auth) {
            mHeaders.put(HttpConstants.AUTHORIZATION, Pref.EMAIL + "|" + Pref.DEVICE_UUID);
        }

        return mHeaders;

    }

    public String getSecureFile() {
        return getLinks().getSecureFile();
    }

    public ServerResponseHttpBean getSiteEndpointGet(String url) {
        Http http = new Http();
        String response;
        try {
            response = http.runGetMethod(getLinks().getSite()
                    + "/" + url, getResponseHeaders(true));
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean getSync(String dateTime, boolean seedSince) {
        Http http = new Http();
        String response;
        try {
            String link = getLinks().getSync();
            if (dateTime != null) {
                if (seedSince) {
                    link += "?seed_since=" + dateTime;
                } else {
                    link += "?since=" + dateTime;
                }
            }
            response = http.runGetMethod(link, getResponseHeaders(true));
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            ServerResponseHttpBean serverResponseHttpBean = gson.fromJson(
                    response, ServerResponseHttpBean.class);
            try {
                JSONObject obj = new JSONObject(response);
                serverResponseHttpBean.setCon(obj.getString("configuration"));
            } catch (Exception e) {
                new ApiError(e).log(getClass());
            }
            return serverResponseHttpBean;
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean getTwoStepVerification() {
        Http http = new Http();
        String response;
        try {
            String link = getLinks().getAccount_2step();
            response = http.runGetMethod(link, getResponseHeaders(true));

            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            ServerResponseHttpBean serverResponseHttpBean = gson.fromJson(
                    response, ServerResponseHttpBean.class);
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject objAuth = new JSONObject(obj.getString("auth"));
                TwoStepVerificationHttpBean twoStepVerificationHttpBean = new TwoStepVerificationHttpBean();
                twoStepVerificationHttpBean.setMulti_factor_code(objAuth
                        .getString("multi_factor_code"));
                twoStepVerificationHttpBean
                        .setMulti_factor_one_time_code(objAuth
                                .getString("multi_factor_one_time_code"));
                twoStepVerificationHttpBean.setQr(objAuth.getString("qr"));
                serverResponseHttpBean
                        .setTwo_step_verification(twoStepVerificationHttpBean);
            } catch (Exception e) {
                new ApiError(e).log(getClass());
            }
            return serverResponseHttpBean;
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean installationDevice(AccountPostHttpBean accountPost) {
        Http http = new Http();
        String response;
        try {
            if (getLinks() != null) {
                Gson gson = new GsonBuilder().registerTypeAdapter(
                        DateTime.class, new DateTimeTypeConverter()).create();
                response = http.runPostMethod(
                        getLinks().getInstallation(), getResponseHeaders(false),
                        gson.toJson(accountPost));
                return gson.fromJson(
                        response, ServerResponseHttpBean.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    private LinksHttpBean readFromPersistentStorage() {
        Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
        String links = Pref.getValue(App.get(), Constants.APLICATION_LINKS, "");
        ServerResponseHttpBean serverResponseHttpBean = gson.fromJson(links, ServerResponseHttpBean.class);
        if (null == serverResponseHttpBean) return null;
        return serverResponseHttpBean.getLinks();
    }

    public ServerResponseHttpBean recommendedSite() {
        Http http = new Http();
        String response;
        try {
            response = http
                    .runPostMethod(getLinks().getVerification(),
                            getResponseHeaders(true), null);
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean shareEndpointPatch(ShareHttpPost shareHttpPost) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            response = http.runPatchMethod(
                    getLinks().getShare(), getResponseHeaders(true),
                    gson.toJson(shareHttpPost));

            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean shareEndpointPost(ShareHttpPost shareHttpPost) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            response = http.runPostMethod(getLinks().getShare(),
                    getResponseHeaders(true), gson.toJson(shareHttpPost));
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }

        return null;
    }

    public ServerResponseHttpBean smsPost(String phone, String countryDialCode) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            JsonObject json = new JsonObject();
            json.addProperty("number", phone);
            json.addProperty("country", countryDialCode);
            json.addProperty("language", HttpConstants.LANGUAGE);

            response = http.runPostMethod(getLinks().getSms(),
                    getResponseHeaders(true), json.toString());
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBean storageRegionEndpointPatch(
            StorageRegionHttpPost storageRegionHttpPost) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            response = http.runPatchMethod(
                    getLinks().getAccount(), getResponseHeaders(true),
                    gson.toJson(storageRegionHttpPost));

            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    public ServerResponseHttpBeanTwoStep twoStepVerificationPost(String code) {
        Http http = new Http();
        String response;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            JsonObject json = new JsonObject();
            json.addProperty("code", code);
            response = http.runPostMethod(
                    getLinks().getAccount_2step(), getResponseHeaders(true),
                    json.toString());
            return gson
                    .fromJson(response, ServerResponseHttpBeanTwoStep.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

    private void updateLinksFromServer() {
        ServerResponseHttpBean responseHttpBean = getEntryPoint();
        if (null == responseHttpBean) return;
        sLinks = responseHttpBean.getLinks();
        Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
        Pref.setValue(App.get(), Constants.APLICATION_LINKS, gson.toJson(responseHttpBean));
    }

    public ServerResponseHttpBean verificationEmail(String email) {
        Http http = new Http();
        String response;
        try {
            HashMap<String, String> mHeaders = getResponseHeaders(false);
            mHeaders.put(HttpConstants.AUTHORIZATION, email);
            response = http
                    .runPostMethod(getLinks().getVerification(),
                            mHeaders, null);

            Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class,
                    new DateTimeTypeConverter()).create();
            return gson.fromJson(
                    response, ServerResponseHttpBean.class);
        } catch (Exception e) {
            new ApiError(e).log(getClass());
        }
        return null;
    }

}
