package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemApplicationFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_applicaiton)
    EditText mApplicationView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_type)
    EditText mTypeView;
    @Bind(R.id.fr_si_username)
    EditText mUsernameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Application;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_application;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(SecureItemData.Identifier.APPLICATION, mApplicationView.getText().toString());
        properties.setString(SecureItemData.Identifier.TYPE, mTypeView.getText().toString());
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mApplicationView.setText(properties.getString(SecureItemData.Identifier.APPLICATION));
        mTypeView.setText(properties.getString(SecureItemData.Identifier.TYPE));
    }
}
