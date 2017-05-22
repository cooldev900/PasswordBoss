package com.passwordboss.android.http.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class SubscriptionHttpBean extends BaseHttpBean {
	
	private DateTime expiration_date;
	private DateTime next_validation_date;	
	private PrimaryAccountHttpBean primary_account;
	private SubscriptionLevelHttpBean subscription_level;
	private ProviderHttpBean provider;
	private String domain;
	private int seats;
	private String source;
	private String medium;
	private String campaign;
	public DateTime getExpirationDate() {
		return expiration_date;
	}
	public void setExpirationDate(DateTime expiration_date) {
		this.expiration_date = expiration_date;
	}
	public PrimaryAccountHttpBean getPrimaryAccount() {
		return primary_account;
	}
	public void setPrimaryAccount(PrimaryAccountHttpBean primary_account) {
		this.primary_account = primary_account;
	}
	public SubscriptionLevelHttpBean getSubscriptionLevel() {
		return subscription_level;
	}
	public void setSubscriptionLevel(SubscriptionLevelHttpBean subscription_level) {
		this.subscription_level = subscription_level;
	}
	public DateTime getNextValidationDate() {
		return next_validation_date;
	}
	public void setNextValidationDate(DateTime next_validation_date) {
		this.next_validation_date = next_validation_date;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public ProviderHttpBean getProvider() {
		return provider;
	}
	public void setProvider(ProviderHttpBean provider) {
		this.provider = provider;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	
	
}
