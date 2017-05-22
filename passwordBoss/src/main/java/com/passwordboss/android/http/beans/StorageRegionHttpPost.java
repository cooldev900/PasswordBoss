package com.passwordboss.android.http.beans;

public class StorageRegionHttpPost {
	private String storage_region, email, installation;
	private boolean synchronize;

	
	public StorageRegionHttpPost() {
		super();
	}

	public StorageRegionHttpPost(String storage_region, String email, String installation) {
		super();
		this.storage_region = storage_region;
		this.email = email;
		this.installation = installation;
	}
	
	public StorageRegionHttpPost(boolean synchronize, String storage_region, String email, String installation) {
		super();
		this.storage_region = storage_region;
		this.email = email;
		this.installation = installation;
		this.synchronize = synchronize;
	}

	public StorageRegionHttpPost(boolean synchronize, String email, String installation) {
		super();
		this.synchronize = synchronize;
		this.email = email;
		this.installation = installation;
	}
	
	public String getStorageRegion() {
		return storage_region;
	}

	public void setStorageRegion(String storage_region) {
		this.storage_region = storage_region;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	public boolean isSynchronize() {
		return synchronize;
	}

	public void setSynchronize(boolean synchronize) {
		this.synchronize = synchronize;
	}
	
}
