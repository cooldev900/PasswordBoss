package com.passwordboss.android.http.beans;

public class MessageDefinitionsHttpBean extends BaseHttpBean {
	private CampaignHttpBean campaign;
	private ConfirmActionHttpBean cancel_action_id, cancel_action_text, confirm_action_id, confirm_action_text,
	display_subsequent, initial_delay, timer, type, skip_show_if_late;
	private ImageSizeHttpBean size;
	private boolean account_status, show_reminder;
	private String body1, body2, body3, bullet_list, identifier, implementation, reminder_text, template, title1,
	title2, title3, icon_type, subscription_type;
	private int max_display_count ;
	
	public CampaignHttpBean getCampaign() {
		return campaign;
	}
	public void setCampaign(CampaignHttpBean campaign) {
		this.campaign = campaign;
	}
	public ConfirmActionHttpBean getCancelActionId() {
		return cancel_action_id;
	}
	public void setCancelActionId(ConfirmActionHttpBean cancelActionId) {
		this.cancel_action_id = cancelActionId;
	}
	public ConfirmActionHttpBean getCancelActionText() {
		return cancel_action_text;
	}
	public void setCancelActionText(ConfirmActionHttpBean cancelActionText) {
		this.cancel_action_text = cancelActionText;
	}
	public ConfirmActionHttpBean getConfirmActionId() {
		return confirm_action_id;
	}
	public void setConfirmActionId(ConfirmActionHttpBean confirm_action_id) {
		this.confirm_action_id = confirm_action_id;
	}
	public ConfirmActionHttpBean getConfirmActionText() {
		return confirm_action_text;
	}
	public void setConfirmActionText(ConfirmActionHttpBean confirmActionText) {
		this.confirm_action_text = confirmActionText;
	}
	public ConfirmActionHttpBean getDisplaySubsequent() {
		return display_subsequent;
	}
	public void setDisplaySubsequent(ConfirmActionHttpBean displaySubsequent) {
		this.display_subsequent = displaySubsequent;
	}
	public ConfirmActionHttpBean getInitialDelay() {
		return initial_delay;
	}
	public void setInitialDelay(ConfirmActionHttpBean initialDelay) {
		this.initial_delay = initialDelay;
	}
	public ConfirmActionHttpBean getTimer() {
		return timer;
	}
	public void setTimer(ConfirmActionHttpBean timer) {
		this.timer = timer;
	}
	public ConfirmActionHttpBean getType() {
		return type;
	}
	public void setType(ConfirmActionHttpBean type) {
		this.type = type;
	}
	public String getSubscriptionType() {
		return subscription_type;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscription_type = subscriptionType;
	}
	public ImageSizeHttpBean getSize() {
		return size;
	}
	public void setSize(ImageSizeHttpBean size) {
		this.size = size;
	}
	public boolean isAccountStatus() {
		return account_status;
	}
	public void setAccountStatus(boolean accountStatus) {
		this.account_status = accountStatus;
	}
	public boolean isShowReminder() {
		return show_reminder;
	}
	public void setShowReminder(boolean showReminder) {
		this.show_reminder = showReminder;
	}
	public String getBody1() {
		return body1;
	}
	public void setBody1(String body1) {
		this.body1 = body1;
	}
	public String getBody2() {
		return body2;
	}
	public void setBody2(String body2) {
		this.body2 = body2;
	}
	public String getBody3() {
		return body3;
	}
	public void setBody3(String body3) {
		this.body3 = body3;
	}
	public String getBulletList() {
		return bullet_list;
	}
	public void setBulletList(String bulletList) {
		this.bullet_list = bulletList;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getImplementation() {
		return implementation;
	}
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}
	public String getReminderText() {
		return reminder_text;
	}
	public void setReminderText(String reminderText) {
		this.reminder_text = reminderText;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getTitle1() {
		return title1;
	}
	public void setTitle1(String title1) {
		this.title1 = title1;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getTitle3() {
		return title3;
	}
	public void setTitle3(String title3) {
		this.title3 = title3;
	}
	public String getIconType() {
		return icon_type;
	}
	public void setIconType(String iconType) {
		this.icon_type = iconType;
	}
	public ConfirmActionHttpBean getSkipShowIfLate() {
		return skip_show_if_late;
	}
	public void setSkipShowIfLate(ConfirmActionHttpBean skipShowIfLate) {
		this.skip_show_if_late = skipShowIfLate;
	}
	public int getMaxDisplayCount() {
		return max_display_count;
	}
	public void setMaxDisplayCount(int maxDisplayCount) {
		this.max_display_count = maxDisplayCount;
	}
	
}
