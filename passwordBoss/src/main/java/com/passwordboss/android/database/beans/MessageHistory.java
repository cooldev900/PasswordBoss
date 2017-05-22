package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.MESSAGE_HISTORY)
public class MessageHistory {

	@DatabaseField(columnName = "rowid", 
			dataType = DataType.STRING)
	private String rowid;
	
	@DatabaseField(columnName = DatabaseConstants.ID, 
			dataType = DataType.STRING, id = true)
	private String id;

	@DatabaseField(columnName = DatabaseConstants.ANALITICS_CODE, 
			dataType = DataType.STRING)
	private String analytics_code;
	
	@DatabaseField(columnName = DatabaseConstants.MSG_TYPE, 
			dataType = DataType.STRING)
	private String msg_type;
	
	@DatabaseField(columnName = DatabaseConstants.THEME, 
			dataType = DataType.STRING)
	private String theme;

	@DatabaseField(columnName = DatabaseConstants.BUTTON_CLICKED, 
			dataType = DataType.STRING)
	private String button_clicked;
	
	@DatabaseField(columnName = DatabaseConstants.BUY_BUTTON, 
			dataType = DataType.STRING)
	private String buy_button;
	
	@DatabaseField(columnName = DatabaseConstants.DAYS_SINCE_ACCOUNT_CREATED, 
			dataType = DataType.STRING)
	private String days_since_account_created;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String last_modified_date;
	

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnaliticsCode() {
		return analytics_code;
	}

	public void setAnaliticsCode(String analitics_code) {
		this.analytics_code = analitics_code;
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

	public String getButtonClicked() {
		return button_clicked;
	}

	public void setButtonClicked(String button_clicked) {
		this.button_clicked = button_clicked;
	}

	public String getBuyButton() {
		return buy_button;
	}

	public void setBuyButton(String buy_button) {
		this.buy_button = buy_button;
	}

	public String getDaysSinceAccountCreated() {
		return days_since_account_created;
	}

	public void setDaysSinceAccountCreated(String days_since_account_created) {
		this.days_since_account_created = days_since_account_created;
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
