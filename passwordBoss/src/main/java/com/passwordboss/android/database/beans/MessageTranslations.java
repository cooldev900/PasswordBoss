package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.MESSAGE_TRANSLATIONS_TABLE_NAME)
public class MessageTranslations {

	@DatabaseField(
			columnName = DatabaseConstants.MSG_ID, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.MSG_ID)
	private MessageDefinitions msg_id;
	
	
	@DatabaseField(columnName = DatabaseConstants.LANG,
			dataType = DataType.STRING,  canBeNull = false)
	private String lang;
	
	@DatabaseField(columnName = DatabaseConstants.TITLE,
			dataType = DataType.STRING)
	private String title;
	
	@DatabaseField(columnName = DatabaseConstants.TITLE1,
			dataType = DataType.STRING)
	private String title1;
	
	@DatabaseField(columnName = DatabaseConstants.TITLE2,
			dataType = DataType.STRING)
	private String title2;
	
	@DatabaseField(columnName = DatabaseConstants.BODY,
			dataType = DataType.STRING)
	private String body;
	
	@DatabaseField(columnName = DatabaseConstants.BODY1,
			dataType = DataType.STRING)
	private String body1;
	
	@DatabaseField(columnName = DatabaseConstants.BODY2,
			dataType = DataType.STRING)
	private String body2;
	
	@DatabaseField(columnName = DatabaseConstants.CONFIG_ACTION_TEXT,
			dataType = DataType.STRING)
	private String confirm_action_text;
	
	@DatabaseField(columnName = DatabaseConstants.CANCEL_ACTION_TEXT,
			dataType = DataType.STRING)
	private String cancel_action_text;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String last_modified_date;

	public MessageDefinitions getMsgId() {
		return msg_id;
	}

	public void setMsgId(MessageDefinitions msg_id) {
		this.msg_id = msg_id;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public String getConfirmActionText() {
		return confirm_action_text;
	}

	public void setConfirmActionText(String confirm_action_text) {
		this.confirm_action_text = confirm_action_text;
	}

	public String getCancelActionText() {
		return cancel_action_text;
	}

	public void setCancelActionText(String cancel_action_text) {
		this.cancel_action_text = cancel_action_text;
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
	
	
	
}
