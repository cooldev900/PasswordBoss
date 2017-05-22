package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemSoftwareLicenseFragment extends SecureItemFragment {
    @Bind(R.id.fr_sisl_license_key)
    EditText mLicenseKeyView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sisl_nubmer_of_licenses)
    EditText mNubmerOfLicencesView;
    @Bind(R.id.fr_sisl_order_number)
    EditText mOrderNumberView;
    @Bind(R.id.fr_sisl_price)
    EditText mPriceView;
    @Bind(R.id.fr_sisl_publisher)
    EditText mPublisherView;
    @Bind(R.id.fr_sisl_purchase_date)
    EditText mPurchaseDateView;
    @Bind(R.id.fr_sisl_support_through)
    EditText mSupportThroughView;
    @Bind(R.id.fr_sisl_verison)
    EditText mVersionView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.SoftwareLicense;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_software_license;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.LICENSE_KEY, mLicenseKeyView);
        properties.setString(Identifier.VERSION, mVersionView);
        properties.setString(Identifier.PUBLISHER, mPublisherView);
        properties.setString(Identifier.PRICE, mPriceView);
        properties.setString(Identifier.PURCHASE_DATE, mPurchaseDateView);
        properties.setString(Identifier.SUPPORT_THROUGH, mSupportThroughView);
        properties.setString(Identifier.ORDER_NUMBER, mOrderNumberView);
        properties.setString(Identifier.NUMBER_OF_LICENSES, mNubmerOfLicencesView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mLicenseKeyView.setText(properties.getString(Identifier.LICENSE_KEY));
        mVersionView.setText(properties.getString(Identifier.VERSION));
        mPublisherView.setText(properties.getString(Identifier.PUBLISHER));
        mPriceView.setText(properties.getString(Identifier.PRICE));
        mPurchaseDateView.setText(properties.getString(Identifier.PURCHASE_DATE));
        mSupportThroughView.setText(properties.getString(Identifier.SUPPORT_THROUGH));
        mOrderNumberView.setText(properties.getString(Identifier.ORDER_NUMBER));
        mNubmerOfLicencesView.setText(properties.getString(Identifier.NUMBER_OF_LICENSES));
    }

}
