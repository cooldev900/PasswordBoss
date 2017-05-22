package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemHotelRewardsFragment extends SecureItemFragment {
    @Bind(R.id.fr_sihr_hotel)
    EditText mHotelView;
    @Bind(R.id.fr_sihr_membership_number)
    EditText mMembershipNumberView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_phone_number)
    EditText mPhoneNumberView;
    @Bind(R.id.fr_si_status_level)
    EditText mStatusLevelView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.HotelRewards;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_hotel_rewards;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.HOTEL, mHotelView);
        properties.setString(Identifier.MEMBERSHIP_NUMBER, mMembershipNumberView);
        properties.setString(Identifier.STATUS_LEVEL, mStatusLevelView);
        properties.setString(Identifier.PHONE_NUMBER, mPhoneNumberView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mHotelView.setText(properties.getString(Identifier.HOTEL));
        mMembershipNumberView.setText(properties.getString(Identifier.MEMBERSHIP_NUMBER));
        mStatusLevelView.setText(properties.getString(Identifier.STATUS_LEVEL));
        mPhoneNumberView.setText(properties.getString(Identifier.PHONE_NUMBER));
    }

}
