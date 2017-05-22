package com.passwordboss.android.http.beans;

public class ChannelHttpBean extends BaseHttpBean {
	private String purchase_url,purchase_url_in_app,
	search_provider_url,start_page_url, channel_id;

	public String getPurchaseUrl() {
		return purchase_url;
	}
	public void setPurchaseUrl(String purchase_url) {
		this.purchase_url = purchase_url;
	}
	public String getPurchaseUrlInApp() {
		return purchase_url_in_app;
	}
	public void setPurchaseUrlInApp(String purchaseUrlInApp) {
		this.purchase_url_in_app = purchaseUrlInApp;
	}
	public String getSearch_provider_url() {
		return search_provider_url;
	}
	public void setSearchProviderUrl(String searchProviderUrl) {
		this.search_provider_url = searchProviderUrl;
	}
	public String getStartPageUrl() {
		return start_page_url;
	}
	public void setStartPageUrl(String startPageUrl) {
		this.start_page_url = startPageUrl;
	}
	public String getChannelId() {
		return channel_id;
	}
	public void setChannelId(String channel_id) {
		this.channel_id = channel_id;
	}	
	
}


