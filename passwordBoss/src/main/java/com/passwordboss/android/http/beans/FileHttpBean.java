package com.passwordboss.android.http.beans;

import com.passwordboss.android.database.beans.StorageRegion;

public class FileHttpBean extends BaseHttpBean {

	private String hash;
	private int master_password_version;
	private String download_url;
	private String checksum;
	
	private StorageRegion storage_region;
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public int getMasterPasswordVersion() {
		return master_password_version;
	}
	public void setMasterPasswordVersion(int masterPasswordVersion) {
		this.master_password_version = masterPasswordVersion;
	}
	public String getDownloadUrl() {
		return download_url;
	}
	public void setDownloadUrl(String storageUrl) {
		this.download_url = storageUrl;
	}
	public StorageRegion getStorageRegion() {
		return storage_region;
	}
	public void setStorageRegion(StorageRegion storageRegion) {
		this.storage_region = storageRegion;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
}
