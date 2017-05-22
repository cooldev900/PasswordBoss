package com.passwordboss.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.passwordboss.android.R;
import com.passwordboss.android.activity.SettingItemListActivity;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.event.CardTypeSelectedEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.widget.AppInputField;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemCreditCardFragment extends SecureItemFragment {
    @Bind(R.id.fr_sicc_card_number)
    EditText mCardNumberView;
    @Bind(R.id.fr_sicc_card_type)
    AppInputField mCardTypeView;
    @Bind(R.id.fr_sicc_expires_month)
    Spinner mExpiresMonthsView;
    @Bind(R.id.fr_sicc_expires_year)
    Spinner mExpiresYearsView;
    @Bind(R.id.fr_sicc_issue_date)
    EditText mIssueDateView;
    @Bind(R.id.fr_sicc_issuing_bank)
    EditText mIssuingBankView;
    @Bind(R.id.fr_sicc_name_on_card)
    EditText mNameOnCardView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sicc_pin)
    EditText mPinView;
    @Bind(R.id.fr_sicc_security_code)
    EditText mSecurityCodeView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.CreditCard;
    }

    @SuppressWarnings("unchecked")
    private String getSpinnerValue(Spinner spinner) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        return adapter.getItem(spinner.getSelectedItemPosition());
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_credit_card;
    }

    private void initializeExpiresMonthsView() {
        List<String> list = new ArrayList<>();
        list.add(""); // not selected
        for (int index = 1; index <= 12; index++) {
            list.add(String.valueOf(index));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExpiresMonthsView.setAdapter(adapter);
    }

    private void initializeExpiresYearsView() {
        List<String> list = new ArrayList<>();
        list.add(""); // not selected
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        for (int index = year; index <= year + 25; index++) {
            list.add(String.valueOf(index));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExpiresYearsView.setAdapter(adapter);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCardTypeSelectedEvent(CardTypeSelectedEvent e) {
        EventBus.getDefault().removeStickyEvent(e);
        String cardType = e.getCardType();
        mCardTypeView.setText(cardType);
    }

    @OnClick(R.id.fr_sicc_card_type)
    void onClickCardType() {
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_CREDIT_CARDS));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeExpiresMonthsView();
        initializeExpiresYearsView();


    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.NAME_ON_CARD, mNameOnCardView);
        properties.setString(Identifier.CARD_NUMBER, mCardNumberView);
        properties.setString(Identifier.CARD_TYPE, mCardTypeView);
        properties.setString(Identifier.EXPIRES_MONTH, getSpinnerValue(mExpiresMonthsView));
        properties.setString(Identifier.EXPIRES_YEAR, getSpinnerValue(mExpiresYearsView));
        properties.setString(Identifier.ISSUING_BANK, mIssuingBankView);
        properties.setString(Identifier.SECURITY_CODE, mSecurityCodeView);
        properties.setString(Identifier.ISSUE_DATE, mIssueDateView);
        properties.setString(Identifier.PIN, mPinView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mNameOnCardView.setText(properties.getString(Identifier.NAME_ON_CARD));
        mCardNumberView.setText(properties.getString(Identifier.CARD_NUMBER));
        mCardTypeView.setText(properties.getString(Identifier.CARD_TYPE));
        setSpinnerValue(mExpiresMonthsView, properties.getString(Identifier.EXPIRES_MONTH));
        setSpinnerValue(mExpiresYearsView, properties.getString(Identifier.EXPIRES_YEAR));
        mIssuingBankView.setText(properties.getString(Identifier.ISSUING_BANK));
        mSecurityCodeView.setText(properties.getString(Identifier.SECURITY_CODE));
        mIssueDateView.setText(properties.getString(Identifier.ISSUE_DATE));
        mPinView.setText(properties.getString(Identifier.PIN));
    }

    @SuppressWarnings("unchecked")
    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position == -1) position = 0;
        spinner.setSelection(position);
    }


}
