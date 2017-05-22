package com.passwordboss.android.http.beans;

public class CampaignHttpBean extends BaseHttpBean{

	private String identifier, notes, description;
	private PartnerHttpBean partner;
	
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
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public PartnerHttpBean getPartner() {
		return partner;
	}
	public void setPartner(PartnerHttpBean partner) {
		this.partner = partner;
	}
}