package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.SITE_DOMAINS_TABLE_NAME)
public class SiteDomain {
	@DatabaseField(columnName = DatabaseConstants.UUID, 
			dataType = DataType.STRING, canBeNull=false)
	private String uuid;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String last_modified_date;
	
	@DatabaseField(columnName = DatabaseConstants.ACTIVE, 
			dataType = DataType.BOOLEAN, defaultValue = "true")
	private boolean active;
	
	@DatabaseField(columnName = DatabaseConstants.DOMAIN, 
			dataType = DataType.STRING, id=true)
	private String domain;

	
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	
}
