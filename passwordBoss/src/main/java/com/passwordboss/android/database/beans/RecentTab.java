package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.SB_BOOKMARKS_TABLE_NAME)
public class RecentTab extends Base {

	@DatabaseField(columnName = DatabaseConstants.URL, dataType = DataType.STRING)
	private String url;
	@DatabaseField(columnName = DatabaseConstants.RECENTSTABS_WEBSITENAME, dataType = DataType.STRING)
	private String website_name;
	@DatabaseField(columnName = DatabaseConstants.RECENTSTABS_IMAGE, dataType = DataType.LONG_STRING)
	private String image;

	public RecentTab() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebsiteName() {
		return website_name;
	}

	public void setWebsiteName(String website_name) {
		this.website_name = website_name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
