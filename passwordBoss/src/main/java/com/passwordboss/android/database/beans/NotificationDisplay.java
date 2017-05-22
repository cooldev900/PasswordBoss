package com.passwordboss.android.database.beans;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.NOTIFICATION_DISPLAY_TABLE_NAME)
public class NotificationDisplay extends Base {

	@DatabaseField(
			columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING)
	private String name;

	@DatabaseField(
			columnName = DatabaseConstants.DESCRIPTION, 
			dataType = DataType.STRING)
	private String description;

	public NotificationDisplay() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
