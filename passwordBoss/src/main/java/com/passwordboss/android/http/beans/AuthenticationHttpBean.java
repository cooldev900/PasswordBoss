package com.passwordboss.android.http.beans;

import org.json.JSONException;
import org.json.JSONObject;



public class AuthenticationHttpBean {
	private String email, language,installation,public_key;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	public String getPublicKey() {
		return public_key;
	}

	public void setPublicKey(String public_key) {
		this.public_key = public_key;
	}

	@Override
	public String toString() {
		try {
			JSONObject mJson = new JSONObject();
			
			if (!"".equals(this.email)) {
				mJson.put("email", this.email);
			}
			if (!"".equals(this.language)) {
				mJson.put("language", this.language);
			}
			if (!"".equals(this.installation)) {
				mJson.put("installation", this.installation);
			}
			if (!"".equals(this.public_key)) {
				mJson.put("public_key", this.public_key);
			}
			return mJson.toString();
		} catch (JSONException e) {
			////Log.print(e);
		}
		return null;
	}

}
