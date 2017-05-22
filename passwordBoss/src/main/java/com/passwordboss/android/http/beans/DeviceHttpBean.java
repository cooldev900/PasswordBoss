package com.passwordboss.android.http.beans;

import org.joda.time.DateTime;

public class DeviceHttpBean extends BaseHttpBean{
	private LanguageHttpBean application_language, device_language;
	private InstallationsHttpBean installation;
	private DateTime last_sync_date, software_last_update_date;
	private String nickname, software_version;
	
	public LanguageHttpBean getApplicationLanguage() {
		return application_language;
	}
	public void setApplicationLanguage(LanguageHttpBean application_language) {
		this.application_language = application_language;
	}
	public LanguageHttpBean getDeviceLanguage() {
		return device_language;
	}
	public void setDeviceLanguage(LanguageHttpBean device_language) {
		this.device_language = device_language;
	}
	public InstallationsHttpBean getInstallation() {
		return installation;
	}
	public void setInstallation(InstallationsHttpBean installation) {
		this.installation = installation;
	}
	public DateTime getLastSyncDate() {
		return last_sync_date;
	}
	public void setLastSyncDate(DateTime last_sync_date) {
		this.last_sync_date = last_sync_date;
	}
	public DateTime getSoftwareLastUpdateDate() {
		return software_last_update_date;
	}
	public void setSoftwareLastUpdateDate(DateTime software_last_update_date) {
		this.software_last_update_date = software_last_update_date;
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
}
