package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemBankAccountFragment extends SecureItemFragment {
    @Bind(R.id.fr_siba_account_number)
    EditText mAccountNumberView;
    @Bind(R.id.fr_siba_bank_name)
    EditText mBankNameView;
    @Bind(R.id.fr_siba_bank_phone)
    EditText mBankPhoneView;
    @Bind(R.id.fr_siba_bic_swift)
    EditText mBicSwiftView;
    @SuppressWarnings("SpellCheckingInspection")
    @Bind(R.id.fr_siba_iban)
    EditText mIbanView;
    @Bind(R.id.fr_siba_name_on_account)
    EditText mNameOnAccountView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_siba_pin)
    EditText mPinView;
    @Bind(R.id.fr_siba_routing_number)
    EditText mRoutingNumberView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.BankAccount;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_bank_account;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.BANK_NAME, mBankNameView);
        properties.setString(Identifier.NAME_ON_ACCOUNT, mNameOnAccountView);
        properties.setString(Identifier.ACCOUNT_NUMBER, mAccountNumberView);
        properties.setString(Identifier.ROUTING_NUMBER, mRoutingNumberView);
        properties.setString(Identifier.SWIFT, mBicSwiftView);
        properties.setString(Identifier.IBAN, mIbanView);
        properties.setString(Identifier.PIN, mPinView);
        properties.setString(Identifier.BANK_PHONE, mBankPhoneView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mBankNameView.setText(properties.getString(Identifier.BANK_NAME));
        mNameOnAccountView.setText(properties.getString(Identifier.NAME_ON_ACCOUNT));
        mAccountNumberView.setText(properties.getString(Identifier.ACCOUNT_NUMBER));
        mRoutingNumberView.setText(properties.getString(Identifier.ROUTING_NUMBER));
        mBicSwiftView.setText(properties.getString(Identifier.SWIFT));
        mIbanView.setText(properties.getString(Identifier.IBAN));
        mPinView.setText(properties.getString(Identifier.PIN));
        mBankPhoneView.setText(properties.getString(Identifier.BANK_PHONE));
    }
}
