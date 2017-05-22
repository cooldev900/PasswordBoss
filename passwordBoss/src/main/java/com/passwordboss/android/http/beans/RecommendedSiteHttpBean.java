package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class RecommendedSiteHttpBean extends BaseHttpBean  implements Serializable{

	private UriHttpBean uris[];
	private String hash, status, friendly_name;
	private ImagesHttpBean images[];
	private boolean validated;

	public UriHttpBean[] getUris() {
		return uris;
	}
	public void setUris(UriHttpBean[] uris) {
		this.uris = uris;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	public ImagesHttpBean[] getImages() {
		return images;
	}
	public void setImages(ImagesHttpBean[] images) {
		this.images = images;
	}
	public String getFriendlyName() {
		return friendly_name;
	}
	public void setFriendlyName(String friendly_name) {
		this.friendly_name = friendly_name;
	}
	
}
