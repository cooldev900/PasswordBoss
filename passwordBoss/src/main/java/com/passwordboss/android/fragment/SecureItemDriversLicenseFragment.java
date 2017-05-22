package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
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

public class SecureItemDriversLicenseFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_country)
    AppInputField mCountryView;
    @Bind(R.id.fr_si_expires)
    EditText mExpiresView;
    @Bind(R.id.fr_si_first_name)
    EditText mFirstNameView;
    @Bind(R.id.fr_si_last_name)
    EditText mLastNameView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sidl_number)
    EditText mNumberView;
    @Bind(R.id.fr_si_state)
    EditText mStateView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.DriversLicense;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_driving_license;
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
        properties.setString(Identifier.FIRST_NAME, mFirstNameView);
        properties.setString(Identifier.LAST_NAME, mLastNameView);
        properties.setString(Identifier.DRIVER_LICENSE_NUMBER, mNumberView);
        properties.setString(Identifier.EXPIRES, mExpiresView);
        properties.setString(Identifier.STATE, mStateView);
        properties.setString(Identifier.COUNTRY, mCountryView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mFirstNameView.setText(properties.getString(Identifier.FIRST_NAME));
        mLastNameView.setText(properties.getString(Identifier.LAST_NAME));
        mNumberView.setText(properties.getString(Identifier.DRIVER_LICENSE_NUMBER));
        mExpiresView.setText(properties.getString(Identifier.EXPIRES));
        final String state = properties.getString(Identifier.STATE);
        mStateView.setText(state);
        mCountryView.setText(properties.getString(Identifier.COUNTRY));
    }

}
