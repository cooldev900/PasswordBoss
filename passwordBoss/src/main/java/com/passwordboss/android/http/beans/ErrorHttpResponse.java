package com.passwordboss.android.http.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class ErrorHttpResponse implements Serializable{

	private ErrorHttpBean error;
	private LanguageHttpBean language;
	private String media,version;
	private DateTime now;
	
	public ErrorHttpBean getError() {
		return error;
	}
	public void setError(ErrorHttpBean error) {
		this.error = error;
	}
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public DateTime getNow() {
		return now;
	}
	public void setNow(DateTime now) {
		this.now = now;
	}
	
	
}
