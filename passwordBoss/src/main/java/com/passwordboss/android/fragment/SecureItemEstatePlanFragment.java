package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemEstatePlanFragment extends SecureItemFragment {
    @Bind(R.id.fr_siep_attorney)
    EditText mAttorneyView;
    @Bind(R.id.fr_siep_executor)
    EditText mExecutorView;
    @Bind(R.id.fr_siep_location)
    EditText mLocationView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_siep_trustee)
    EditText mTrusteeView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.EstatePlan;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_estate_plan;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.LOCATION_OF_DOCUMENTS, mLocationView);
        properties.setString(Identifier.ATTORNEY, mAttorneyView);
        properties.setString(Identifier.EXECUTOR, mExecutorView);
        properties.setString(Identifier.TRUSTEE, mTrusteeView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mLocationView.setText(properties.getString(Identifier.LOCATION_OF_DOCUMENTS));
        mAttorneyView.setText(properties.getString(Identifier.ATTORNEY));
        mExecutorView.setText(properties.getString(Identifier.EXECUTOR));
        mTrusteeView.setText(properties.getString(Identifier.TRUSTEE));
    }

}
