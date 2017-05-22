package com.passwordboss.android.http.beans;

public class FieldHelperBean {
	private String aliases[];
	private String exclusions[];
	
	public String[] getAliases() {
		return aliases;
	}
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
	public String[] getExclusions() {
		return exclusions;
	}
	public void setExclusions(String[] exclusions) {
		this.exclusions = exclusions;
	}
}
