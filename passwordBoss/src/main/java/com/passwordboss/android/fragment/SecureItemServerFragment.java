package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemServerFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_applicaiton)
    EditText mApplicationView;
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
        return ItemType.Server;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_server;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.APPLICATION, mApplicationView);
        properties.setString(Identifier.SERVER_ADDRESS, mServerAddress);
        properties.setString(Identifier.PORT, mPortView);
        properties.setString(Identifier.USERNAME, mUsernameView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mApplicationView.setText(properties.getString(Identifier.APPLICATION));
        mServerAddress.setText(properties.getString(Identifier.SERVER_ADDRESS));
        mPortView.setText(properties.getString(Identifier.PORT));
        mUsernameView.setText(properties.getString(Identifier.USERNAME));
    }
}
