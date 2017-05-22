package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemPrescriptionFragment extends SecureItemFragment {
    @Bind(R.id.fr_sipr_doctor_phone)
    EditText mDoctorPhoneView;
    @Bind(R.id.fr_sipr_doctor)
    EditText mDoctorView;
    @Bind(R.id.fr_sipr_medicine)
    EditText mMedicineView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_sipr_pharmacy_phone)
    EditText mPharmacyPhoneView;
    @Bind(R.id.fr_sipr_pharmacy)
    EditText mPharmacyView;
    @Bind(R.id.fr_sipr_prescription_number)
    EditText mPrescriptionNumberView;
    @Bind(R.id.fr_sipr_refills)
    EditText mRefillsView;


    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Prescription;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_prescription;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.MEDICINE, mMedicineView);
        properties.setString(Identifier.DOCTOR, mDoctorView);
        properties.setString(Identifier.DOCTOR_PHONE, mDoctorPhoneView);
        properties.setString(Identifier.PHARMACY, mPharmacyView);
        properties.setString(Identifier.PHARMACY_PHONE, mPharmacyPhoneView);
        properties.setString(Identifier.PRESCRIPTION_NUMBER, mPrescriptionNumberView);
        properties.setString(Identifier.REFILLS, mRefillsView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mMedicineView.setText(properties.getString(Identifier.MEDICINE));
        mDoctorView.setText(properties.getString(Identifier.DOCTOR));
        mDoctorPhoneView.setText(properties.getString(Identifier.DOCTOR_PHONE));
        mPharmacyView.setText(properties.getString(Identifier.PHARMACY));
        mPharmacyPhoneView.setText(properties.getString(Identifier.PHARMACY_PHONE));
        mPrescriptionNumberView.setText(properties.getString(Identifier.PRESCRIPTION_NUMBER));
        mRefillsView.setText(properties.getString(Identifier.REFILLS));
    }

}
