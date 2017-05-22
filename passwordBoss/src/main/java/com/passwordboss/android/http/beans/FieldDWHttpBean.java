package com.passwordboss.android.http.beans;

public class FieldDWHttpBean {
	
	private FieldHelperBean 
		pb_billing_address, pb_card_numberpb_card_type, pb_cvv, 
		pb_expiration_date, pb_issue_bank, pb_issue_number, pb_issue_date, 
		pb_pin;

	public FieldHelperBean getPbBillingAddress() {
		return pb_billing_address;
	}

	public void setPbBillingAddress(FieldHelperBean pbBillingAddress) {
		this.pb_billing_address = pbBillingAddress;
	}

	public FieldHelperBean getPbCardNumberpbCardType() {
		return pb_card_numberpb_card_type;
	}

	public void setPbCardNumberpbCardType(
			FieldHelperBean pbCardNumberpbCardType) {
		this.pb_card_numberpb_card_type = pbCardNumberpbCardType;
	}

	public FieldHelperBean getPbCvv() {
		return pb_cvv;
	}

	public void setPbCvv(FieldHelperBean pbCvv) {
		this.pb_cvv = pbCvv;
	}

	public FieldHelperBean getPb_expirationDate() {
		return pb_expiration_date;
	}

	public void setPbExpirationDate(FieldHelperBean pbExpirationDate) {
		this.pb_expiration_date = pbExpirationDate;
	}

	public FieldHelperBean getPbIssueBank() {
		return pb_issue_bank;
	}

	public void setPbIssueBank(FieldHelperBean pbIssueBank) {
		this.pb_issue_bank = pbIssueBank;
	}

	public FieldHelperBean getPbIssueNumber() {
		return pb_issue_number;
	}

	public void setPbIssueNumber(FieldHelperBean pb_issue_number) {
		this.pb_issue_number = pb_issue_number;
	}

	public FieldHelperBean getPbIssueDate() {
		return pb_issue_date;
	}

	public void setPbIssueDate(FieldHelperBean pbIssueDate) {
		this.pb_issue_date = pbIssueDate;
	}

	public FieldHelperBean getPbPin() {
		return pb_pin;
	}

	public void setPbPin(FieldHelperBean pbPin) {
		this.pb_pin = pbPin;
	}

	
}
