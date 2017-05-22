package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.LANGUAGE_TABLE_NAME)
public class Language extends Base {
	
	@DatabaseField(columnName = DatabaseConstants.CODE, 
			dataType = DataType.STRING, id = true)
	private String code;
	
	@DatabaseField(columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING)
	private String name;
	
	@DatabaseField(columnName = DatabaseConstants.LANGUAGE_TRANSLATED_NAME, 
			dataType = DataType.STRING)
	private String translated_name;
	
	@DatabaseField(columnName = DatabaseConstants.LANGUAGE_TRANSLATED, 
			dataType = DataType.BOOLEAN)
	private boolean translated;

	public Language() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTranslatedName() {
		return translated_name;
	}

	public void setTranslatedName(String translatedName) {
		this.translated_name = translatedName;
	}

	public boolean isTranslated() {
		return translated;
	}

	public void setTranslated(boolean translated) {
		this.translated = translated;
	}
	
	
	
}
