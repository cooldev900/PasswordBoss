package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemHealthInsuranceFragment extends SecureItemFragment {
    @Bind(R.id.fr_sihi_group_number)
    EditText mGroupNumberView;
    @Bind(R.id.fr_si_insurance_company)
    EditText mInsuranceCompanyView;
    @Bind(R.id.fr_si_member_id)
    EditText mMemberIdView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sihi_prescription_plan)
    EditText mPrescriptionPlanView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.HealthInsurance;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_health_insurance;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.INSURANCE_COMPANY, mInsuranceCompanyView);
        properties.setString(Identifier.MEMBER_ID, mMemberIdView);
        properties.setString(Identifier.GROUP_NUMBER, mGroupNumberView);
        properties.setString(Identifier.PRESCRIPTION_PLAN, mPrescriptionPlanView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mInsuranceCompanyView.setText(properties.getString(Identifier.INSURANCE_COMPANY));
        mMemberIdView.setText(properties.getString(Identifier.MEMBER_ID));
        mGroupNumberView.setText(properties.getString(Identifier.GROUP_NUMBER));
        mPrescriptionPlanView.setText(properties.getString(Identifier.PRESCRIPTION_PLAN));
    }

}
