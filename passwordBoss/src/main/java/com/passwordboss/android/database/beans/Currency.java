package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.CURRENCY_TABLE_NAME)
public class Currency {
	@DatabaseField(columnName = DatabaseConstants.UUID, 
			dataType = DataType.STRING)
	private String uuid;
	
	@DatabaseField(columnName = DatabaseConstants.CODE, 
			dataType = DataType.STRING, id = true)
	private String code;
	
	@DatabaseField(columnName = DatabaseConstants.SYMBOL, 
			dataType = DataType.STRING, canBeNull = false)
	private String symbol;
	
	@DatabaseField(columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING, canBeNull = false)
	private String name;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String last_modified_date;
	
	@DatabaseField(columnName = DatabaseConstants.ACTIVE, 
			dataType = DataType.BOOLEAN, defaultValue = "true")
	private boolean active;
	
	
	public Currency() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
