package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = DatabaseConstants.FAVORITE_TABLE_NAME)
public class Favorite extends Base {
	
	@DatabaseField(
			columnName = DatabaseConstants.URL,
			dataType = DataType.STRING, id = true)
	private String url;
	
	@DatabaseField(
			columnName = DatabaseConstants.NAME,
			dataType = DataType.STRING)
	private String name;
	
	@DatabaseField(
			columnName = DatabaseConstants.HASH,
			dataType = DataType.STRING)
	private String hash;
	
	
	public Favorite() {
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void calculateHash() {
		String data = "";
		data += getUrl() + getName() + getUuid() + getOrder() + getActive();
		setHash(Utils.getMD5(data));
	}
	
	
}
