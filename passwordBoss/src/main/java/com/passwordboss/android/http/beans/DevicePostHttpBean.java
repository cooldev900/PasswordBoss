package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class DevicePostHttpBean implements Serializable {
	private String application_language,
		device_language, installation, 
		nickname, software_version,
		verification;

	public String getApplicationLanguage() {
		return application_language;
	}

	public void setApplicationLanguage(String application_language) {
		this.application_language = application_language;
	}

	public String getDeviceLanguage() {
		return device_language;
	}

	public void setDeviceLanguage(String device_language) {
		this.device_language = device_language;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSoftwareVersion() {
		return software_version;
	}

	public void setSoftwareVersion(String software_version) {
		this.software_version = software_version;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}
	
}
