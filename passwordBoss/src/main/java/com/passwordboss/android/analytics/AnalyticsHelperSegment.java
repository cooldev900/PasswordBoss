package com.passwordboss.android.analytics;

import android.content.Context;

import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.UserSubscription;
import com.passwordboss.android.database.bll.UserSubscriptionBll;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;
import com.segment.analytics.Analytics;
import com.segment.analytics.Options;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.TreeMap;

public class AnalyticsHelperSegment {

    public static String MESSAGE_TEMPLATE = "Message Template";
    public static String MESSAGE_TEMPLATE_VALUE = "Buy Screen v1";
    public static String BUY_BUTTON_NOV = "Bottom Nav";
    public static String BUY_BUTTON_LEFT_NAV = "Left Nav";
    public static String BUY_BUTTON_TOP_NAV = "Top Nav";
    public static String BUY_BUTTON_SETTINGS_PROFILE = "Settings-Profile";
    public static String BUY_BUTTON_SETUP_WIZARD = "Setup Wizard";
    public static String CAMPAING_NAME = "Campaign Name";
    public static String STEP_NAME_LANGUAGE_SELECTION = "Language selection";
    public static String STEP_NAME_ENTER_EMAIL = "Enter email";
    public static String STEP_NAME_CONFIRM_MP = "Confirm MP";
    public static String SW_SCREEN_NAME_GET_STARTED = "Get Started";
    public static String SW_SCREEN_NAME_PERSONAL_INFO = "Personal Info";
    public static String SW_SCREEN_NAME_ADD_TO_MOBILE = "Add to mobile";
    public static String SW_SCREEN_NAME_DIGITAL_WALLET = "Digital Wallet";
    public static String SW_SCREEN_NAME_TRY_ONE_CLICK_LOGIN = "Try One-Click Login";
    public static String SW_SCREEN_NAME_GET_PREMIUM = "Get Premium";
    public static String BUTTON_CLICKED_VALUE_COUNTINUE = "Continue";
    public static String BUTTON_CLICKED_VALUE_CLOSE = "Close";
    public static String BUTTON_CLICKED_VALUE_CANCELED = "Canceled";
    public static String BUTTON_CLICKED_VALUE_ONE_YEAR = "1 Year";
    public static String BUTTON_CLICKED_VALUE_TWO_YEAR = "2 Years";
    public static String BUTTON_CLICKED_VALUE_THREE_YEAR = "3 Years";
    public static String ACTION_ADDED = "Added";
    public static String ACTION_DELETED = "Deleted";
    public static String ACTION_SHARED = "Shared";
    public static String ACTION_ACCEPTED = "Accepted";
    public static String ACTION_REJECTED = "Rejected";
    public static String ACTION_EXPIRED = "Expired";
    public static String ACTION_CANCELED = "Canceled";
    public static String TYPE = "Type";
    public static String TYPE_NAME = "Name";
    public static String TYPE_ADDRESS = "Address";
    public static String TYPE_PHONE_NUMBER = "Phone Number";
    public static String TYPE_EMAIL = "Email";
    public static String TYPE_DRIVER_LICENSE = "Drivers License";
    public static String TYPE_MEMBER_ID = "Member ID";
    public static String TYPE_SOCIAL_SECURITY = "Social Security";
    public static String TYPE_PASSPORT = "Passport";
    public static String TYPE_COMPANY = "Company";
    public static String TYPE_SECURE_NOTE = "Secure Note";
    public static String TYPE_BANK_ACCUNT = "Bank Account";
    public static String TYPE_CREDIT_CARD = "Credit Card";
    public static String TYPE_PASSWORD = "Password";
    public static String SOURCE_MAIN_UI = "Main UI";
    public static String SOURCE_BROWSER_EXTENSION = "Browser Extension";
    public static String SOURCE_PASSWORD_VAULT = "Password Vault";
    public static String STATUS_ENABLE = "Enabled";
    public static String STATUS_DISABLED = "Disabled";
    public static String STATUS_CANCEL = "Cancel";
    public static String STATUS_COMPLETED = "Completed";
    public static String STATUS_SKIPPED = "Skipped";
    public static String STATUS_GET_PREMIUM = "Get Premium";
    // Install
    private static String INSTALL = "Install";
    private static String PLATFOR = "Platform";
    private static String PLATFOR_VALUE = "Android";
    private static String DEVICE_CATEGORY = "Device Category";
    private static String DEVICE_TYPE = "Device Type";
    private static String CHANNEL_ID = "Channel ID";
    private static String CHANNEL_ID_VALUE = "10000";
    private static String PRODUCTION_VERSION = "Product Version";
    private static String OS_LANGUAGE = "OS Language";
    // In-App Marketing
    private static String IN_APP_MARKETING = "In-App Marketing";
    private static String MESSAGE = "Message #";
    private static String MESSAGE_TYPE = "Message Type";
    private static String BUY_BUTTON = "Buy Button";
    private static String DAYS_SINCE_ACCOUNT_CREATED = "Days Since Account Created";
    //Free To Paid
    private static String FREE_TO_PAID = "Free to Paid";
    private static String IN_APP_MESSAGE_SHOWN = "In-app messages shown";
    private static String SUBSCIPTION_TYPE = "Subscription Type";
    private static String SUBSCIPTION_LEVEL = "Subscription Level";
    private static String SUBSCIPTION_PROVIDER = "Subscription Provider";
    private static String SUBSCIPTION_SOURCE = "Subscription Source";
    private static String SUBSCIPTION_MEDIUM = "Medium";
    private static String CAMPAIGN = "Campaign";
    // Account Creation Flow
    private static String ACCOUNT_CREATION_FLOW = "Account Creation Flow";
    private static String STEP = "Step #";
    private static String STEP_NAME = "Step Name";
    private static String LANGUAGE_SELECTED = "Language Selected";
    // Account Created
    private static String ACCOUNT_CREATED = "Account Created";
    // Setup Wizard
    private static String SETUP_WIZARD = "Setup Wizard";
    private static String SW_WIZARD_VERSION = "Wizard Version";
    private static String SCREEN = "Screen #";
    private static String SCREEN_NAME = "Screen Name";
    // PRODUCT_TOUR
    private static String PRODUCT_TOUR = "Product Tour";
    // Password Vault
    private static String PASSWORD_VAULT = "Password Vault";
    // Digital Wallet
    private static String DIGITAL_WALLET = "Digital Wallet";
    // Personal Info
    private static String PERSONAL_INFO = "Personal Info";
    // Sharing
    private static String SHARING = "Sharing";
    // Password Generator
    private static String PASSWORD_GENERATOR = "Password Generator";
    private static String CREATE_PASSWORD = "Create Password";
    // In-App Browser
    private static String IN_APP_BROWSER = "In-App Browser";
    private static String OPEN_SITE_FROM_PV = "Opened Site from PV";
    // 2-Factor Verification
    private static String TWO_FACTOR_VERIFICATION = "2-Factor Verification";
    //Analytics
    private static String ANALYTICS = "Analytics";
    //Analytics
    private static String ACTIVE_USER = "Active User";
    private static String LOGIN_TODAY = "Login Today";
    //Online Checkout
    private static String ONLINE_CHECKOUT = "Online Checkout";
    private static String BROWSER = "Browser";
    private static String BROWSER_VERSION = "In-App Browser";
    // COMMON
    private static String BUTTON_CLICKED = "Button Clicked";
    private static String ACTION = "Action";
    private static String SOURCE = "Source";
    private static String STATUS = "Status";

