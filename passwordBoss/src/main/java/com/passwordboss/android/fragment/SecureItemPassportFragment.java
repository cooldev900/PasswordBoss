package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemPassportFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_date_of_birth)
    EditText mDateOfBirthView;
    @Bind(R.id.fr_si_expires)
    EditText mExpiresView;
    @Bind(R.id.fr_si_first_name)
    EditText mFirstNameView;
    @Bind(R.id.fr_sips_issue_date)
    EditText mIssueDateView;
    @Bind(R.id.fr_si_last_name)
    EditText mLastNameView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sips_nationality)
    EditText mNationalityView;
    @Bind(R.id.fr_si_number)
    EditText mNumberView;
    @Bind(R.id.fr_sips_place_of_issue)
    EditText mPlaceOfIssueView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Passport;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_passport;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.FIRST_NAME, mFirstNameView);
        properties.setString(Identifier.LAST_NAME, mLastNameView);
        properties.setString(Identifier.PASSPORT_NUMBER, mNumberView);
        properties.setString(Identifier.NATIONALITY, mNationalityView);
        properties.setString(Identifier.ISSUE_DATE, mIssueDateView);
        properties.setString(Identifier.EXPIRES, mExpiresView);
        properties.setString(Identifier.DATE_OF_BIRTH, mDateOfBirthView);
        properties.setString(Identifier.PLACE_OF_ISSUE, mPlaceOfIssueView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mFirstNameView.setText(properties.getString(Identifier.FIRST_NAME));
        mLastNameView.setText(properties.getString(Identifier.LAST_NAME));
        mNumberView.setText(properties.getString(Identifier.PASSPORT_NUMBER));
        mNationalityView.setText(properties.getString(Identifier.NATIONALITY));
        mIssueDateView.setText(properties.getString(Identifier.ISSUE_DATE));
        mExpiresView.setText(properties.getString(Identifier.EXPIRES));
        mDateOfBirthView.setText(properties.getString(Identifier.DATE_OF_BIRTH));
        mPlaceOfIssueView.setText(properties.getString(Identifier.PLACE_OF_ISSUE));
    }


}
