package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class LanguageHttpBean extends BaseHttpBean implements Serializable{
	
	private String translated_name, url;
	private boolean translated;
	
	public LanguageHttpBean() {
		super();
	}
	
	public boolean getTranslated() {
		return translated;
	}
	
	public void setTranslated(boolean translated) {
		this.translated = translated;
	}
	
	public String getTranslatedName() {
		return translated_name;
	}
	
	public void setTranslatedName(String translated_name) {
		this.translated_name = translated_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
