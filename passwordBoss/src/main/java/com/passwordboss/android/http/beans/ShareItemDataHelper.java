package com.passwordboss.android.http.beans;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.crypto.CryptoHelper;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.bll.UserInfoBll;
import com.passwordboss.android.jsonbean.DateTimeTypeConverter;
import com.passwordboss.android.utils.Pref;

import org.joda.time.DateTime;


public class ShareItemDataHelper {
	private String key;
	private String payload;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public static ShareItemData convertShareItemDataHelperToShareItemData(String data, Context context) {
		ShareItemDataHelper mShareItemDataHelper = null;
		ShareItemData shareItemData = null;
		try {
			Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
			mShareItemDataHelper = gson.fromJson(data, ShareItemDataHelper.class);
			//android.util.Log.v("data", data);
;			DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure.getHelper(context, Pref.DATABASE_KEY);
			UserInfoBll mUserInfoBll = new UserInfoBll(mDatabaseHelperSecure);
			UserInfo mUserInfo = mUserInfoBll.getUserInfoByEmail(Pref.getValue(context, Constants.EMAIL, null));
			if (mShareItemDataHelper != null) {
				
				/*if(mUserInfo!=null)
				android.util.Log.v("mUserInfo", mUserInfo.toString());
				else 
					android.util.Log.v("mUserInfo", "null");
					
				
				//android.util.Log.v("mShareItemDataHelper.getKey()", mShareItemDataHelper.getKey());
				if(mUserInfo.getPrivateKey()!=null)
				{	

					//android.util.Log.v("mUserInfo.getPrivateKey()LEN", String.valueOf(mShareItemDataHelper.getKey().length()));
				//android.util.Log.v("mUserInfo.getPrivateKey()", String.valueOf(mShareItemDataHelper.getKey()));
				//android.util.Log.v("mUserInfo.getPrivateKey()", new String(mUserInfo.getPrivateKey()));
				//android.util.Log.v("mUserInfo.getPrivateKey() UTF8", new String(mUserInfo.getPrivateKey(),"UTF-8"));
				}
				else 
					//android.util.Log.v("mUserInfo.getPrivateKey()", "NULL"); */
				
				String aesKey = CryptoHelper.getRSADecryptedString(mShareItemDataHelper.getKey(), mUserInfo.getPrivateKey());
				//android.util.Log.v("AESKEY",aesKey);
				String payload = CryptoHelper.getAESKeySetDecryptedString(mShareItemDataHelper.getPayload(), aesKey);
				//android.util.Log.v("payload",payload);
				ShareItemPayload payloadObject  = gson.fromJson(payload, ShareItemPayload.class);
				shareItemData = new ShareItemData(mShareItemDataHelper.getKey(), payloadObject, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			////Log.print(e);
		}
		return shareItemData;
	}

}