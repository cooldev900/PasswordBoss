package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.CHANNEL_TABLE_NAME)
public class Channel extends Base{

	@DatabaseField(
			columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING
	)
	private String name;
	
	@DatabaseField(
			columnName = DatabaseConstants.CHANNEL_NOTES, 
			dataType = DataType.STRING
	)
	private String notes;
	
	@DatabaseField(
			columnName = DatabaseConstants.CHANNEL_PARTNER, 
			dataType = DataType.INTEGER
	)
	private int partner;
	
	@DatabaseField(
			columnName = DatabaseConstants.CHANNEL_ID, 
			dataType = DataType.INTEGER
	)
	private int channel_id;
	
	public Channel() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getPartner() {
		return partner;
	}

	public void setPartner(int partner) {
		this.partner = partner;
	}

	public int getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
}
