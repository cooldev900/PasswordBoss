package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.activity.SettingItemListActivity;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.event.CountrySelectedEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.widget.AppInputField;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemAddressFragment extends SecureItemFragment {
    @Bind(R.id.fr_siad_address1)
    EditText mAddress1View;
    @Bind(R.id.fr_siad_address2)
    EditText mAddress2View;
    @Bind(R.id.fr_siad_city)
    EditText mCityView;
    @Bind(R.id.fr_si_country)
    AppInputField mCountryView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_state)
    AutoCompleteTextView mStateView;
    @Bind(R.id.fr_siad_zip_postal)
    EditText mZipPostalView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Address;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_address;
    }

    @OnClick(R.id.fr_si_country)
    void onClickCountry() {
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_COUNTRY));
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventCountrySelected(CountrySelectedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        Country country = event.getCountry();
        mCountryView.setText(country.getName());
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.ADDRESS1, mAddress1View);
        properties.setString(Identifier.ADDRESS2, mAddress2View);
        properties.setString(Identifier.CITY, mCityView);
        properties.setString(Identifier.STATE, mStateView);
        properties.setString(Identifier.ZIP_CODE, mZipPostalView);
        properties.setString(Identifier.COUNTRY, mCountryView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mAddress1View.setText(properties.getString(Identifier.ADDRESS1));
        mAddress2View.setText(properties.getString(Identifier.ADDRESS2));
        mCityView.setText(properties.getString(Identifier.CITY));
        mStateView.setText(properties.getString(Identifier.STATE));
        mZipPostalView.setText(properties.getString(Identifier.ZIP_CODE));
        mCountryView.setText(properties.getString(Identifier.COUNTRY));
    }

}
