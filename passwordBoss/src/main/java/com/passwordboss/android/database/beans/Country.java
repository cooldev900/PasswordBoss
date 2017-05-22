package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.COUNTRY_TABLE_NAME)
public class Country extends Base {

	@DatabaseField(columnName = DatabaseConstants.NAME,
			dataType = DataType.STRING)
	private String name;

	@DatabaseField(columnName = DatabaseConstants.COUNTRY_SYMBOL,
			dataType = DataType.STRING)
	private String currency;

	@DatabaseField(columnName = DatabaseConstants.DIALING_CODE,
			dataType = DataType.STRING)
	private String dialing_code;

	@DatabaseField(columnName = DatabaseConstants.CODE,
			dataType = DataType.STRING, id = true)
	private String code;


	@DatabaseField(columnName = DatabaseConstants.LANGUAGE,
			foreign = true, foreignAutoRefresh = true,
			foreignColumnName = DatabaseConstants.CODE)
	private Language language_code;



	public Country() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Language getLanguageCode() {
		return language_code;
	}

	public void setLanguageCode(Language languageCode) {
		this.language_code = languageCode;
	}

	public String getDialingCode() {
		return dialing_code;
	}

	public void setDialingCode(String dialing_code) {
		this.dialing_code = dialing_code;
	}

	@Override
	public String toString() {
		return getName();
	}
}
