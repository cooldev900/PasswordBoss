package com.passwordboss.android.subscription;

import android.app.Dialog;
import android.content.Context;

import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Feature;
import com.passwordboss.android.database.bll.FeatureBll;
import com.passwordboss.android.utils.Pref;

import java.util.HashMap;
import java.util.List;

public class SubscriptionValidator {

	public static String FEATURE_PV_UNLIMITED_PASSWORD_STORAGE   				="1001";
	public static String FEATURE_PV_ADD_MANAGE_LOGINS                           ="1002";
	public static String FEATURE_PV_AUTO_LOGIN                                  ="1003";
	public static String FEATURE_PV_ADD_MANAGE_FAVORITES                        ="1004";
	public static String FEATURE_DW_ADD_MANAGE_CREDIT_CARD                      ="2001";
	public static String FEATURE_DW_ADD_MANAGE_BANK_ACCOUNT                     ="2002";
	public static String FEATURE_DW_ADD_MANAGE_PAY_PAL                          ="2003";
	public static String FEATURE_PI_ADD_MANAGE_DRIVER_LICENSE                   ="3001";
	public static String FEATURE_PI_ADD_MANAGE_PASSPORT                         ="3002";
	public static String FEATURE_PI_ADD_MANAGE_ADDRESS                          ="3003";
	public static String FEATURE_PI_ADD_MANAGE_PHONE                            ="3004";
	public static String FEATURE_PI_ADD_MANAGE_COMPANY                          ="3005";
	public static String FEATURE_PI_ADD_MANAGE_EMAIL                            ="3006";
	public static String FEATURE_PI_ADD_MANAGE_MEMBER_ID                        ="3007";
	public static String FEATURE_PI_ADD_MANAGE_SOCIAL_SECURITY_NUMBER           ="3008";
	public static String FEATURE_PI_ADD_MANAGE_SECURE_NOTE                      ="3009";
	public static String FEATURE_PI_ADD_MANAGE_FAVORITES                        ="3010";
	public static String FEATURE_ACCESS_SECURE_BROWSER                          ="4001";
	public static String FEATURE_ACCESS_PASSWORD_GENERATOR                      ="5001";
	public static String FEATURE_UP_TO_5_SHARES                                 ="6001";
	public static String FEATURE_UNLIMITED_SHARES                               ="6002";
	public static String FEATURE_MANAGE_SHARES                                  ="6003";
	public static String FEATURE_SHOW_SECURITY_SCORE                            ="7001";
	public static String FEATURE_UPDATE_SECURITY_SCORE                          ="7002";
	public static String FEATURE_INVITE_FRIEND                                  ="8001";
	public static String FEATURE_NOTIFICATIONS_AND_ALERTS                       ="8002";
	public static String FEATURE_2_STEP_AUTHENTICATION                          ="8003";
	public static String FEATURE_DISABLE_ADS                                    ="8004";
	public static String FEATURE_ACCOUNT_MANAGEMENT                             ="8005";
	public static String FEATURE_SYNCHRONIZE_DATA_ACROS_DEVICES                 ="9001";
	public static String FEATURE_ONLINE_BACKUP                                  ="9002";
	public static String FEATURE_CHOOSE_DATA_CENTER                             ="9003";
	public static String FEATURE_ALL_NON_LISTED_FUNCTIONALITY                   ="9999";
	
	public static HashMap<String, Feature> featureList = null;
	public static Dialog mDialog = null;
	public  static boolean isShow = false;
	public static boolean isFeatureValid(String idFeature, Context mContext, boolean showDialog) {
		boolean result = false;
		try {
			if (featureList == null) {
				setDate(mContext);
			}
			if (featureList != null) {
				Feature feature = featureList.get(idFeature);
				if (feature != null) {
					result = true;
				} else {
					if (showDialog) {
						SubscriptionValidator.showUpgradeNeeded(mContext);
					}
				}
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static void setDate(Context mContext) {
		try {
			DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
			FeatureBll mFeatureBll = new FeatureBll(mDatabaseHelperSecure);
			List<Feature> listRecords = mFeatureBll.getAllRecords();
			featureList = new HashMap<>();
			for (Feature f :listRecords) {
				featureList.put(f.getIdentifier(), f);
			}
		} catch (Exception e) {
		}
	}
	
	public static void hideUpgradeNeeded() {
		if (mDialog != null){ 
			mDialog.dismiss();
		}
	}
	public static void showUpgradeNeeded(final Context mContext) {
		if (!isShow) {
			try {
				throw new UnsupportedOperationException(); // TODO: 5/13/2016 refactor, see old implementation
			} catch (Exception e) {
			}
		}
	}
	
}
