package com.passwordboss.android.http.beans;

public class SenderAccountHttpBean extends BaseHttpBean{
	
	private CategoryHttpBean country;
	private LanguageHttpBean language;
	private InstallationsHttpBean installation;
	private String email, first_name, last_name, phone, public_key, 
		referral_code, timezone;	
	private BaseHttpBean storage_region;
	private SubscriptionHttpBean subscription;
	private boolean synchronize;
	
	public boolean isSynchronize() {
		return synchronize;
	}
	public void setSynchronize(boolean synchronize) {
		this.synchronize = synchronize;
	}
	public CategoryHttpBean getCountry() {
		return country;
	}
	public void setCountry(CategoryHttpBean country) {
		this.country = country;
	}
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	public InstallationsHttpBean getInstallation() {
		return installation;
	}
	public void setInstallation(InstallationsHttpBean installation) {
		this.installation = installation;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPublicKey() {
		return public_key;
	}
	public void setPublicKey(String publicKey) {
		this.public_key = publicKey;
	}
	public String getReferralCode() {
		return referral_code;
	}
	public void setReferralCode(String referralCode) {
		this.referral_code = referralCode;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public BaseHttpBean getStorageRegion() {
		return storage_region;
	}
	public void setStorageRegion(BaseHttpBean storageRegion) {
		this.storage_region = storageRegion;
	}
	public SubscriptionHttpBean getSubscription() {
		return subscription;
	}
	public void setSubscription(SubscriptionHttpBean subscription) {
		this.subscription = subscription;
	}
}
