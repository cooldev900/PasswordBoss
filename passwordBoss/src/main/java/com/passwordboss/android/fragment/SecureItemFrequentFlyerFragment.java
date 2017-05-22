package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemFrequentFlyerFragment extends SecureItemFragment {
    @Bind(R.id.fr_siff_airline_phone)
    EditText mAirlinePhoneView;
    @Bind(R.id.fr_siff_airline)
    EditText mAirlineView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_siff_number)
    EditText mNumberView;
    @Bind(R.id.fr_si_status_level)
    EditText mStatusLevelView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.FrequentFlyer;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_frequent_flyer;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.AIRLINE, mAirlineView);
        properties.setString(Identifier.FREQUENT_FLYER_NUMBER, mNumberView);
        properties.setString(Identifier.STATUS_LEVEL, mStatusLevelView);
        properties.setString(Identifier.FREQUENT_FLYER_PHONE, mAirlinePhoneView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mAirlineView.setText(properties.getString(Identifier.AIRLINE));
        mNumberView.setText(properties.getString(Identifier.FREQUENT_FLYER_NUMBER));
        mStatusLevelView.setText(properties.getString(Identifier.STATUS_LEVEL));
        mAirlinePhoneView.setText(properties.getString(Identifier.FREQUENT_FLYER_PHONE));
    }

}
