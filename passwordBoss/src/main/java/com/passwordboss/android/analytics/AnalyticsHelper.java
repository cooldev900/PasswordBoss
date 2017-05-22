package com.passwordboss.android.analytics;

import android.content.Context;

import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

public class AnalyticsHelper {

	public static String Application_InvideOthers_Facebook = "Application__InvideOthers--Facebook";
	public static String Application_InvideOthers_Twitter = "Application__InvideOthers--Twitter";
	public static String Application_InvideOthers_Email = "Application__InvideOthers--Email";
	
	public static String NavigationPasswordVault = "Nav Password Vault";
	public static String NavigationDigitalWallet = "Nav Digital Wallet";
	public static String NavigationPersonalInfo = "Nav Personal Info";
	public static String NavigationBrowser = "Nav Browser";
	public static String NavigationPasswordGenerator = "Nav Password Generator";
	public static String NavigationShareCenter = "Nav Share Center";
	public static String NavigationLock = "Nav Lock";		
	public static String NavigationSettings = "Nav Settings";
	public static String NavigationMessages = "Nav Messages";
	public static String NavigationUpgradeToPremium = "Nav Upgrade";
	
	// Custom User Attributes
	public static String PinEnable = "pin_enable";						
	public static String ApplicationVersion = "Aplication version";
	public static String Channel_ID = "Channel_ID";
	
	public static void logCustomEvent(Context context, String event) {

		try {
			String[] tmp1 = event.split("__");

			String _event, _property_KEY, _property_VALUE;
			if (tmp1.length > 2) {
				_event = tmp1[0];
				String[] tmp2 = tmp1[1].split("--");
				if (tmp2.length >= 2) {
					_property_KEY = tmp2[0];
					_property_VALUE = tmp2[1];
				} else {
					_property_KEY = tmp2[0];
					_property_VALUE = "";
				}
				if ("EMPTY STRING".equals(_property_VALUE)) {
					_property_VALUE = "";
				}
				
				if (!"Nothing".equals(_property_KEY)) {
					Analytics.with(context).track(_event, new Properties().putValue(_property_KEY, _property_VALUE));		
				}
			}
			Analytics.with(context).flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
