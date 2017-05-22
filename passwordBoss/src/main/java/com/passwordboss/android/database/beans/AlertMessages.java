package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.ALERT_MESSAGES_TABLE_NAME)
public class AlertMessages extends Base {

	@DatabaseField(
			columnName = DatabaseConstants.HEADLINE, 
			dataType = DataType.STRING)
	private String headline;
	
	@DatabaseField(
			columnName = DatabaseConstants.ICON_TYPE, 
			dataType = DataType.STRING)
	private String icon_type;
	
	@DatabaseField(
			columnName = DatabaseConstants.MESSAGE, 
			dataType = DataType.STRING)
	private String message;
	
	@DatabaseField(
			columnName = DatabaseConstants.MESSAGE_TYPE, 
			dataType = DataType.STRING)
	private String message_type;
	
	@DatabaseField(
			columnName = DatabaseConstants.STATUS, 
			dataType = DataType.STRING)
	private String status;
	
	@DatabaseField(
			columnName = DatabaseConstants.URL, 
			dataType = DataType.STRING)
	private String url;

	@DatabaseField(
			columnName = DatabaseConstants.BRAND_UUID, 
			dataType = DataType.STRING)
	private String brand_uuid;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String published_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String message_seen_date;

	public AlertMessages() {}
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getIconType() {
		return icon_type;
	}

	public void setIconType(String icon_type) {
		this.icon_type = icon_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageType() {
		return message_type;
	}

	public void setMessageType(String message_type) {
		this.message_type = message_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBrandUuid() {
		return brand_uuid;
	}

	public void setBrandUuid(String brand_uuid) {
		this.brand_uuid = brand_uuid;
	}

	public String getPublishedDate() {
		return published_date;
	}

	public void setPublishedDate(String published_date) {
		this.published_date = published_date;
	}

	public String getMessageSeenDate() {
		return message_seen_date;
	}

	public void setMessageSeenDate(String message_seen_date) {
		this.message_seen_date = message_seen_date;
	}
	
	
}
