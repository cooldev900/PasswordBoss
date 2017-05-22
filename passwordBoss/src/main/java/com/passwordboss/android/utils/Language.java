package com.passwordboss.android.utils;

public class Language {

    private String language;
    private String languageTwoLetterCode;

    public Language(String language, String languageTwoLetterCode) {
        setLanguage(language);
        setLanguageTwoLetterCode(languageTwoLetterCode);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageTwoLetterCode() {
        return languageTwoLetterCode;
    }

    public void setLanguageTwoLetterCode(String languageTwoLetterCode) {
        this.languageTwoLetterCode = languageTwoLetterCode;
    }

}
