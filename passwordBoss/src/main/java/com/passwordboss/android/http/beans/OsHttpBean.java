package com.passwordboss.android.http.beans;

public class OsHttpBean extends BaseHttpBean{

	private String architecture, description, major_version, minor_version;

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMajor_version() {
		return major_version;
	}

	public void setMajor_version(String major_version) {
		this.major_version = major_version;
	}

	public String getMinor_version() {
		return minor_version;
	}

	public void setMinor_version(String minor_version) {
		this.minor_version = minor_version;
	}
}
