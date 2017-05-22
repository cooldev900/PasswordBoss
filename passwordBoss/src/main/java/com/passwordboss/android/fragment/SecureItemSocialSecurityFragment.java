package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemSocialSecurityFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_date_of_birth)
    EditText mDateOfBirthView;
    @Bind(R.id.fr_si_first_name)
    EditText mFirstNameView;
    @Bind(R.id.fr_si_last_name)
    EditText mLastNameView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_number)
    EditText mNumberView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.SocialSecurity;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_social_security;
    }
    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.FIRST_NAME, mFirstNameView);
        properties.setString(Identifier.LAST_NAME, mLastNameView);
        properties.setString(Identifier.SOCIAL_SECURITY_NUMBER, mNumberView);
        properties.setString(Identifier.DATE_OF_BIRTH, mDateOfBirthView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mFirstNameView.setText(properties.getString(Identifier.FIRST_NAME));
        mLastNameView.setText(properties.getString(Identifier.LAST_NAME));
        mNumberView.setText(properties.getString(Identifier.SOCIAL_SECURITY_NUMBER));
        mDateOfBirthView.setText(properties.getString(Identifier.DATE_OF_BIRTH));
    }

}
