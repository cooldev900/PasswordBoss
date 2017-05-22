package com.passwordboss.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.utils.LanguagesUtils;
import com.passwordboss.android.utils.Pref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class SelectLanguageActivity extends BaseActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectLanguageActivity.class);
    ConfigurationBll mConfigurationBll;
    DatabaseHelperNonSecure mDatabaseHelperNonSecure;
    @Bind(R.id.ac_sl_language)
    Spinner mLanguageView;
    @Bind(R.id.ac_sl_button_next)
    Button mNextButton;
    @Bind(R.id.ac_sl_label_prompt)
    TextView mPromptView;
    private LanguagesUtils nLanguagesUtils;

    private void initializeLanguage() {
        try {
            mDatabaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(getApplicationContext());
            mConfigurationBll = new ConfigurationBll(mDatabaseHelperNonSecure);
            nLanguagesUtils = new LanguagesUtils(this);
            ArrayList<String> languages = nLanguagesUtils.getListLanguages();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mLanguageView.setAdapter(adapter);
            String language = null;
            String email = Pref.getValue(this, Constants.EMAIL, null);
            if (null != email) {
                Configuration mConfiguration = mConfigurationBll.getConfigurationByEmailAndKey(email, DatabaseConstants.LANGUAGE);
                if (mConfiguration != null) {
                    language = mConfiguration.getValue();
                }
            }
            if (language == null) {
                language = nLanguagesUtils.getLanguageByLetterCode(getResources().getConfiguration().locale.getLanguage()
                );
            }
            for (int i = 0; i < languages.size(); i++) {
                if (languages.get(i).equalsIgnoreCase(language)) {
                    mLanguageView.setSelection(i, false);
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error during initialize language", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String language = (String) mLanguageView.getSelectedItem();
        AnalyticsHelperSegment.logAccountCreationFlow(this, 1,
                AnalyticsHelperSegment.STEP_NAME_LANGUAGE_SELECTION,
                language,
                AnalyticsHelperSegment.BUTTON_CLICKED_VALUE_CLOSE);
    }

    @OnClick(R.id.ac_sl_button_next)
    void onClickButtonNext() {
        String language = (String) mLanguageView.getSelectedItem();
        String email = Pref.getValue(this, Constants.EMAIL, Constants.NO_EMAIL);
        mConfigurationBll.updateOrInsertItem(email, DatabaseConstants.LANGUAGE, language);
        nLanguagesUtils.changeLanguage(language);
        AnalyticsHelperSegment.logAccountCreationFlow(this, 1,
                AnalyticsHelperSegment.STEP_NAME_LANGUAGE_SELECTION,
                language,
                AnalyticsHelperSegment.BUTTON_CLICKED_VALUE_COUNTINUE);
        Pref.setValue(this, Constants.LANGUAGE_CHOSEN, true);
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNextButton.setText(R.string.Next);
        mPromptView.setText(R.string.SelectLanguageMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);
        initializeLanguage();
    }

    @OnItemSelected(R.id.ac_sl_language)
    void onItemSelected(int position) {
        String language = (String) mLanguageView.getItemAtPosition(position);
        android.content.res.Configuration newConfig = nLanguagesUtils.changeLanguage(language);
        if (null != newConfig) onConfigurationChanged(newConfig);
        AnalyticsHelperSegment.logAccountCreationFlow(this, 1,
                AnalyticsHelperSegment.STEP_NAME_LANGUAGE_SELECTION,
                language,
                AnalyticsHelperSegment.BUTTON_CLICKED_VALUE_COUNTINUE);

    }

}
