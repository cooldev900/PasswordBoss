package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.Country;

public class CountrySelectedEvent extends SettingItemResultEvent {
    private Country mCountry;

    public CountrySelectedEvent(Country country) {
        mCountry = country;
    }

    public Country getCountry() {
        return mCountry;
    }
}
