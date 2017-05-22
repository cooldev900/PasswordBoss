package com.passwordboss.android.http.beans;

import org.joda.time.DateTime;

public class InstallationsHttpBean extends BaseHttpBean{
	private String chrome_version, device_marketing_name, timezone, 
		software_version, nickname, manufacturer, identifier, ie_version, 
		ip_address, extended_information, firefox_version, default_browser;
	private CountryHttpBean country;
	private ChannelHttpBean channel;
	private DeviceCategoryHttpBean device_category;
	private DeviceTypeHttpBean device_type;
	private LanguageHttpBean language;
	private OsHttpBean os;
	private PartnerHttpBean partner;
	private MediumHttpBean medium;
	private SourceHttpBean source;
	private CampaignHttpBean campaign;
	private DateTime uninstall_date, last_contact_date;
	private int resolution_height, resolution_width, 
		screen_height, screen_width;
	private boolean account_created;
	
	public String getDefaultBrowser() {
		return default_browser;
	}
	public void setDefaultBrowser(String default_browser) {
		this.default_browser = default_browser;
	}
	public String getChromeVersion() {
		return chrome_version;
	}
	public void setChromeVersion(String chrome_version) {
		this.chrome_version = chrome_version;
	}
	public String getDeviceMarketingName() {
		return device_marketing_name;
	}
	public void setDeviceMarketingName(String device_marketing_name) {
		this.device_marketing_name = device_marketing_name;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getSoftwareVersion() {
		return software_version;
	}
	public void setSoftwareVersion(String software_version) {
		this.software_version = software_version;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
	public String getIpAddress() {
		return ip_address;
	}
	public void setIpAddress(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getExtendedInformation() {
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
	public CountryHttpBean getCountry() {
		return country;
	}
	public void setCountry(CountryHttpBean country) {
		this.country = country;
	}
	public ChannelHttpBean getChannel() {
		return channel;
	}
	public void setChannel(ChannelHttpBean channel) {
		this.channel = channel;
	}
	public DeviceCategoryHttpBean getDeviceCategory() {
		return device_category;
	}
	public void setDeviceCategory(DeviceCategoryHttpBean device_category) {
		this.device_category = device_category;
	}
	public DeviceTypeHttpBean getDeviceType() {
		return device_type;
	}
	public void setDeviceType(DeviceTypeHttpBean device_type) {
		this.device_type = device_type;
	}
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public OsHttpBean getOs() {
		return os;
	}
	public void setOs(OsHttpBean os) {
		this.os = os;
	}
	public PartnerHttpBean getPartner() {
		return partner;
	}
	public void setPartner(PartnerHttpBean partner) {
		this.partner = partner;
	}
	public MediumHttpBean getMedium() {
		return medium;
	}
	public void setMedium(MediumHttpBean medium) {
		this.medium = medium;
	}
	public SourceHttpBean getSource() {
		return source;
	}
	public void setSource(SourceHttpBean source) {
		this.source = source;
	}
	public CampaignHttpBean getCampaign() {
		return campaign;
	}
	public void setCampaign(CampaignHttpBean campaign) {
		this.campaign = campaign;
	}
	public DateTime getUninstallDate() {
		return uninstall_date;
	}
	public void setUninstallDate(DateTime uninstall_date) {
		this.uninstall_date = uninstall_date;
	}
	public DateTime getLastContactDate() {
		return last_contact_date;
	}
	public void setLastContactDate(DateTime last_contact_date) {
		this.last_contact_date = last_contact_date;
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
	public boolean isAccountCreated() {
		return account_created;
	}
	public void setAccountCreated(boolean account_created) {
		this.account_created = account_created;
	}
}
