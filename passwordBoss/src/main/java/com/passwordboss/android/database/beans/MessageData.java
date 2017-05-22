package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.MESSAGE_DATA_TABLE_NAME)
public class MessageData {

	@DatabaseField(
			columnName = DatabaseConstants.MSG_ID, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.MSG_ID)
	private MessageDefinitions msg_id;
	
	
	@DatabaseField(
			columnName = DatabaseConstants.ACTION_ID, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.ID)
	private Action action_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.TIMER_DATE,
			dataType = DataType.STRING)
	private String timer_date;
	
	@DatabaseField(
			columnName = DatabaseConstants.LAST_DISPLAY_DATE,
			dataType = DataType.STRING)
	private String last_display_date;
	
	@DatabaseField(
			columnName = DatabaseConstants.DISPlAY_CNT,
			dataType = DataType.STRING)
	private String display_cnt;
	
	@DatabaseField(
			columnName = DatabaseConstants.EVENT_PROPERTY,
			dataType = DataType.STRING)
	private String event_property;
	
	@DatabaseField(
			columnName = DatabaseConstants.EVENT_VALUE,
			dataType = DataType.STRING)
	private String event_value;
	
	@DatabaseField(columnName = DatabaseConstants.COMPLITED, 
			dataType = DataType.BOOLEAN, defaultValue = "false", canBeNull = false)
	private boolean completed;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String last_modified_date;
	
	@DatabaseField(columnName = DatabaseConstants.ACTIVE, 
			dataType = DataType.BOOLEAN, defaultValue = "true", canBeNull = false)
	private boolean active;

	public MessageDefinitions getMsgId() {
		return msg_id;
	}

	public void setMsgId(MessageDefinitions msg_id) {
		this.msg_id = msg_id;
	}

	public Action getActionId() {
		return action_id;
	}

	public void setActionId(Action action_id) {
		this.action_id = action_id;
	}

	public String getTimerDate() {
		return timer_date;
	}

	public void setTimerDate(String timer_date) {
		this.timer_date = timer_date;
	}

	public String getLastDisplayDate() {
		return last_display_date;
	}

	public void setLastDisplayDate(String last_display_date) {
		this.last_display_date = last_display_date;
	}

	public String getDisplayCnt() {
		return display_cnt;
	}

	public void setDisplayCnt(String display_cnt) {
		this.display_cnt = display_cnt;
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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
