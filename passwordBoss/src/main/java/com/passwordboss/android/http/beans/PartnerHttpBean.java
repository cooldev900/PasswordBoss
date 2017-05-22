package com.passwordboss.android.http.beans;

public class PartnerHttpBean extends BaseHttpBean {
	private String notes, partner_id;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}
}
