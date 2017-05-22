package com.passwordboss.android.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.bll.LanguageBll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LanguagesUtils {


    ArrayList<Language> arrayListLanguages;
    private Context mContext;
    private DatabaseHelperNonSecure mDatabaseHelperNonSecure;
    private LanguageBll mLanguageBll;

    public LanguagesUtils(Context context) {
        arrayListLanguages = new ArrayList<>();
        mDatabaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(context);
        List<com.passwordboss.android.database.beans.Language> langList;
        try {
            mLanguageBll = new LanguageBll(mDatabaseHelperNonSecure);
            langList = mLanguageBll.getAllLanguages();
            // TODO: temp solution, refactor
            if (langList != null) {
                for (int i = 0, max = langList.size(); i < max; i++) {
                    arrayListLanguages.add(new Language(langList.get(i).getTranslatedName(), langList.get(i).getCode()));
                }
            }

        } catch (Exception ex) {
        }


        this.mContext = context;
    }

    @Nullable
    public Configuration changeLanguage(String language) {
        int position = -1;
        for (int i = 0, max = arrayListLanguages.size(); i < max; i++) {
            if (arrayListLanguages.get(i).getLanguage().equalsIgnoreCase(language)) {
                position = i;
            }
        }
        if (position != -1 && arrayListLanguages.size() > position) {
            Locale locale = new Locale(arrayListLanguages.get(position).getLanguageTwoLetterCode());
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
            return config;
        }
        return null;
    }

    public String getLanguageByLetterCode(String latter) {
        int position = -1;
        for (int i = 0, max = arrayListLanguages.size(); i < max; i++) {
            if (arrayListLanguages.get(i).getLanguageTwoLetterCode().equalsIgnoreCase(latter)) {
                position = i;
            }
        }
        if (position != -1) {
            return arrayListLanguages.get(position).getLanguage();
        }
        return null;
    }

    public ArrayList<String> getListLanguages() {
        ArrayList<String> languages = new ArrayList<>();
        for (int i = 0; i < arrayListLanguages.size(); i++) {
            languages.add(arrayListLanguages.get(i).getLanguage());
        }
        Collections.sort(languages, String.CASE_INSENSITIVE_ORDER);
        return languages;
    }
}
