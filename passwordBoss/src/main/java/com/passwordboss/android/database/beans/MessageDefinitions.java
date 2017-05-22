package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.MESSAGE_DEFINITIONS_TABLE_NAME)
public class MessageDefinitions {

	@DatabaseField(
			columnName = DatabaseConstants.MSG_ID,
			dataType = DataType.STRING, id = true, canBeNull = false)
	private String msg_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.MSG_TYPE,
			dataType = DataType.STRING, canBeNull = false)
	private String msg_type;
	
	@DatabaseField(
			columnName = DatabaseConstants.THEME,
			dataType = DataType.STRING, canBeNull = false)
	private String theme;
	
	@DatabaseField(
			columnName = DatabaseConstants.ICON_TYPE,
			dataType = DataType.STRING)
	private String icon_type;
	
	@DatabaseField(
			columnName = DatabaseConstants.CONFIRM_ACTION,
			dataType = DataType.STRING)
	private String confirm_action;
	
	@DatabaseField(
			columnName = DatabaseConstants.CANCEL_ACTION,
			dataType = DataType.STRING)
	private String cancel_action;
	
	@DatabaseField(columnName = DatabaseConstants.SHOW_REMINDER, 
			dataType = DataType.BOOLEAN, defaultValue = "false", canBeNull = false)
	private boolean show_reminder;
	
	@DatabaseField(columnName = DatabaseConstants.ACCOUNT_STATUS, 
			dataType = DataType.BOOLEAN, canBeNull = false)
	private boolean account_status;
	
	@DatabaseField(
			columnName = DatabaseConstants.SUBSCRIPTION_LEVEL,
			dataType = DataType.STRING, canBeNull = false)
	private String subscription_level;
	
	@DatabaseField(
			columnName = DatabaseConstants.START_TIMER_ON,
			dataType = DataType.STRING, canBeNull = false)
	private String start_timer_on;
	
	@DatabaseField(columnName = DatabaseConstants.START_DELAY_MINUTES, 
			dataType = DataType.INTEGER, defaultValue = "0", canBeNull = false)
	private int start_delay_minutes;
	
	@DatabaseField(columnName = DatabaseConstants.REPEAT_DELAY_MINUTES, 
			dataType = DataType.INTEGER, defaultValue = "0", canBeNull = false)
	private int repeat_delay_minutes;
	
	@DatabaseField(columnName = DatabaseConstants.SKIP_SHOW_IF_LATE_MINUTES, 
			dataType = DataType.INTEGER, defaultValue = "0", canBeNull = false)
	private int skip_show_if_late_minutes;
	
	@DatabaseField(columnName = DatabaseConstants.MAX_REP_CNT, 
			dataType = DataType.INTEGER, defaultValue = "0", canBeNull = false)
	private int max_rep_cnt;
	
	@DatabaseField(
			columnName = DatabaseConstants.EVENT_ID,
			dataType = DataType.STRING)
	private String event_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.EVENT_PROPERTY,
			dataType = DataType.STRING)
	private String event_property;
	
	@DatabaseField(
			columnName = DatabaseConstants.EVENT_VALUE,
			dataType = DataType.STRING)
	private String event_value;
	
	@DatabaseField(columnName = DatabaseConstants.SHOW_ON_NO_EVENT, 
			dataType = DataType.BOOLEAN, defaultValue = "true", canBeNull = false)
	private boolean show_on_no_event;
	
	@DatabaseField(columnName = DatabaseConstants.SHOW_ON_NO_EVENT_DELAY_MINUTES, 
			dataType = DataType.INTEGER, defaultValue = "0", canBeNull = false)
	private int show_on_no_event_delay_minutes;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String last_modified_date;
	
	@DatabaseField(columnName = DatabaseConstants.ACTIVE, 
			dataType = DataType.BOOLEAN, defaultValue = "true", canBeNull = false)
	private boolean active;

	public String getMsgId() {
		return msg_id;
	}

	public void setMsgId(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsgType() {
		return msg_type;
	}

	public void setMsgType(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getIconType() {
		return icon_type;
	}

	public void setIconType(String icon_type) {
		this.icon_type = icon_type;
	}

	public String getConfirmAction() {
		return confirm_action;
	}

	public void setConfirmAction(String confirm_action) {
		this.confirm_action = confirm_action;
	}

	public String getCancelAction() {
		return cancel_action;
	}

	public void setCancelAction(String cancel_action) {
		this.cancel_action = cancel_action;
	}

	public boolean isShowReminder() {
		return show_reminder;
	}

	public void setShowReminder(boolean show_reminder) {
		this.show_reminder = show_reminder;
	}

	public boolean isAccountStatus() {
		return account_status;
	}

	public void setAccountStatus(boolean account_status) {
		this.account_status = account_status;
	}

	public String getSubscriptionLevel() {
		return subscription_level;
	}

	public void setSubscriptionLevel(String subscription_level) {
		this.subscription_level = subscription_level;
	}

	public String getStartTimerOn() {
		return start_timer_on;
	}

	public void setStartTimerOn(String start_timer_on) {
		this.start_timer_on = start_timer_on;
	}

	public int getStartDelayMinutes() {
		return start_delay_minutes;
	}

	public void setStartDelayMinutes(int start_delay_minutes) {
		this.start_delay_minutes = start_delay_minutes;
	}

	public int getRepeatDelayMinutes() {
		return repeat_delay_minutes;
	}

	public void setRepeatDelayMinutes(int repeat_delay_minutes) {
		this.repeat_delay_minutes = repeat_delay_minutes;
	}

	public int getSkipShowIfLateMinutes() {
		return skip_show_if_late_minutes;
	}

	public void setSkipShowIfLateMinutes(int skip_show_if_late_minutes) {
		this.skip_show_if_late_minutes = skip_show_if_late_minutes;
	}

	public int getMaxRepCnt() {
		return max_rep_cnt;
	}

	public void setMaxRepCnt(int max_rep_cnt) {
		this.max_rep_cnt = max_rep_cnt;
	}

	public String getEventId() {
		return event_id;
	}

	public void setEventId(String event_id) {
		this.event_id = event_id;
	}

	public String getEventProperty() {
		return event_property;
	}

	public void setEventProperty(String event_property) {
		this.event_property = event_property;
	}

	public String getEventValue() {
		return event_value;
	}

	public void setEventValue(String event_value) {
		this.event_value = event_value;
	}

	public boolean isShowOnNoEvent() {
		return show_on_no_event;
	}

	public void setShowOnNoEvent(boolean show_on_no_event) {
		this.show_on_no_event = show_on_no_event;
	}

	public int getShowOnNoEventDelayMinutes() {
		return show_on_no_event_delay_minutes;
	}

	public void setShowOnNoEventDelayMinutes(int show_on_no_event_delay_minutes) {
		this.show_on_no_event_delay_minutes = show_on_no_event_delay_minutes;
	}

	public String getCreatedDate() {
		return created_date;
	}

	public void setCreatedDate(String created_date) {
		this.created_date = created_date;
	}

	public String getLastModifiedDate() {
		return last_modified_date;
	}

	public void setLastModifiedDate(String last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}	
}
