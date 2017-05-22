package com.passwordboss.android.http.beans;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.passwordboss.android.beans.SecureItemDataJSON;
import com.passwordboss.android.crypto.CryptoHelper;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SiteUri;
import com.passwordboss.android.database.bll.SiteUriBll;
import com.passwordboss.android.jsonbean.DateTimeTypeConverter;
import com.passwordboss.android.utils.Pref;

import org.joda.time.DateTime;


public class ShareItemData {
	private String key, publicKey;
	private ShareItemPayload payload;
	
	
	public ShareItemData(String key, ShareItemPayload payload, String publicKey) {
		this.key = key;
		this.payload = payload;
		this.publicKey = publicKey;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public ShareItemPayload getPayload() {
		return payload;
	}
	public void setPayload(ShareItemPayload payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		String result= "";
		try {
			Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
			String payloadEncrypted = CryptoHelper.getAESKeySetEncryptedString(gson.toJson(payload), key);
			String keyAes = CryptoHelper.getRSAEncryptedString(this.key, Base64.decode(publicKey,Base64.DEFAULT));
			result = "{ \"key\":\"" + keyAes + "\"," + "\"payload\":\"" + payloadEncrypted + "\"}";
		} catch (Exception e) {
			////Log.print(e);
		}
		return result;
	}
	
	public static ShareItemData convertSecureItemToShareItemData(Context context, SecureItem secureItem, String publicKey) {
		ShareItemData shareItemData = null;
		try {
			if (secureItem != null) {
				SiteUri mSiteUri = null;
				SecureItemDataJSON secureItemDataJSON = SecureItemDataJSON.getSecureItemDataString(secureItem.getSecureItemData());
				if (secureItem.getSiteId() != null) {
					DatabaseHelperSecure databaseHelperSecure = DatabaseHelperSecure.getHelper(context, Pref.DATABASE_KEY);
					SiteUriBll mSiteUriBll = new SiteUriBll(databaseHelperSecure);
					mSiteUri = mSiteUriBll.getSiteUriBySiteId(secureItem.getSiteId());
				}
				
				ShareItemPayload shareItemPayload = new ShareItemPayload();
				shareItemPayload.setId(secureItem.getId());
				shareItemPayload.setCategoryId(secureItem.getFolder().getId());
				shareItemPayload.setHash(secureItem.getHash());
				shareItemPayload.setName(secureItem.getName());
				shareItemPayload.setSecureItemTypeName(secureItem.getSecureItemTypeName());
				shareItemPayload.setSiteId(secureItem.getSiteId());
				shareItemPayload.setUuid(secureItem.getUuid());
				shareItemPayload.setFavorite(secureItem.isFavorite());
				shareItemPayload.setColor(secureItem.getColor());
				shareItemPayload.setType(secureItem.getType());
				shareItemPayload.setShare(secureItem.isShare());
				shareItemPayload.setVerified(secureItem.isVerified());
				shareItemPayload.setOrder(secureItem.getOrder());
				shareItemPayload.setCreatedDate(secureItem.getCreatedDate());
				shareItemPayload.setLastModifiedDate(secureItem.getLastModifiedDate());
				shareItemPayload.setLoginUrl(secureItem.getLoginUrl());
				shareItemPayload.setData(secureItemDataJSON);
				if (mSiteUri != null) {
					shareItemPayload.setSiteUrl(mSiteUri.getUri());
				}
				shareItemData = new ShareItemData(CryptoHelper.createSecretAESKeySetString(), shareItemPayload, publicKey);
			}
		} catch (Exception e) {
			////Log.print(e);
		}
		return shareItemData;
	}
}