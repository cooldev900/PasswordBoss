package com.passwordboss.android.http.beans;

import java.io.Serializable;

public class FeatureHttpBean extends BaseHttpBean implements Serializable{
	public String description, identifier;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
