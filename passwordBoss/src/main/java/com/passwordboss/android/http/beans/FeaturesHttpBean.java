package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class FeaturesHttpBean extends BaseHttpBean implements Serializable{

	private String description, duration;
	private FeatureHttpBean features[];
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public FeatureHttpBean[] getFeatures() {
		return features;
	}
	public void setFeatures(FeatureHttpBean[] features) {
		this.features = features;
	}
	
	
	
	
}
