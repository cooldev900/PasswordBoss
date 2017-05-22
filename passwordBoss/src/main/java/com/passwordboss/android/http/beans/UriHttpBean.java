package com.passwordboss.android.http.beans;

public class UriHttpBean extends BaseHttpBean {

	private String behavior, hash, type, uri, host, lookup_method;
	private int verified_count;
	private boolean is_trivial;
	
	public String getBehavior() {
		return behavior;
	}
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getVerifiedCount() {
		return verified_count;
	}
	public void setVerifiedCount(int verified_count) {
		this.verified_count = verified_count;
	}
	public boolean isTrivial() {
		return is_trivial;
	}
	public void setTrivial(boolean is_trivial) {
		this.is_trivial = is_trivial;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getLookupMethod() {
		return lookup_method;
	}
	public void setLookupMethod(String lookup_method) {
		this.lookup_method = lookup_method;
	}
	
	
	
}
