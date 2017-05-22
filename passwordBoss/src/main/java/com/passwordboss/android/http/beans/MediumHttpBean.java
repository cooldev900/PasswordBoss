package com.passwordboss.android.http.beans;

public class MediumHttpBean extends BaseHttpBean{
	private String identifier, notes;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
