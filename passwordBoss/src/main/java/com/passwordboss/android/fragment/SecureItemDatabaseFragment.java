package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemDatabaseFragment extends SecureItemFragment {
    @Bind(R.id.fr_sidb_database)
    EditText mDatabaseView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_port)
    EditText mPortView;
    @Bind(R.id.fr_si_server_address)
    EditText mServerAddress;
    @Bind(R.id.fr_si_username)
    EditText mUsernameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Database;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_database;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.SERVER_ADDRESS, mServerAddress.getText().toString());
        properties.setString(Identifier.PORT, mPortView.getText().toString());
        properties.setString(Identifier.DATABASE, mDatabaseView.getText().toString());
        properties.setString(Identifier.USERNAME, mUsernameView.getText().toString());
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mServerAddress.setText(properties.getString(Identifier.SERVER_ADDRESS));
        mPortView.setText(properties.getString(Identifier.PORT));
        mDatabaseView.setText(properties.getString(Identifier.DATABASE));
        mUsernameView.setText(properties.getString(Identifier.USERNAME));
    }
}
