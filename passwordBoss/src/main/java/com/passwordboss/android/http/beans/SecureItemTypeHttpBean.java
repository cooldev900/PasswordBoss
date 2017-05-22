package com.passwordboss.android.http.beans;

public class SecureItemTypeHttpBean  extends BaseHttpBean {
	private String storage_format, data;

	public String getStorageFormat() {
		return storage_format;
	}

	public void setStorageFormat(String storage_format) {
		this.storage_format = storage_format;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
