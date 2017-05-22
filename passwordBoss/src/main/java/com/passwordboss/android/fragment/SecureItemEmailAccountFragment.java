package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemEmailAccountFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_port)
    EditText mPortView;
    @Bind(R.id.fr_siea_smpt_server)
    EditText mSmptServerView;
    @Bind(R.id.fr_si_type)
    EditText mTypeView;
    @Bind(R.id.fr_si_username)
    EditText mUsernameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.EmailAccount;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_email_account;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.SMPT_SERVER, mSmptServerView);
        properties.setString(Identifier.PORT, mPortView);
        properties.setString(Identifier.TYPE, mTypeView);
        properties.setString(Identifier.USERNAME, mUsernameView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mSmptServerView.setText(properties.getString(Identifier.SMPT_SERVER));
        mPortView.setText(properties.getString(Identifier.PORT));
        mTypeView.setText(properties.getString(Identifier.TYPE));
        mUsernameView.setText(properties.getString(Identifier.USERNAME));

    }
}