    public static boolean checkTime(Context mContext, String item) {
        String storedDate = "";
        boolean sendData = false;
        if (Pref.SEGMENT_FLUSH.equals(item)) {
            storedDate = Pref.getValue(mContext, Pref.SEGMENT_FLUSH, "");
        } else if (Constants.ACTIVE_USER_ANALYTIC.equals(item)) {
            storedDate = Pref.getValue(mContext, Constants.ACTIVE_USER_ANALYTIC, "");
        }

        if (storedDate.length() > 0) {
            DateTime d1 = DateTime.now();
            DateTime d2 = DateTime.parse(storedDate, DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"));
            long diff = d1.getMillis() - d2.getMillis();
            if (diff > diff) { // // TODO: 3/4/2016 figure out what would be implemented there
                sendData = true;
            }
        } else {
            sendData = true;
        }
        return sendData;
    }

    public static void logAccountCreated(Context mContext) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            UserSubscriptionBll mUserSubscriptionBll = new UserSubscriptionBll(mDatabaseHelperSecure);
            UserSubscription mSubscription = mUserSubscriptionBll.getUserInfoByEmail(Pref.EMAIL);

            if (mSubscription != null) {
                events.put(SUBSCIPTION_TYPE, mSubscription.getName());
            }
            logCustomEvents(mContext, ACCOUNT_CREATED, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logAccountCreationFlow(Context mContext, int step, String stepName, String language,
                                              String button) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(STEP, STEP + step);
            events.put(STEP_NAME, stepName);
            if (language != null) {
                events.put(LANGUAGE_SELECTED, language);
            }
            events.put(BUTTON_CLICKED, button);
            logCustomEvents(mContext, ACCOUNT_CREATION_FLOW, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logActiveUser(Context mContext) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(LOGIN_TODAY, "Yes");
            if (checkTime(mContext, Constants.ACTIVE_USER_ANALYTIC)) {
                logCustomEvents(mContext, ACTIVE_USER, events);
                Pref.setValue(mContext, Constants.ACTIVE_USER_ANALYTIC, DateTime.now().toString("MM/dd/yyyy HH:mm:ss"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logAnalytics(Context mContext, String securityScore, String duplicatePass, String weakPass,
                                    String oldPass, String total) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put("1", securityScore);
            events.put("2", duplicatePass);
            events.put("3", weakPass);
            events.put("4", oldPass);
            events.put("5", total);
            logCustomEvents(mContext, ANALYTICS, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logCustomEvents(Context mContext, String event, TreeMap<String, String> events) {
        Properties prop = new Properties();
        prop.putAll(events);
        Analytics.with(mContext).track(event, prop);
        Analytics.with(mContext).flush();

    }

    public static void logDigitalWallet(Context mContext, String action, String type) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(ACTION, action);
            events.put(TYPE, type);
            logCustomEvents(mContext, DIGITAL_WALLET, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logFreeToPaid(Context mContext, String subsciptionType, String subsciptionLevel, String subsciptionProvider, String subscriptionSource, String subscriptionMedium, String campaign) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            UserSubscriptionBll mUserSubscriptionBll = new UserSubscriptionBll(mDatabaseHelperSecure);
            UserSubscription mSubscription = mUserSubscriptionBll.getUserInfoByEmail(Pref.EMAIL);
            int days = Utils.calculateNumberOfDaySinceAccountCreated(new DateTime(mSubscription.getCreatedDate(), DateTimeZone.UTC));
            events.put(SUBSCIPTION_TYPE, mSubscription.getName());
            events.put(DAYS_SINCE_ACCOUNT_CREATED, days + "");
            int messageNumber = Integer.parseInt(Pref.getMessageNumber(mContext));
            if (messageNumber > 0) {
                messageNumber--;
            }
            events.put(IN_APP_MESSAGE_SHOWN, messageNumber + "");
            events.put(MESSAGE, MESSAGE + Pref.getMessageNumber(mContext));
            events.put(SUBSCIPTION_TYPE, subsciptionType);
            events.put(SUBSCIPTION_LEVEL, subsciptionLevel);
            events.put(SUBSCIPTION_PROVIDER, subsciptionProvider);
            events.put(SUBSCIPTION_SOURCE, subscriptionSource);
            events.put(SUBSCIPTION_MEDIUM, subscriptionMedium);
            events.put(CAMPAIGN, campaign);
            logCustomEvents(mContext, FREE_TO_PAID, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logInAppBrowser(Context mContext) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(OPEN_SITE_FROM_PV, "True");
            logCustomEvents(mContext, IN_APP_BROWSER, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logInAppMarketing(Context mContext, String button, String buyButton) {
        try {
            DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            UserSubscriptionBll mUserSubscriptionBll = new UserSubscriptionBll(mDatabaseHelperSecure);
            UserSubscription mSubscription = mUserSubscriptionBll.getUserInfoByEmail(Pref.EMAIL);
            int days = Utils.calculateNumberOfDaySinceAccountCreated(new DateTime(mSubscription.getCreatedDate(), DateTimeZone.UTC));
            Pref.incrementMessageNumber(mContext);
            TreeMap<String, String> events = new TreeMap<>();
            events.put(MESSAGE, MESSAGE + Pref.getMessageNumber(mContext));
            events.put(BUTTON_CLICKED, button);
            if (BUTTON_CLICKED_VALUE_ONE_YEAR.equalsIgnoreCase(button) ||
                    BUTTON_CLICKED_VALUE_TWO_YEAR.equalsIgnoreCase(button) ||
                    BUTTON_CLICKED_VALUE_THREE_YEAR.equalsIgnoreCase(button) ||
                    BUTTON_CLICKED_VALUE_CANCELED.equalsIgnoreCase(button)) {
                events.put(MESSAGE_TEMPLATE, MESSAGE_TEMPLATE_VALUE);
            }
            if (!"".equals(buyButton)) {
                events.put(BUY_BUTTON, buyButton);
            }
            events.put(DAYS_SINCE_ACCOUNT_CREATED, days + "");
            logCustomEvents(mContext, IN_APP_MARKETING, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logInstallEvents(Context mContext, ServerResponseHttpBean response) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(PLATFOR, PLATFOR_VALUE);
            events.put(PRODUCTION_VERSION, Pref.getAppVersion(mContext));
            events.put(DEVICE_CATEGORY, response.getInstallations()[0].getDeviceCategory().getName());
            events.put(DEVICE_TYPE, response.getInstallations()[0].getDeviceType().getName());
            events.put(OS_LANGUAGE, response.getInstallations()[0].getLanguage().getName());
            events.put(CHANNEL_ID, response.getInstallations()[0].getChannel().getChannelId());
            logCustomEvents(mContext, INSTALL, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logOnlineCheckout(Context mContext) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(BROWSER, BROWSER_VERSION);
            logCustomEvents(mContext, ONLINE_CHECKOUT, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logPasswordGenerator(Context mContext, String source) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(CREATE_PASSWORD, "True");
            events.put(SOURCE, source);
            logCustomEvents(mContext, PASSWORD_GENERATOR, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logPasswordVault(Context mContext, String action) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(ACTION, action);
            logCustomEvents(mContext, PASSWORD_VAULT, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logPersonalInfo(Context mContext, String action, String type) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(ACTION, action);
            events.put(TYPE, type);
            logCustomEvents(mContext, PERSONAL_INFO, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logProductTour(Context mContext, int step, String screenName, String button) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(SCREEN, SCREEN + step);
            events.put(SCREEN_NAME, screenName);
            events.put(BUTTON_CLICKED, button);
            logCustomEvents(mContext, PRODUCT_TOUR, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logSharing(Context mContext, String action) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(ACTION, action);
            events.put(TYPE, "");
            logCustomEvents(mContext, SHARING, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void logTwoFactorVerification(Context mContext, String status) {
        try {
            TreeMap<String, String> events = new TreeMap<>();
            events.put(STATUS, status);
            logCustomEvents(mContext, TWO_FACTOR_VERIFICATION, events);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void register(Context mContext) {
        String email = Pref.getValue(mContext, Constants.EMAIL, "UNKNOWN");
        String identify = email;

        boolean isUnknown = "UNKNOWN".equals(email);
        if (isUnknown) {
            identify = Pref.INSTALLATION_UUID;
        }
        if (!isUnknown) {
            if (Pref.getValue(mContext, "_isFirstRun_", true)) {
                Analytics.with(mContext).alias(email, new Options());
                Pref.setValue(mContext, "_isFirstRun_", false);
            } else {
                Traits traits = (Traits) Analytics.with(mContext).getAnalyticsContext().get("traits");
                String userId = (String) traits.get("userId");
                String anonymousId = (String) traits.get("anonymousId");
                if (userId == null) {
                    Analytics.with(mContext).alias(email, new Options());
                }
            }
        }

        if (!isUnknown) {
            Analytics.with(mContext).identify(identify, new Traits().putName(email), null);
        } else {
            Analytics.with(mContext).identify(identify);
        }
    }

}
