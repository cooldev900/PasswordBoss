package com.passwordboss.android.http.beans;

import com.passwordboss.android.beans.SecureItemDataJSON;

public class ShareItemPayload {
	private String id, category_id, hash, name, secure_item_type_name, 
		site_id, uuid, type, created_date, last_modified_date, site_url, login_url;
	private SecureItemDataJSON data;
	private boolean share, favorite, verified;
	private int color, order;
	
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
	public String getSecureItemTypeName() {
		return secure_item_type_name;
	}
	public void setSecureItemTypeName(String secure_item_type_name) {
		this.secure_item_type_name = secure_item_type_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedDate() {
		return created_date;
	}
	public void setCreatedDate(String created_date) {
		this.created_date = created_date;
	}
	public String getLastModifiedDate() {
		return last_modified_date;
	}
	public void setLastModifiedDate(String last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public SecureItemDataJSON getData() {
		return data;
	}
	public void setData(SecureItemDataJSON data) {
		this.data = data;
	}
	public String getCategoryId() {
		return category_id;
	}
	public void setCategoryId(String category_id) {
		this.category_id = category_id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getSiteId() {
		return site_id;
	}
	public void setSiteId(String site_id) {
		this.site_id = site_id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public boolean getFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public boolean getShare() {
		return share;
	}
	public void setShare(boolean share) {
		this.share = share;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean getVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getSiteUrl() {
		return site_url;
	}
	public void setSiteUrl(String siteUrl) {
		this.site_url = siteUrl;
	}
	public String getLoginUrl() {
		return login_url;
	}
	public void setLoginUrl(String login_url) {
		this.login_url = login_url;
	}	
}
