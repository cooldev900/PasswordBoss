package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemSshKeyFragment extends SecureItemFragment {
    @Bind(R.id.fr_sisk_bit_strength)
    EditText mBitStrengthView;
    @Bind(R.id.fr_sisk_format)
    EditText mFormatView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sisk_passphrase)
    EditText mPassphraseView;
    @Bind(R.id.fr_si_port)
    EditText mPortView;
    @Bind(R.id.fr_sisk_private_key)
    EditText mPrivateKeyView;
    @Bind(R.id.fr_sisk_public_key)
    EditText mPublicKeyView;
    @Bind(R.id.fr_si_server_address)
    EditText mServerAddressView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.SshKey;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_ssh_key;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.SERVER_ADDRESS, mServerAddressView);
        properties.setString(Identifier.PORT, mPortView);
        properties.setString(Identifier.BIT_STRENGTH, mBitStrengthView);
        properties.setString(Identifier.FORMAT, mFormatView);
        properties.setString(Identifier.PASSPHRASE, mPassphraseView);
        properties.setString(Identifier.PUBLIC_KEY, mPublicKeyView);
        properties.setString(Identifier.PRIVATE_KEY, mPrivateKeyView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mServerAddressView.setText(properties.getString(Identifier.SERVER_ADDRESS));
        mPortView.setText(properties.getString(Identifier.PORT));
        mBitStrengthView.setText(properties.getString(Identifier.BIT_STRENGTH));
        mFormatView.setText(properties.getString(Identifier.FORMAT));
        mPassphraseView.setText(properties.getString(Identifier.PASSPHRASE));
        mPublicKeyView.setText(properties.getString(Identifier.PUBLIC_KEY));
        mPrivateKeyView.setText(properties.getString(Identifier.PRIVATE_KEY));
    }
}
