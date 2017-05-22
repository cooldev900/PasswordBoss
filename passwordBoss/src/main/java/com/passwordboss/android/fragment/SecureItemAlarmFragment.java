package com.passwordboss.android.fragment;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.model.ItemType;

import butterknife.Bind;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class SecureItemAlarmFragment extends SecureItemFragment {
    @Bind(R.id.fr_sial_alarm_company)
    EditText mAlarmCompanyView;
    @Bind(R.id.fr_sial_code)
    EditText mCodeView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_si_phone_number)
    EditText mPhoneView;

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Alarm;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_alarm;
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.ALARM_CODE, mCodeView);
        properties.setString(Identifier.ALARM_COMPANY, mAlarmCompanyView);
        properties.setString(Identifier.PHONE_NUMBER, mPhoneView);
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mCodeView.setText(properties.getString(Identifier.ALARM_CODE));
        mAlarmCompanyView.setText(properties.getString(Identifier.ALARM_COMPANY));
        mPhoneView.setText(properties.getString(Identifier.PHONE_NUMBER));

    }

}
