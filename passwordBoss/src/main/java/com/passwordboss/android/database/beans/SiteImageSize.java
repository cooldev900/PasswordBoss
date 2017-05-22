package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = DatabaseConstants.SITE_IMAGE_SIZE_TABLE_NAME)
public class SiteImageSize {
	
	@DatabaseField(columnName = DatabaseConstants.ID, 
			dataType = DataType.STRING, id = true)
	private String id;
	
	@DatabaseField(columnName = DatabaseConstants.UUID, 
			dataType = DataType.STRING)
	private String uuid;

	@DatabaseField(
			columnName = DatabaseConstants.HASH, 
			dataType = DataType.STRING)
	private String hash;

	@DatabaseField(
			columnName = DatabaseConstants.NAME, 
			dataType = DataType.STRING)
	private String name;
	
	@DatabaseField(
			columnName = DatabaseConstants.SITE_IMAGE_SIZE_WIDHT, 
			dataType = DataType.INTEGER)
	private int width;
	
	@DatabaseField(
			columnName = DatabaseConstants.SITE_IMAGE_SIZE_HEIGHT, 
			dataType = DataType.INTEGER)
	private int height;

	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String last_modified_date;
	
	@DatabaseField(columnName = DatabaseConstants.ACTIVE, 
			dataType = DataType.BOOLEAN, defaultValue = "true")
	private boolean active;
	
	public SiteImageSize() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
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

	public void setCreatedDate(String createdDate) {
		this.created_date = createdDate;
	}

	public String getLastModifiedDate() {
		return last_modified_date;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.last_modified_date = lastModifiedDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void calculateHash() {
		String data = "";
		data += getUuid() + getName() + getHeight() + getWidth() + isActive();
		setHash(Utils.getMD5(data));
	}
}
