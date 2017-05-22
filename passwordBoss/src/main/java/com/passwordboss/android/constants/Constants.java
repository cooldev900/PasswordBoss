package com.passwordboss.android.constants;

public class Constants {


	public static String getAPPHome() {
		return "/data/data/com.passwordboss.android"; // // TODO: 5/14/2016 not sure about, need to review
	}

	public static String APP_HOME = getAPPHome();
	public static String APP_DATABASE = getAPPHome();
	public static String DIR_IMAGES = APP_HOME + "/data";
	public static String DIR_IMAGES_FAVICON = DIR_IMAGES + "/Favicon";
	public static String DIR_IMAGES_FAVICON_LIST = DIR_IMAGES_FAVICON + "/List";

	public static String PREF_FILE = "PREF_PASSWORDBOSS";
	public static final String EMAIL = "EMAIL";
	public static final String PIN = "PIN";

	public static final String PV_TYPE = "PV";


	public static final String TERMS_OF_SERVICES_LINK = "https://www.passwordboss.com/terms-of-use/";
	public static final String PRIVACY_POLICY_LINK = "https://www.passwordboss.com/privacy-policy/";

	public static final String PUBLIC_KEY = "PublicKey";
	public static final String PRIVATE_KEY = "PrivateKey";
	public static final String INSTALLATION_UUID = "INSTALLATION_UUID";
	public static final String CHANNEL_ID = "CHANNEL_ID";

	public static final String LANGUAGE_CHOSEN = "CHOOSE_LANGUAGE";
	public static final String APLICATION_LINKS = "APLICATION_LINKS";

	public static final String LATEST_SYNC = "latest_sync";

	public static final String TABLET_VERSION = "Tablet";
	public static final String MOBILE_VERSION = "Phone";
	public static final String MOBILE = "Mobile";

	public static final String CONFIGURATION_MASTER_PASSWORD_CHANGE = "master_password_changed";
	public static final String CONFIGURATION_STORE_DATABASE_HASH = "store_database_hash";
	public static final String CONFIGURATION_MASTER_PASSWORD_VERSION = "master_password_version";
	public static final String CONFIGURATION_LAST_SYNC_HASH = "last_sync_hash";
	public static final String SEED_SINCE= "seed_since";
	public static final String EXT_APP_PARAMS= "ext_app_params";
	public static final String NO_EMAIL = "defaults@local.install";

	public static final String SECURE_ITEM_NOTIFICATION_SHARE = "share";
	public static final String SECURE_ITEM_NOTIFICATION_SEC_ALERT = "sec_alert";
	public static final String PAID = "Paid";
	public static final String FREE = "Free";
	public static final String TRIAL = "Trial";

	public static final String ACTIVE_USER_ANALYTIC = "active_user_analytic";
}

