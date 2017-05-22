package com.passwordboss.android.database;

import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.utils.Pref;

import java.io.File;

public class DatabaseConstants {

	/* SQLCipher secured database constants */
	public static String SECURED_DB_NAME = "Store.db";
	public static String SECURED_DB_NAME_DOWNLOAD = "Store1.db";


	/* SQL non-secured database constants */
	public static String NON_SECURED_DB_NAME = "Controller.db";


	public static String getSecuredDBPath() {
		return Constants.APP_DATABASE
				+ File.separator + Pref.EMAIL + File.separator
				+ DatabaseConstants.SECURED_DB_NAME;
	}
	
	public static String getSecuredDBPathBackUp() {
		return Constants.APP_DATABASE
				+ File.separator + Pref.EMAIL + File.separator
				+ DatabaseConstants.SECURED_DB_NAME_DOWNLOAD;
	}
	
	// FIELDS
	public static final String ID = "id";
	public static final String UUID = "uuid";
	public static final String ACTIVE = "active";
	public static final String COMPLITED = "completed";
	public static final String INCOMPLITED = "incompleted";
	public static final String WAITING = "waiting";
	public static final String PENDING = "pending";
	public static final String WAITING_DATA = "waiting4data";
	public static final String NOSENT = "offline";

	public static final String SHARED = "shared";
	public static final String REJECTED = "rejected";
	public static final String CANCELED = "canceled";
	public static final String EXPIRED = "expired";
	public static final String REVOKED = "revoked";
	public static final String REMOVED = "Removed";
	public static final String STATUS = "status";
	public static final String ORDER = "order";
	public static final String CREATED_DATE = "created_date";
	public static final String DURATION = "duration";
	public static final String LAST_MODIFIED_DATE = "last_modified_date";
	public static final String SHARE = "share";
	public static final String HASH = "hash";

	public static final String LATEST_SYNC = "latest_sync";
	public static final String EMAIL = "email";
	public static final String SUBSCRIPTION_DATE = "subscription_date";
	public static final String EXPIRATION_DATE = "expiration_date";
	public static final String NEXT_VALIDATION_DATE = "next_validation_date";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String MULTIUSER = "multiuser";
	public static final String KEY = "key";
	public static final String VALUE = "value";
	public static final String MSG_ID = "msg_id";
	public static final String MSG_TYPE = "msg_type";
	public static final String THEME = "theme";
	public static final String ICON_TYPE = "icon_type";
	public static final String CONFIRM_ACTION = "confirm_action";
	public static final String CANCEL_ACTION = "cancel_action";
	public static final String SHOW_REMINDER = "show_reminder";
	public static final String ACCOUNT_STATUS = "account_status";
	public static final String SUBSCRIPTION_LEVEL = "subscription_level";
	public static final String START_TIMER_ON = "start_timer_on";
	public static final String START_DELAY_MINUTES = "start_delay_minutes";
	public static final String REPEAT_DELAY_MINUTES = "repeat_delay_minutes";
	public static final String SKIP_SHOW_IF_LATE_MINUTES = "skip_show_if_late_minutes";
	public static final String MAX_REP_CNT = "max_rep_cnt";
	public static final String EVENT_ID = "event_id";
	public static final String EVENT_PROPERTY = "event_property";
	public static final String EVENT_VALUE = "event_value";
	public static final String SHOW_ON_NO_EVENT = "show_on_no_event";
	public static final String SHOW_ON_NO_EVENT_DELAY_MINUTES = "show_on_no_event_delay_minutes";
	public static final String LANG = "lang";
	public static final String TITLE = "title";
	public static final String TITLE1 = "title1";
	public static final String TITLE2 = "title2";
	public static final String BODY = "body";
	public static final String BODY1 = "body1";
	public static final String BODY2 = "body2";
	public static final String CONFIG_ACTION_TEXT = "confirm_action_text";
	public static final String CANCEL_ACTION_TEXT = "cancel_action_text";
	public static final String ACTION_ID = "action_id";
	public static final String TIMER_DATE = "timer_date";
	public static final String LAST_DISPLAY_DATE = "last_display_date";
	public static final String DISPlAY_CNT = "display_cnt";
	public static final String INSTALLATION = "installation_id";
	public static final String EVENT_DEF_ID = "event_def_id";
	public static final String PROPERTY_KEY = "property_key";
	public static final String PROPERTY_VALUE = "property_value";
	public static final String ACCOUNT = "account";
	public static final String PROCESSED = "processed";
	public static final String SECTION_ID = "section_id";
	public static final String SECTION_NAME = "section_name";
	public static final String EVENT_NAME = "event_name";
	public static final String COLLECT = "collect";
	public static final String COLLECT_ONCE = "collect_once";
	public static final String MULTI_FACTOR_AUTHENTICATION = "multi_factor_authentication";
	public static final String HEADLINE = "headline";
	public static final String MESSAGE = "message";
	public static final String MESSAGE_TYPE = "message_type";
	public static final String URL = "url";
	public static final String BRAND_UUID = "brand_uuid";
	public static final String SECURE_ITEM_ID = "secure_item_id";
	public static final String PHONE = "phone";
	public static final String NICKNAME = "nickname";

