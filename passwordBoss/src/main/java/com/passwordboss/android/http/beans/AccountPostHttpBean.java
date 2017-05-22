package com.passwordboss.android.http.beans;

import org.joda.time.DateTime;



public class AccountPostHttpBean {

	private String campaign, channel, chrome_version, device_category,
		device_marketing_name, device_type, extended_information, firefox_version,
		identifier, ie_version, manufacturer, language, medium, nickname,
		os,partner,referrer,software_version,source,timezone;
	private int resolution_height, resolution_width, screen_height, screen_width;
	
	private DateTime uninstall_date;
	
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChrome_version() {
		return chrome_version;
	}
	public void setChromeVersion(String chrome_version) {
		this.chrome_version = chrome_version;
	}
	public String getDeviceCategory() {
		return device_category;
	}
	public void setDeviceCategory(String device_category) {
		this.device_category = device_category;
	}
	public String getDeviceMarketingName() {
		return device_marketing_name;
	}
	public void setDeviceMarketingName(String device_marketing_name) {
		this.device_marketing_name = device_marketing_name;
	}
	public String getDeviceType() {
		return device_type;
	}
	public void setDeviceType(String device_type) {
		this.device_type = device_type;
	}
	public String getExtendedinformation() {
		return extended_information;
	}
	public void setExtendedInformation(String extended_information) {
		this.extended_information = extended_information;
	}
	public String getFirefoxVersion() {
		return firefox_version;
	}
	public void setFirefoxVersion(String firefox_version) {
		this.firefox_version = firefox_version;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getIeVersion() {
		return ie_version;
	}
	public void setIeVersion(String ie_version) {
		this.ie_version = ie_version;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getReferrer() {
		return referrer;
	}
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	public String getSoftwareVersion() {
		return software_version;
	}
	public void setSoftwareVersion(String software_version) {
		this.software_version = software_version;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public int getResolutionHeight() {
		return resolution_height;
	}
	public void setResolutionHeight(int resolution_height) {
		this.resolution_height = resolution_height;
	}
	public int getResolutionWidth() {
		return resolution_width;
	}
	public void setResolutionWidth(int resolution_width) {
		this.resolution_width = resolution_width;
	}
	public int getScreenHeight() {
		return screen_height;
	}
	public void setScreenHeight(int screen_height) {
		this.screen_height = screen_height;
	}
	public int getScreenWidth() {
		return screen_width;
	}
	public void setScreenWidth(int screen_width) {
		this.screen_width = screen_width;
	}
	public DateTime getUninstallDate() {
		return uninstall_date;
	}
	public void setUninstallDate(DateTime uninstall_date) {
		this.uninstall_date = uninstall_date;
	}
}
