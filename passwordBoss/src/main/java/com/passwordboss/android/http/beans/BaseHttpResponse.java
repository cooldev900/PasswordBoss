package com.passwordboss.android.http.beans;

import java.io.Serializable;

/*
 * Base class for all HTTP responses 
 * contains ErrorHttpResponse to error info in case of errors
 * And media version, now and version
 */
public class BaseHttpResponse implements Serializable{
	
	private ErrorHttpResponse error;
	private String media,now,version;
	
	public ErrorHttpResponse getError() {
		return error;
	}
	public void setError(ErrorHttpResponse error) {
		this.error = error;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	

}
