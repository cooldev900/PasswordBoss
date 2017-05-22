package com.passwordboss.android.beans;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.net.URI;

public class ListingBean implements Serializable, Comparable<ListingBean>{
	
	private String itemUuid;
	private String displayName, displayName2, siteUrlList, siteUrlTile, siteUri;
	private boolean favorite, shared;
	private boolean isSeparator;
	private String separatorName;
	private String categoryName;
	private String uuidSiteImageSize;
	private String uuidSiteImage;
	private String uuidSite;
	private String type;
	private String creditCardType;
	private int color;
	private String lastModificationDate;
	private String lastAccess;
	private Drawable icon;
	
	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public ListingBean() {
	}

	public ListingBean(String type) {
		this.type = type;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getUuidSite() {
		return uuidSite;
	}

	public void setUuidSite(String uuidSite) {
		this.uuidSite = uuidSite;
	}

	public String getDisplayName() {
	    // for URLs, cut some parts
		 
	    if(displayName!=null && displayName.matches("(http:|https:).*")) {
	        try {
	            URI uri = new URI(displayName);
	            return uri.getHost();
	        } catch (Exception ex) {
	            return displayName;
	        }	        
	    }
	    return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSiteUrlTile() {
		return siteUrlTile;
	}

	public void setSiteUrlTile(String siteUrlTile) {
		this.siteUrlTile = siteUrlTile;
	}
	
	public String getSiteUrlList() {
		return siteUrlList;
	}

	public void setSiteUrlList(String siteUrlList) {
		this.siteUrlList = siteUrlList;
	}

	public String getItemUuid() {
		return itemUuid;
	}

	public void setItemUuid(String itemUuid) {
		this.itemUuid = itemUuid;
	}

	public boolean isSeparator() {
		return isSeparator;
	}

	public void setSeparator(boolean isSeparator) {
		this.isSeparator = isSeparator;
	}

	public String getSeparatorName() {
		return separatorName;
	}

	public void setSeparatorName(String separatorName) {
		this.separatorName = separatorName;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public void setSiteUri(String siteUri) {
		this.siteUri = siteUri;
	}
	
	public String getSiteUri() {
		return this.siteUri;
	}

	public String getUuidSiteImageSize() {
		return uuidSiteImageSize;
	}

	public void setUuidSiteImageSize(String uuidSiteImageSize) {
		this.uuidSiteImageSize = uuidSiteImageSize;
	}

	public String getUuidSiteImage() {
		return uuidSiteImage;
	}

	public void setUuidSiteImage(String uuidSiteImage) {
		this.uuidSiteImage = uuidSiteImage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDisplayName2() {
		return displayName2;
	}

	public void setDisplayName2(String displayName2) {
		this.displayName2 = displayName2;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public String getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(String lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(String lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	@Override
	public int compareTo(ListingBean another) {
		int result = 0;
		try {
			if (getDisplayName() != null && another != null) {
				result = getDisplayName().compareToIgnoreCase(another.getDisplayName());
			}
		} catch (Exception e) {
			////Log.print(e);
		}
		return result;
	}

	@Override
	public String toString() {
		return "ListingBean [itemUuid=" + itemUuid + ", displayName="
				+ displayName + ", displayName2=" + displayName2
				+ ", siteUrlList=" + siteUrlList + ", siteUrlTile="
				+ siteUrlTile + ", siteUri=" + siteUri + ", favorite="
				+ favorite + ", shared=" + shared + ", isSeparator="
				+ isSeparator + ", separatorName=" + separatorName
				+ ", categoryName=" + categoryName + ", uuidSiteImageSize="
				+ uuidSiteImageSize + ", uuidSiteImage=" + uuidSiteImage
				+ ", uuidSite=" + uuidSite + ", type=" + type
				+ ", creditCardType=" + creditCardType + "]";
	}
}
