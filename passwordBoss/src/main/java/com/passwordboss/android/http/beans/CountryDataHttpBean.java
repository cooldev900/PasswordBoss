package com.passwordboss.android.http.beans;

public class CountryDataHttpBean extends BaseHttpResponse{
	private CountryHttpBean[] countries;
	
	
	public CountryDataHttpBean() {
		super();
	}

	public CountryHttpBean[] getCountries() {
		return countries;
	}

	public void setCountries(CountryHttpBean[] countries) {
		this.countries = countries;
	}

}