	public static final String CREDIT_CARD = "CreditCard";
	public static final String MASTER_CREDIT_CARD = "Master";
	public static final String VISA_CREDIT_CARD = "Visa";
	public static final String AMEX_CREDIT_CARD = "American Express";
	public static final String JCB_CREDIT_CARD = "JCB";
	public static final String DISCOVER_CREDIT_CARD = "Discover";
	public static final String OTHER_CREDIT_CARD = "Other";

	public static final String LANGUAGE = "language_code";
	public static final String REMEMBER_EMAIL = "remember_email";
	public static final String DEFAULT = "default";

	public static final String ENABLE_PIN = "pin_enabled";
	public static final String AUTOLOCK = "auto_lock";
	public static final String PUSH_NOTIFICATIONS = "push_notifications";
	public static final String ACCOUNT_EMAIL = "account_e_mail";
	public static final String CLOUD_STORAGE_ENABLE = "enableStorageCloudBackup";

	public static final String ADVANCED_AUTO_LOGIN = "auto_login";
	public static final String ADVANCED_TWO_STEP_VERIFICATION = "two_step_verification";
	public static final String ADVANCED_TRUSTED_DEVICE = "trusted_device";
	public static final String ADVANCED_TRUSTED_DEVICE_DATE = "last_two_step_verification_date";

	public static final String None = "None";
	public static final String Free = "Free";
	public static final String Trial = "Trial";
	public static final String Paid = "Paid";
	public static final String Donation = "Donation";

	public static final String ADVANCED_AUTO_STORE_DATA = "auto_store_data";

	public static final String DOMAIN = "domain";
	public static final String CODE = "code";
	public static final String SYMBOL = "symbol";
	public static final String DIALING_CODE = "dialing_code";
	
	public static final String ANALITICS_CODE = "analytics_code";
	public static final String BUTTON_CLICKED = "button_clicked";
	public static final String BUY_BUTTON = "buy_button";
	public static final String DAYS_SINCE_ACCOUNT_CREATED = "days_since_account_created";
	
	
	// TABLE NAME
	public static final String SETTINGS_TABLE_NAME = "settings";
	public static final String MESSAGE_DEFINITIONS_TABLE_NAME = "message_definitions";
	public static final String MESSAGE_TRANSLATIONS_TABLE_NAME = "message_translations";
	public static final String MESSAGE_DATA_TABLE_NAME = "message_data";
	public static final String EVENTS_TABLE_NAME = "events";
	public static final String EVENTS_DEFINITION_TABLE_NAME = "event_definition";
	public static final String SITE_DOMAINS_TABLE_NAME = "site_domains";
	public static final String CURRENCY_TABLE_NAME = "currency";
	public static final String COUNTRY_TABLE_NAME = "country";
	public static final String ALERT_MESSAGES_TABLE_NAME = "country";
	public static final String MESSAGE_HISTORY = "message_history";
	

	/*
	 * Database - Controller Table - action
	 */
	public static final String ACTION_TABLE_NAME = "action";
	public static final String ACTION_START_DATE = "start_date";
	public static final String ACTION_COMPLETED_DATE = "completed_date";

	public static final String ACTION_TYPE = "action_type";

	public static final String ACTION_CONFIGURATION = "configuration";


	/*
	 * Database - Controller Table - configuration
	 */
	public static final String CONFIGURATION_TABLE_NAME = "configuration";

	/*
	 * Database - not specified Table - feature
	 */
	public static final String FEATURES_TABLE_NAME = "feature";
	public static final String FEATURES_IDENTIFIER = "identifier";

