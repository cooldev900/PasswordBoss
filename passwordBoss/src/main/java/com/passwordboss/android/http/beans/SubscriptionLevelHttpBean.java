package com.passwordboss.android.http.beans;

public class SubscriptionLevelHttpBean extends BaseHttpBean{
	private int duration, seats;
	private String description, call_to_action, marketing_description, marketing_headline, marketing_promo, 
		subscription_type, user_display_name, internal_name;
	private boolean multiuser;
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isMultiuser() {
		return multiuser;
	}
	public void setMultiuser(boolean multiuser) {
		this.multiuser = multiuser;
	}
	public String getCallToAction() {
		return call_to_action;
	}
	public void setCallToAction(String call_to_action) {
		this.call_to_action = call_to_action;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	
	public String getMarketingDescription() {
		return marketing_description;
	}
	public void setMarketingDescription(String marketing_description) {
		this.marketing_description = marketing_description;
	}
	public String getMarketingHeadline() {
		return marketing_headline;
	}
	public void setMarketingHeadline(String marketing_headline) {
		this.marketing_headline = marketing_headline;
	}
	public String getMarketingPromo() {
		return marketing_promo;
	}
	public void setMarketingPromo(String marketing_promo) {
		this.marketing_promo = marketing_promo;
	}
	public String getSubscriptionType() {
		return subscription_type;
	}
	public void setSubscriptionType(String subscription_type) {
		this.subscription_type = subscription_type;
	}
	public String getUserDisplayName() {
		return user_display_name;
	}
	public void setUserDisplayName(String user_display_name) {
		this.user_display_name = user_display_name;
	}
	public String getInternalName() {
		return internal_name;
	}
	public void setInternalName(String internal_name) {
		this.internal_name = internal_name;
	}
	
}
