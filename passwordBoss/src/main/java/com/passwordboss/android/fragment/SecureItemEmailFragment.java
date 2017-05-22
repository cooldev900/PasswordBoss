package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemEmailFragment extends SecureItemFragment {
    @Bind(R.id.fr_siem_email)
    EditText mEmailView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Email;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_email;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.EMAIL, mEmailView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mEmailView.setText(properties.getString(Identifier.EMAIL));
    }


}
