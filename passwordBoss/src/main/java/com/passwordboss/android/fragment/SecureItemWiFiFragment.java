package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemWiFiFragment extends SecureItemFragment {
    @Bind(R.id.fr_siwf_authentication)
    EditText mAuthenticationView;
    @Bind(R.id.fr_siwf_encryption)
    EditText mEncryptionView;
    @Bind(R.id.fr_siwf_fips_mode)
    EditText mFipsModeView;
    @Bind(R.id.fr_siwf_key_type)
    EditText mKeyTypeView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_siwf_ssid)
    EditText mSsidView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.WiFi;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_wifi;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.SSID, mSsidView);
        properties.setString(Identifier.AUTHENTICATION, mAuthenticationView);
        properties.setString(Identifier.ENCRYPTION, mEncryptionView);
        properties.setString(Identifier.FIPS_MODE, mFipsModeView);
        properties.setString(Identifier.KEY_TYPE, mKeyTypeView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mSsidView.setText(properties.getString(Identifier.SSID));
        mAuthenticationView.setText(properties.getString(Identifier.AUTHENTICATION));
        mEncryptionView.setText(properties.getString(Identifier.ENCRYPTION));
        mFipsModeView.setText(properties.getString(Identifier.FIPS_MODE));
        mKeyTypeView.setText(properties.getString(Identifier.KEY_TYPE));
    }
}
