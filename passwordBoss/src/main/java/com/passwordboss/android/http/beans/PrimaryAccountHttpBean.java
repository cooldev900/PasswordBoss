package com.passwordboss.android.http.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class PrimaryAccountHttpBean  implements Serializable{
	private String primary_account, email, first_name, 
		last_name, phone, referral_code, timezone;
	private boolean synchronize;
	private LanguageHttpBean language;
	private InstallationsHttpBean installation;
	private CountryHttpBean country;
	
	private DateTime last_login;
	private BaseHttpBean storage_region;
	private SubscriptionHttpBean subscription;
	
	public String getPrimary_account() {
		return primary_account;
	}
	public void setPrimaryAccount(String primary_account) {
		this.primary_account = primary_account;
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
	public InstallationsHttpBean getInstallation() {
		return installation;
	}
	public void setInstallation(InstallationsHttpBean installation) {
		this.installation = installation;
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
	public BaseHttpBean getStorageRegion() {
		return storage_region;
	}
	public void setStorageRegion(BaseHttpBean storage_region) {
		this.storage_region = storage_region;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public boolean isSynchronize() {
		return synchronize;
	}
	public void setSynchronize(boolean synchronize) {
		this.synchronize = synchronize;
	}
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public DateTime getLastLogin() {
		return last_login;
	}
	public void setLastLogin(DateTime last_login) {
		this.last_login = last_login;
	}
	public SubscriptionHttpBean getSubscription() {
		return subscription;
	}
	public void setSubscription(SubscriptionHttpBean subscription) {
		this.subscription = subscription;
	}

	
}
