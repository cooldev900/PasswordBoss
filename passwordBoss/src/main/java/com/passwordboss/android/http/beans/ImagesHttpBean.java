package com.passwordboss.android.http.beans;

public class ImagesHttpBean extends BaseHttpBean{

	private String hash, url;
	private ImageSizeHttpBean size;
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ImageSizeHttpBean getSize() {
		return size;
	}
	public void setSize(ImageSizeHttpBean size) {
		this.size = size;
	}

}
