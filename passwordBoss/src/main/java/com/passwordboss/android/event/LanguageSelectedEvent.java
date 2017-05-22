package com.passwordboss.android.event;

public class LanguageSelectedEvent extends SettingItemResultEvent {

    private String mSelectedLanguage;

    public LanguageSelectedEvent(String selectedLanguage) {
        mSelectedLanguage = selectedLanguage;
    }

    public String getSelectedLanguage() {
        return mSelectedLanguage;
    }
}
