package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemInsuranceFragment extends SecureItemFragment {
    @Bind(R.id.fr_siin_agent)
    EditText mAgentView;
    @Bind(R.id.fr_siin_deductible)
    EditText mDeductibleView;
    @Bind(R.id.fr_si_insurance_company)
    EditText mInsuranceCompanyView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_phone_number)
    EditText mPhoneNumberView;
    @Bind(R.id.fr_siin_policy_number)
    EditText mPolicyNumberView;
    @Bind(R.id.fr_siin_renewal_date)
    EditText mRenewalDateView;
    @Bind(R.id.fr_si_type)
    EditText mTypeView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Insurance;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_insurance;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.TYPE, mTypeView);
        properties.setString(Identifier.INSURANCE_COMPANY, mInsuranceCompanyView);
        properties.setString(Identifier.POLICY_NUMBER, mPolicyNumberView);
        properties.setString(Identifier.AGENT, mAgentView);
        properties.setString(Identifier.AGENT_PHONE_NUMBER, mPhoneNumberView);
        properties.setString(Identifier.DEDUCTIBLE, mDeductibleView);
        properties.setString(Identifier.RENEWAL_DATE, mRenewalDateView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mTypeView.setText(properties.getString(Identifier.TYPE));
        mInsuranceCompanyView.setText(properties.getString(Identifier.INSURANCE_COMPANY));
        mPolicyNumberView.setText(properties.getString(Identifier.POLICY_NUMBER));
        mAgentView.setText(properties.getString(Identifier.AGENT));
        mPhoneNumberView.setText(properties.getString(Identifier.AGENT_PHONE_NUMBER));
        mDeductibleView.setText(properties.getString(Identifier.DEDUCTIBLE));
        mRenewalDateView.setText(properties.getString(Identifier.RENEWAL_DATE));
    }

}
