package com.passwordboss.android.http.beans;

public class CountryHttpBean extends BaseHttpBean{
	
	private CurrencyHttpBean currency;
	private LanguageHttpBean language;
	private String dialing_code;
	
	public CountryHttpBean() {
		super();
	}
	
	public CurrencyHttpBean getCurrency() {
		return currency;
	}
	
	public void setCurrency(CurrencyHttpBean currency) {
		this.currency = currency;
	}
	
	public LanguageHttpBean getLanguage() {
		return language;
	}
	
	public void setLanguage(LanguageHttpBean language) {
		this.language = language;
	}

	public String getDialingCode() {
		return dialing_code;
	}

	public void setDialingCode(String dialingCode) {
		this.dialing_code = dialingCode;
	}
}
