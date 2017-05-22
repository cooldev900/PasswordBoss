package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemMemberIdFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_first_name)
    EditText mFirstNameView;
    @Bind(R.id.fr_si_last_name)
    EditText mLastNameView;
    @Bind(R.id.fr_si_member_id)
    EditText mMemberIdView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.MemberId;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_member_id;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.FIRST_NAME, mFirstNameView);
        properties.setString(Identifier.LAST_NAME, mLastNameView);
        properties.setString(Identifier.MEMBER_ID, mMemberIdView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mFirstNameView.setText(properties.getString(Identifier.FIRST_NAME));
        mLastNameView.setText(properties.getString(Identifier.LAST_NAME));
        mMemberIdView.setText(properties.getString(Identifier.MEMBER_ID));
    }

}
