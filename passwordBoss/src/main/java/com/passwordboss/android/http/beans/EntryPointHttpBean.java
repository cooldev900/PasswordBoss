package com.passwordboss.android.http.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class EntryPointHttpBean extends BaseHttpResponse{

	private LanguageHttpBean language;
	private LinksHttpBean links;
	
	public LanguageHttpBean getLanguage() {
		return language;
	}
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}
	public LinksHttpBean getLinks() {
		return links;
	}
	public void setLinks(LinksHttpBean links) {
		this.links = links;
	}
}
