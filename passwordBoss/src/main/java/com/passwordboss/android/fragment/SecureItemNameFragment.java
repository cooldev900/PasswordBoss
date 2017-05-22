package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.google.common.base.Joiner;
import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemNameFragment extends SecureItemFragment {
    @Bind(R.id.fr_si_first_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mFirstNameView;
    @Bind(R.id.fr_si_last_name)
    EditText mLastNameView;
    @Bind(R.id.fr_sinm_middle_name)
    EditText mMiddleNameView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Name;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_name;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        final String name = Joiner
                .on(' ')
                .skipNulls()
                .join(mFirstNameView.getText().toString(), mLastNameView.getText().toString());
        item.setName(name);
        properties.setString(Identifier.FIRST_NAME, mFirstNameView);
        properties.setString(Identifier.MIDDLE_NAME, mMiddleNameView);
        properties.setString(Identifier.LAST_NAME, mLastNameView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mFirstNameView.setText(properties.getString(Identifier.FIRST_NAME));
        mMiddleNameView.setText(properties.getString(Identifier.MIDDLE_NAME));
        mLastNameView.setText(properties.getString(Identifier.LAST_NAME));
    }

}