	/*
	 * Database - Controller Table - notification
	 */
	public static final String NOTIFICATION_TABLE_NAME = "notification";
	public static final String NOTIFICATION_DATE = "notification_date";
	public static final String NOTIFICATION_DISSMISSAL_DATE = "dismissal_date";
	public static final String NOTIFICATION_DEVICE = "device";
	public static final String NOTIFICATION_CATEGORY = "notification_category";
	public static final String NOTIFICATION_DISPLAY = "notification_display";
	public static final String NOTIFICATION_SITE = "site";

	/*
	 * Database - Controller Table - notification_category
	 */
	public static final String NOTIFICATION_CAT_TABLE_NAME = "notification_category";

	/*
	 * Database - Controller Table - notification_display
	 */
	public static final String NOTIFICATION_DISPLAY_TABLE_NAME = "notification_display";

	/*
	 * Database - Controller Table - secure_item_type
	 */
	public static final String SECURE_ITEM_TYPE_TABLE_NAME = "secure_item_type";
	public static final String SECURE_ITEM_TYPE_STORAGE_FORMAT = "storage_format";

	/*
	 * Database - Store Table - site_image_size
	 */
	public static final String SITE_IMAGE_SIZE_TABLE_NAME = "site_image_size";
	public static final String SITE_IMAGE_SIZE_WIDHT = "width";
	public static final String SITE_IMAGE_SIZE_HEIGHT = "height";

	/*
	 * Database - Controller Table - bookmarks
	 */
	public static final String SB_BOOKMARKS_TABLE_NAME = "bookmarks";
	public static final String SB_BOOKMARKS_FAVORITES = "is_favorite";
	public static final String SB_BOOKMARKS_SITE_URL = "site_url";
	public static final String SB_BOOKMARKS_ITEM_TYPE = "item_type";

	/*
	 * Database - Controller Table - recent_tabs
	 */
	public static final String RECENTSTABS_WEBSITENAME = "website_name";
	public static final String RECENTSTABS_IMAGE = "image";

	/*
	 * Database - Controller Table - channel
	 */
	public static final String CHANNEL_TABLE_NAME = "channel";
	public static final String CHANNEL_ID = "channel_id";
	public static final String CHANNEL_NOTES = "notes";
	public static final String CHANNEL_PARTNER = "partner";

	/*
	 * Database - Controller Table - feature_group_feature
	 */
	public static final String FEATURE_GROUP_FEATURE_TABLE_NAME = "feature_group_feature";
	public static final String FEATURE_GROUP_FEATURE_FEATURE = "feature_uuid";
	public static final String FEATURE_GROUP_FEATURE_FEATURE_GROUP = "feature_group_uuid";

	/*
	 * Database - Controller Table - favorite
	 */
	public static final String FAVORITE_TABLE_NAME = "favorite";

	/*
	 * Database - Controller Table - feature_group
	 */
	public static final String FEATURE_GROUP_TABLE_NAME = "feature_group";

	/*
	 * Database - Secure Table - secure_item_data
	 */
	public static final String STORAGE_REGION_TABEL_NAME = "storage_region";

	/* Table- "user_info" */
	public static final String USER_INFO_TABLE_NAME = "user_info";
	public static final String STORAGE_REGION_UUID = "storage_region_uuid";
	public static final String SUBSCRIPTION = "subscription";

	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String PIN_NUMBER = "pin_number";

	public static final String MOBILENO = "mobile_no";
	public static final String PUBLICKEY = "public_key";
	public static final String PRIVATEKEY = "private_key";
	public static final String ITEM_SETTINGS_COUNTRY = "country";
	public static final String SEARCH_PROVIDER_URL = "search_provider_url";
	public static final String START_PAGE_URL = "start_page_url";
	

	/* Table - Country */

	public static final String COUNTRY_SYMBOL = "currency";

	/* Table - DataCenters */
	public static final String DATACENTER_TABLE_NAME = "data_location";
	public static final String DATACENTER_COUNTRY_ID = "country_id";
	public static final String DATACENTER_LANGUAGE_ID = "language_id";

	/* Table - Devices */
	public static final String DEVICES_TABLE_NAME = "device";
	public static final String DEVICES_CATEGORY = "device_category";
	public static final String DEVICES_OS = "os";

	/* Table - Language */
	public static final String LANGUAGE_TABLE_NAME = "language";
	public static final String LANGUAGE_TRANSLATED = "translated";
	public static final String LANGUAGE_TRANSLATED_NAME = "translated_name";


	/* Table - Progress */
	public static final String USER_SUBSCRIPTION_TABLE_NAME = "user_subscription";

}
