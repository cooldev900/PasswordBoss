package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemWebsiteFragment extends SecureItemFragment {
    @Bind(R.id.fr_siwb_advanced_settings_container)
    View mAdvancedSettingsContainerView;
    @Bind(R.id.fr_siwb_autologin)
    SwitchCompat mAutologinView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 2)
    EditText mNameView;
    @Bind(R.id.fr_siwb_subdomain_only)
    SwitchCompat mSubdomainOnlyView;
    @Bind(R.id.fr_siwb_url)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mUrlView;
    @Bind(R.id.fr_siwb_use_embedded_browser)
    SwitchCompat mUseEmbeddedBrowserView;
    @Bind(R.id.fr_si_username)
    EditText mUsernameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Website;
    }


    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_website;
    }

    @OnCheckedChanged(R.id.fr_siwb_advanced_settings)
    void onCheckedChangedAdvancedSettings(boolean checked) {
        mAdvancedSettingsContainerView.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setLoginUrl(mUrlView.getText().toString());
        item.setName(mNameView.getText().toString());
        properties.setString(SecureItemData.Identifier.USERNAME, mUsernameView.getText().toString());
        properties.setBoolean(SecureItemData.Identifier.USE_SECURE_BROWSER, mUseEmbeddedBrowserView.isChecked());
        properties.setBoolean(SecureItemData.Identifier.AUTOLOGIN, mAutologinView.isChecked());
        properties.setBoolean(SecureItemData.Identifier.SUB_DOMAIN, mSubdomainOnlyView.isChecked());
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mUrlView.setText(item.getLoginUrl());
        mNameView.setText(item.getName());
        mUsernameView.setText(properties.getString(SecureItemData.Identifier.USERNAME));
        mUseEmbeddedBrowserView.setChecked(properties.getBoolean(SecureItemData.Identifier.USE_SECURE_BROWSER, false));
        mAutologinView.setChecked(properties.getBoolean(SecureItemData.Identifier.AUTOLOGIN, true));
        mSubdomainOnlyView.setChecked(properties.getBoolean(SecureItemData.Identifier.SUB_DOMAIN, false));
    }
}
