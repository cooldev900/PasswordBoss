package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.SB_BOOKMARKS_TABLE_NAME)
public class Bookmark extends Base {

	@DatabaseField(
			columnName = DatabaseConstants.NAME,
			dataType = DataType.STRING)
	private String name;
	
	@DatabaseField(
			columnName = DatabaseConstants.SB_BOOKMARKS_FAVORITES,
			dataType = DataType.BOOLEAN)
	private boolean is_favorite;
	
	@DatabaseField(
			columnName = DatabaseConstants.SB_BOOKMARKS_SITE_URL, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.ID)
	private SiteUri site_url;
	
	@DatabaseField(
			columnName = DatabaseConstants.SB_BOOKMARKS_ITEM_TYPE, 
			dataType = DataType.STRING)
	private String item_type;
	
	public Bookmark() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIsFavorite() {
		return is_favorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.is_favorite = isFavorite;
	}

	public SiteUri getSiteUrl() {
		return site_url;
	}

	public void setSiteUrl(SiteUri siteUrl) {
		this.site_url = siteUrl;
	}

	public String getItemType() {
		return item_type;
	}

	public void setItemType(String itemType) {
		this.item_type = itemType;
	}


}
