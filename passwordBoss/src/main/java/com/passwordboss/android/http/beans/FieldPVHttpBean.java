package com.passwordboss.android.http.beans;

public class FieldPVHttpBean {
	private FieldHelperBean pb_email,pb_password,pb_password_repeat,pb_username;

	public FieldHelperBean getPbEmail() {
		return pb_email;
	}

	public void setPb_email(FieldHelperBean pbEmail) {
		this.pb_email = pbEmail;
	}

	public FieldHelperBean getPbPassword() {
		return pb_password;
	}

	public void setPbPassword(FieldHelperBean pbPassword) {
		this.pb_password = pbPassword;
	}

	public FieldHelperBean getPbPasswordRepeat() {
		return pb_password_repeat;
	}

	public void setPbPasswordRepeat(FieldHelperBean pbPasswordRepeat) {
		this.pb_password_repeat = pbPasswordRepeat;
	}

	public FieldHelperBean getPbUsername() {
		return pb_username;
	}

	public void setPbUsername(FieldHelperBean pbUsername) {
		this.pb_username = pbUsername;
	}
}
