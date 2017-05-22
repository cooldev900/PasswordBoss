package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.ACTION_TABLE_NAME)
public class Action extends Base {

	@DatabaseField(
			columnName = DatabaseConstants.ID,
			dataType = DataType.STRING, id = true)
	private String id;
	
	@DatabaseField(
			columnName = DatabaseConstants.ACTION_START_DATE,
			dataType = DataType.STRING)
	private String start_date;
	
	@DatabaseField(
			columnName = DatabaseConstants.ACTION_COMPLETED_DATE, 
			dataType = DataType.STRING)
	private String completed_date;
	
	@DatabaseField(
			columnName = DatabaseConstants.ACCOUNT_EMAIL, 
			dataType = DataType.STRING)
	private String account_e_mail;
	
	@DatabaseField(
			columnName = DatabaseConstants.STATUS, 
			dataType = DataType.STRING)
	private String status;
	
	@DatabaseField(
			columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING)
	private String name;
	
	@DatabaseField(
			columnName = DatabaseConstants.ACTION_TYPE, 
			dataType = DataType.STRING)
	private String action_type;
	
	@DatabaseField(
			columnName = DatabaseConstants.ACTION_CONFIGURATION, 
			dataType = DataType.STRING)
	private String configuration;
	
	public Action() {
		
	}
	
	public String getActionType() {
		return action_type;
	}
	
	public void setActionType(String action_type) {
		this.action_type = action_type;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartDate() {
		return start_date;
	}

	public void setStartDate(String startDate) {
		this.start_date = startDate;
	}

	public String getCompletedDate() {
		return completed_date;
	}

	public void setCompletedDate(String completed_date) {
		this.completed_date = completed_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getAccountEmail() {
		return account_e_mail;
	}

	public void setAccountEmail(String accountEmail) {
		this.account_e_mail = accountEmail;
	}

	
	
	

}
