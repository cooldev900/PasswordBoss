package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.DATACENTER_TABLE_NAME)
public class DataLocation extends Base{

	@DatabaseField(
			columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING)
	private String name;
	@DatabaseField(
			columnName = DatabaseConstants.DATACENTER_COUNTRY_ID, 
			dataType = DataType.INTEGER)
	private int country_id;
	@DatabaseField(
			columnName = DatabaseConstants.DATACENTER_LANGUAGE_ID, 
			dataType = DataType.INTEGER)
	private int language_id;

	public DataLocation() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCountryId() {
		return country_id;
	}

	public void setCountryId(int country_id) {
		this.country_id = country_id;
	}

	public int getLanguageId() {
		return language_id;
	}

	public void setLanguageId(int language_id) {
		this.language_id = language_id;
	}

}
