package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.Settings;
import com.passwordboss.android.database.bll.CountryBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.helper.Regex;
import com.passwordboss.android.helper.SoftKeyboard;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.UnexpectedServerError;
import com.passwordboss.android.http.beans.AccountPatchHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.task.NetworkTask;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoStepVerificationBackupPhoneFragment extends EventBusFragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwoStepVerificationBackupSecurityCodeFragment.class);
    @Bind(R.id.fr_tsvbp_country_code)
    TextView mCountryCodeView;
    private final AdapterView.OnItemSelectedListener mOnItemSelectedListenerCountry = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Country country = (Country) parent.getItemAtPosition(position);
            String dialingCode = Strings.nullToEmpty(country.getDialingCode());
            if (!dialingCode.startsWith("+")) dialingCode = "+" + dialingCode;
            mCountryCodeView.setText(dialingCode);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    @Bind(R.id.fr_tsvbp_country)
    Spinner mCountryView;
    @Bind(R.id.fr_tsvbp_phone)
    EditText mPhoneView;

    @Nullable
    private String getCurrentCountryCode() {
        try {
            SettingsBll mSettingsBll = new SettingsBll(DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY));
            Settings countrySetting = mSettingsBll.getSettingsByKey(DatabaseConstants.ITEM_SETTINGS_COUNTRY);
            if (null == countrySetting) return null;
            return countrySetting.getValue();
        } catch (SQLException e) {
            LOGGER.error("getCurrentCountryCode", e);
        }
        return null;
    }

    private void nextStep() {
        new SoftKeyboard().hide(this);
        EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.Done));
    }

    @OnClick(R.id.fr_tsvbp_button_next)
    void onClickButtonNext() {
        AwesomeValidation validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(mPhoneView, Regex.NOT_EMPTY, getString(R.string.EnterValidPhone));
        if (!validation.validate()) return;
        new ProgressDialog(this).showLoading();
        String fullPhone = mCountryCodeView.getText().toString() + mPhoneView.getText().toString();
        new SendDataTask(getContext(), fullPhone).start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_two_step_verification_backup_phone, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_two_step_verification_backup_phone, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventSendDataResult(SendDataResultEvent event) {
        new ProgressDialog(this).dismiss();
        if (event.hasError()) {
            new ErrorDialog(getContext()).show(event.getError());
            return;
        }
        nextStep();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_skip:
                nextStep();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateCountryView();
        new SoftKeyboard().showImplicit(mPhoneView);
    }

    private void updateCountryView() {
        try {
            DatabaseHelperNonSecure helperNonSecure = DatabaseHelperNonSecure.getHelper(getContext());
            CountryBll countryBll = new CountryBll(helperNonSecure);
            List<Country> countries = countryBll.getAllCountry();
            ArrayAdapter<Country> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countries);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCountryView.setAdapter(adapter);
            mCountryView.setOnItemSelectedListener(mOnItemSelectedListenerCountry);
            String countryCode = getCurrentCountryCode();
            if (null == countryCode) {
                countryCode = getResources().getConfiguration().locale.getCountry();
            }
            for (int i = 0; i < countries.size(); i++) {
                Country country = countries.get(i);
                if (countryCode.equalsIgnoreCase(country.getCode())) {
                    mCountryView.setSelection(i);
                    return;
                }
            }
            mCountryView.setSelection(0);
        } catch (Exception e) {
            LOGGER.error("updateCountryView", e);
        }
    }

    private static class SendDataResultEvent extends BaseEvent {
        public SendDataResultEvent(Throwable throwable) {
            super(throwable);
        }

        public SendDataResultEvent() {
        }
    }


    private static class SendDataTask extends NetworkTask<SendDataResultEvent> {
        private final String mPhone;

        protected SendDataTask(Context context, String phone) {
            super(context);
            mPhone = phone;
        }

        @Override
        protected SendDataResultEvent createEventError(Exception e) {
            return new SendDataResultEvent(e);
        }

        @Override
        protected SendDataResultEvent createEventSuccess() {
            return new SendDataResultEvent();
        }

        @Override
        protected void runImpl() throws Exception {
            AccountPatchHttpBean bean = new AccountPatchHttpBean();
            bean.setPhone(mPhone);
            ServerResponseHttpBean response = new ServerAPI().accountPatch(bean);
            if (null != response.getError()) {
                throw new UnexpectedServerError(getContext(), response.getError());
            }


        }
    }

}
