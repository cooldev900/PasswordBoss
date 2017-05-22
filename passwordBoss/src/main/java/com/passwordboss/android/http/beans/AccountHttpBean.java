package com.passwordboss.android.http.beans;

import org.joda.time.DateTime;

public class AccountHttpBean extends BaseHttpBean {
	private InstallationsHttpBean installation;
	private BaseHttpBean storage_region;	
	private String email, first_name, last_name, phone, referral_code, timezone;
	private CountryHttpBean country;
	private LanguageHttpBean language;
	private boolean synchronize;
	private SubscriptionHttpBean subscription;
	private DateTime last_login;
	private String public_key;

	public InstallationsHttpBean getInstallation() {
		return installation;
	}
	public void setInstallation(InstallationsHttpBean installation) {
		this.installation = installation;
	}
	public BaseHttpBean getStorageRegion() {
		return storage_region;
	}
	public void setStorageRegion(BaseHttpBean storage_region) {
		this.storage_region = storage_region;
	}
	public CountryHttpBean getCountry() {
		return country;
	}
	public void setCountry(CountryHttpBean country) {
		this.country = country;
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
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getReferralCode() {
		return referral_code;
	}
	public void setReferralCode(String referral_code) {
		this.referral_code = referral_code;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public boolean isSynchronize() {
		return synchronize;
	}
	public void setSynchronize(boolean synchronize) {
		this.synchronize = synchronize;
	}
	public SubscriptionHttpBean getSubscription() {
		return subscription;
	}
	public void setSubscription(SubscriptionHttpBean subscription) {
		this.subscription = subscription;
	}
	public DateTime getLast_login() {
		return last_login;
	}
	public void setLast_login(DateTime last_login) {
		this.last_login = last_login;
	}
	public String getPublicKey() {
		return public_key;
	}
	public void setPublicKey(String public_key) {
		this.public_key = public_key;
	}
}
