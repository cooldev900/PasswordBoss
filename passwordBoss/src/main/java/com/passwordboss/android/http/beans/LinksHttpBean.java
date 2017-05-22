package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class LinksHttpBean implements Serializable {

	private String account, account_site, action, campaign, category, channel,
			configuration, country, country_group, country_group_country,
			country_group_detail, currency, device, device_category, device_os,
			device_type, discount, event, favorite, feature, feature_group,
			feature_group_feature, heartbeat, installation, invitation,
			language, medium, notification, notification_category,
			notification_display, partner, recommended_site, secure_file,
			secure_item_type, share, site, site_field, site_image,
			site_image_size, site_uri, source, storage, subscription,
			subscription_level, subscription_level_feature_group,
			subscription_level_pricing, subscription_payment, sync, trigger,
			verification, subscription_validate_android, account_2step,
			sms;

	@Override
	public String toString() {
		return "LinksHttpBean [account=" + account + ", account_site="
				+ account_site + ", action=" + action + ", campaign="
				+ campaign + ", category=" + category + ", channel=" + channel
				+ ", configuration=" + configuration + ", country=" + country
				+ ", country_group=" + country_group
				+ ", country_group_country=" + country_group_country
				+ ", country_group_detail=" + country_group_detail
				+ ", currency=" + currency + ", device=" + device
				+ ", device_category=" + device_category + ", device_os="
				+ device_os + ", device_type=" + device_type + ", discount="
				+ discount + ", event=" + event + ", favorite=" + favorite
				+ ", feature=" + feature + ", feature_group=" + feature_group
				+ ", feature_group_feature=" + feature_group_feature
				+ ", heartbeat=" + heartbeat + ", installation=" + installation
				+ ", invitation=" + invitation + ", language=" + language
				+ ", medium=" + medium + ", notification=" + notification
				+ ", notification_category=" + notification_category
				+ ", notification_display=" + notification_display
				+ ", partner=" + partner + ", recommended_site="
				+ recommended_site + ", secure_file=" + secure_file
				+ ", secure_item_type=" + secure_item_type + ", share=" + share
				+ ", site=" + site + ", site_field=" + site_field
				+ ", site_image=" + site_image + ", site_image_size="
				+ site_image_size + ", site_uri=" + site_uri + ", source="
				+ source + ", storage=" + storage + ", subscription="
				+ subscription + ", subscription_level=" + subscription_level
				+ ", subscription_level_feature_group="
				+ subscription_level_feature_group
				+ ", subscription_level_pricing=" + subscription_level_pricing
				+ ", subscription_payment=" + subscription_payment + ", sync="
				+ sync + ", trigger=" + trigger + ", verification="
				+ verification + ", subscription_validate_android="
				+ subscription_validate_android + ", account_2step="
				+ account_2step + ", sms=" + getSms() + "]";
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountSite() {
		return account_site;
	}

	public void setAccountSite(String account_site) {
		this.account_site = account_site;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryGroup() {
		return country_group;
	}

	public void setCountryGroup(String country_group) {
		this.country_group = country_group;
	}

	public String getCountryGroupCountry() {
		return country_group_country;
	}

	public void setCountryGroupCountry(String country_group_country) {
		this.country_group_country = country_group_country;
	}

	public String getCountryGroupDetail() {
		return country_group_detail;
	}

	public void setCountryGroupDetail(String country_group_detail) {
		this.country_group_detail = country_group_detail;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDeviceCategory() {
		return device_category;
	}

	public void setDeviceCategory(String device_category) {
		this.device_category = device_category;
	}

	public String getDeviceOs() {
		return device_os;
	}

	public void setDeviceOs(String device_os) {
		this.device_os = device_os;
	}

	public String getDeviceType() {
		return device_type;
	}

	public void setDeviceType(String device_type) {
		this.device_type = device_type;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getFavorite() {
		return favorite;
	}

	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getFeatureGroup() {
		return feature_group;
	}

	public void setFeatureGroup(String feature_group) {
		this.feature_group = feature_group;
	}

	public String getFeatureGroupFeature() {
		return feature_group_feature;
	}

	public void setFeatureGroupFeature(String feature_group_feature) {
		this.feature_group_feature = feature_group_feature;
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	public String getInvitation() {
		return invitation;
	}

	public void setInvitation(String invitation) {
		this.invitation = invitation;
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

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getNotification_category() {
		return notification_category;
	}

	public void setNotificationCategory(String notification_category) {
		this.notification_category = notification_category;
	}

	public String getNotificationDisplay() {
		return notification_display;
	}

	public void setNotificationDisplay(String notification_display) {
		this.notification_display = notification_display;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRecommendedSite() {
		return recommended_site;
	}

	public void setRecommendedSite(String recommended_site) {
		this.recommended_site = recommended_site;
	}

	public String getSecureFile() {
		return secure_file;
	}

	public void setSecureFile(String secure_file) {
		this.secure_file = secure_file;
	}

	public String getSecureItemType() {
		return secure_item_type;
	}

	public void setSecureItemType(String secure_item_type) {
		this.secure_item_type = secure_item_type;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSiteField() {
		return site_field;
	}

	public void setSiteField(String site_field) {
		this.site_field = site_field;
	}

	public String getSiteImage() {
		return site_image;
	}

	public void setSiteImage(String site_image) {
		this.site_image = site_image;
	}

	public String getSiteImageSize() {
		return site_image_size;
	}

	public void setSiteImageSize(String site_image_size) {
		this.site_image_size = site_image_size;
	}

	public String getSiteUri() {
		return site_uri;
	}

	public void setSiteUri(String site_uri) {
		this.site_uri = site_uri;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public String getSubscriptionLevel() {
		return subscription_level;
	}

	public void setSubscriptionLevel(String subscription_level) {
		this.subscription_level = subscription_level;
	}

	public String getSubscriptionLevelFeatureGroup() {
		return subscription_level_feature_group;
	}

	public void setSubscriptionLevelFeatureGroup(
			String subscription_level_feature_group) {
		this.subscription_level_feature_group = subscription_level_feature_group;
	}

	public String getSubscriptionLevelPricing() {
		return subscription_level_pricing;
	}

	public void setSubscriptionLevelPricing(String subscription_level_pricing) {
		this.subscription_level_pricing = subscription_level_pricing;
	}

	public String getSubscriptionPayment() {
		return subscription_payment;
	}

	public void setSubscriptionPayment(String subscription_payment) {
		this.subscription_payment = subscription_payment;
	}

	public String getSync() {
		return sync;
	}

	public void setSync(String sync) {
		this.sync = sync;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getAccount_2step() {
		return account_2step;
	}

	public void setAccount_2step(String account_2step) {
		this.account_2step = account_2step;
	}

	public String getSubscriptionValidateAndroid() {
		return subscription_validate_android;
	}

	public void setSubscriptionValidateAndroid(
			String subscription_validate_android) {
		this.subscription_validate_android = subscription_validate_android;
	}

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
 
}
