package com.passwordboss.android.http.beans;

public class TwoStepVerificationHttpBean {
	
	String multi_factor_code;
	String multi_factor_one_time_code;
	String qr;

	public String getMulti_factor_code() {
		return multi_factor_code;
	}

	public void setMulti_factor_code(String multi_factor_code) {
		this.multi_factor_code = multi_factor_code;
	}

	public String getMulti_factor_one_time_code() {
		return multi_factor_one_time_code;
	}

	public void setMulti_factor_one_time_code(String multi_factor_one_time_code) {
		this.multi_factor_one_time_code = multi_factor_one_time_code;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}
}
