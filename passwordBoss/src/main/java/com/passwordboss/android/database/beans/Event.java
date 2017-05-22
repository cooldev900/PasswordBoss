package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.EVENTS_TABLE_NAME)
public class Event extends Base {

	@DatabaseField(
			columnName = DatabaseConstants.EVENT_DEF_ID, 
			dataType = DataType.STRING)
	private String event_def_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.PROPERTY_KEY, 
			dataType = DataType.STRING)
	private String property_key;
	
	@DatabaseField(
			columnName = DatabaseConstants.PROPERTY_VALUE, 
			dataType = DataType.STRING)
	private String property_value;
	
	@DatabaseField(
			columnName = DatabaseConstants.INSTALLATION, 
			dataType = DataType.STRING)
	private String installation_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.ACCOUNT, 
			dataType = DataType.STRING)
	private String account;

	@DatabaseField(
			columnName = DatabaseConstants.PROCESSED, 
			dataType = DataType.STRING)
	private String processed;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String last_modified_date;

	public String getEventDefId() {
		return event_def_id;
	}

	public void setEventDefId(String event_def_id) {
		this.event_def_id = event_def_id;
	}

	public String getPropertyKey() {
		return property_key;
	}

	public void setPropertyKey(String property_key) {
		this.property_key = property_key;
	}

	public String getPropertyValue() {
		return property_value;
	}

	public void setPropertyValue(String property_value) {
		this.property_value = property_value;
	}

	public String getInstallationId() {
		return installation_id;
	}

	public void setInstallationId(String installation_id) {
		this.installation_id = installation_id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
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
