package com.passwordboss.android.http.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class ServerResponseHttpBeanTwoStep implements Serializable{
	private ErrorHttpBean error;
	private AccountHttpBean account;
	private LanguageHttpBean language;
	private LanguageHttpBean languages[];
	private LinksHttpBean links;
	private String media, version;
	private DateTime now;
	private DateTime next_mobile_sync;
	private DateTime next_offline_sync;
	private DateTime next_sync;
	
	private RecommendedSiteHttpBean recommended_sites[];
	private InstallationsHttpBean installations[];
	private DeviceHttpBean devices[];
	private DeviceHttpBean device;
	private RecommendedSiteHttpBean sites[];
	private CategoryHttpBean categories[];
	private FeaturesHttpBean features;
	private SharesHttpBean shares;
	private FileHttpBean secure_files[];
	private FileHttpBean secure_file;
	private SiteDomainHttpBean site_domains[];
	private String con;
	private TwoStepVerificationHttpBean two_step_verification;
	private String stats_sent_at;
	private boolean auth;
//	private MessageDefinitionsHttpBean message_definitions[];
	
	public SiteDomainHttpBean[] getSiteDomains() {
		return site_domains;
	}
	public void setSiteDomains(SiteDomainHttpBean[] site_domains) {
		this.site_domains = site_domains;
	}
	//	public MessageDefinitionsHttpBean[] getMessageDefinitions() {
//		return message_definitions;
//	}
//	public void setMessageDefinitions(MessageDefinitionsHttpBean[] messageDefinitions) {
//		this.message_definitions = messageDefinitions;
//	}
	public FileHttpBean[] getSecureFiles() {
		return secure_files;
	}
	public void setSecureFiles(FileHttpBean[] secureFiles) {
		this.secure_files = secureFiles;
	}
	
	public FileHttpBean getSecureFile() {
		return secure_file;
	}
	public void setSecureFile(FileHttpBean secure_file) {
		this.secure_file = secure_file;
	}
	public SharesHttpBean getShares() {
		return shares;
	}
	public void setShares(SharesHttpBean shares) {
		this.shares = shares;
	}
	public FeaturesHttpBean getFeatures() {
		return features;
	}
	public void setFeatures(FeaturesHttpBean features) {
		this.features = features;
	}
	private BaseHttpBean device_categories[], secure_item_types[], 
		storage_regions[];
	
	public RecommendedSiteHttpBean[] getRecommendedSites() {
		return recommended_sites;
	}
	public void setRecommendedSites(RecommendedSiteHttpBean[] recommendedSites) {
		this.recommended_sites = recommendedSites;
	}
	public BaseHttpBean[] getStorageRegions() {
		return storage_regions;
	}
	public void setStorageRegions(BaseHttpBean[] storageRegions) {
		this.storage_regions = storageRegions;
	}
	public BaseHttpBean[] getSecureItemTypes() {
		return secure_item_types;
	}
	public void setSecureItemTypes(BaseHttpBean[] secureItemTypes) {
		this.secure_item_types = secureItemTypes;
	}
	public LanguageHttpBean[] getLanguages() {
		return languages;
	}
	public void setLanguages(LanguageHttpBean[] languages) {
		this.languages = languages;
	}
	public BaseHttpBean[] getDeviceCategories() {
		return device_categories;
	}
	public void setDeviceCategories(BaseHttpBean[] deviceCategories) {
		this.device_categories = deviceCategories;
	}
	public DeviceHttpBean getDevice() {
		return device;
	}
	public void setDevice(DeviceHttpBean device) {
		this.device = device;
	}
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public LinksHttpBean getLinks() {
		return links;
	}
	public void setLinks(LinksHttpBean links) {
		this.links = links;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public DateTime getNow() {
		return now;
	}
	public void setNow(DateTime now) {
		this.now = now;
	}
	public InstallationsHttpBean[] getInstallations() {
		return installations;
	}
	public void setInstallations(InstallationsHttpBean[] installations) {
		this.installations = installations;
	}
	public AccountHttpBean getAccount() {
		return account;
	}
	public void setAccount(AccountHttpBean account) {
		this.account = account;
	}
	public ErrorHttpBean getError() {
		return error;
	}
	public void setError(ErrorHttpBean error) {
		this.error = error;
	}
	public DeviceHttpBean[] getDevices() {
		return devices;
	}
	public void setDevices(DeviceHttpBean[] devices) {
		this.devices = devices;
	}
	public RecommendedSiteHttpBean[] getSites() {
		return sites;
	}
	public void setSites(RecommendedSiteHttpBean[] sites) {
		this.sites = sites;
	}
	public CategoryHttpBean[] getCategories() {
		return categories;
	}
	public void setCategories(CategoryHttpBean[] categories) {
		this.categories = categories;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public TwoStepVerificationHttpBean getTwo_step_verification() {
		return two_step_verification;
	}
	public void setTwo_step_verification(TwoStepVerificationHttpBean two_step_verification) {
		this.two_step_verification = two_step_verification;
	}
	public String getStats_sent_at() {
		return stats_sent_at;
	}
	public void setStats_sent_at(String stats_sent_at) {
		this.stats_sent_at = stats_sent_at;
	}
	public DateTime getNextMobileSync() {
		return next_mobile_sync;
	}
	public void setNextMobileSync(DateTime next_mobile_sync) {
		this.next_mobile_sync = next_mobile_sync;
	}
	public DateTime getNextOfflineSync() {
		return next_offline_sync;
	}
	public void setNextOfflineSync(DateTime next_offline_sync) {
		this.next_offline_sync = next_offline_sync;
	}
	public DateTime getNextSync() {
		return next_sync;
	}
	public void setNextSync(DateTime next_sync) {
		this.next_sync = next_sync;
	}
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
