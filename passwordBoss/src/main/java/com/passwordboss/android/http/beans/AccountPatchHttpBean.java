package com.passwordboss.android.http.beans;

public class AccountPatchHttpBean {
	private String language, first_name, phone, last_name, email, backup_phone, public_key;
	public AccountPatchHttpBean() {

	}
	
	public AccountPatchHttpBean(String publicKey) {
		this.public_key = publicKey;
	}
	
	public AccountPatchHttpBean(String language, String phone,
			String first_name, String last_name, String email) {
		super();
		this.language = language;
		this.first_name = first_name;
		this.phone = phone;
		this.last_name = last_name;
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBackup_phone() {
		return backup_phone;
	}

	public void setBackup_phone(String backup_phone) {
		this.backup_phone = backup_phone;
	}

}
