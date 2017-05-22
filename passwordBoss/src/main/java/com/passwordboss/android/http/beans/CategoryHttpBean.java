package com.passwordboss.android.http.beans;

public class CategoryHttpBean extends BaseHttpBean{
	private SecureItemTypeHttpBean	secure_item_type;
	private String parent;
	
	
	public SecureItemTypeHttpBean getSecureItemType() {
		return secure_item_type;
	}
	public void setSecureItemType(SecureItemTypeHttpBean secureItemType) {
		this.secure_item_type = secureItemType;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}

}
