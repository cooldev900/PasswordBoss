package com.passwordboss.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.passwordboss.android.R;
import com.passwordboss.android.activity.SettingItemListActivity;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData.Identifier;
import com.passwordboss.android.database.beans.Settings;
import com.passwordboss.android.database.bll.CountryBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.event.CountrySelectedEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.BaseObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecureItemPhoneFragment extends SecureItemFragment {
    @Bind(R.id.fr_siph_country_code)
    TextView mCountryCodeView;
    @Bind(R.id.fr_si_country)
    @NotEmpty(messageId = R.string.RequiredField, order = 2)
    EditText mCountryView;
    @Bind(R.id.fr_si_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_siph_phone)
    EditText mPhoneView;

    private Observable<List<Country>> countryListObservable = Observable.defer(() -> {
        try {
            return Observable.just(new CountryBll(DatabaseHelperNonSecure.getHelper(getContext())).getAllCountry());
        } catch (SQLException e) {
            throw new IllegalStateException(getString(R.string.ErrorUnexpectedError));
        }
    });

    private Observable<String> currentCountryObservable = DatabaseHelperSecure.getObservable()
            .map(h -> {
                try {
                    return new SettingsBll(h);
                } catch (SQLException e) {
                    throw new IllegalStateException(getString(R.string.ErrorUnexpectedError));
                }
            })
            .map(bll -> {
                Settings setting = bll.getSettingsByKey(DatabaseConstants.ITEM_SETTINGS_COUNTRY);
                if (setting == null || setting.getValue() == null) {
                    throw new IllegalStateException(getString(R.string.ErrorUnexpectedError));
                }
                return setting.getValue();
            });

    @NonNull
    @Override
    protected ItemType getItemType() {
        return ItemType.Phone;
    }

    @Override
    int getTypeLayoutId() {
        return R.layout.fragment_secure_item_phone;
    }

    @OnClick(R.id.fr_si_country)
    void onClickCountry() {
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_COUNTRY));
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventCountrySelected(CountrySelectedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        Country country = event.getCountry();
        mCountryView.setText(country.getName());
        mCountryCodeView.setText(country.getDialingCode());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Observable.zip(countryListObservable, currentCountryObservable, (countryList, currentCountry) -> {
            for (Country country : countryList) {
                if (currentCountry.equalsIgnoreCase(country.getCode())) return country;
            }
            throw new IllegalStateException(getString(R.string.ErrorUnexpectedError));
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Country>(getActivity()) {
                    @Override
                    public void onNext(Country country) {
                        mCountryCodeView.setText(country.getDialingCode());
                        mCountryView.setText(country.getName());
                    }
                });
    }

    @Override
    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateData(item, properties);
        item.setName(mNameView.getText().toString());
        properties.setString(Identifier.COUNTRY, mCountryView);
        properties.setString(Identifier.PHONE_NUMBER, mPhoneView);
        properties.setString(Identifier.PHONE_CODE, mCountryCodeView.getText().toString());
    }

    @Override
    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        super.populateViews(item, properties);
        mNameView.setText(item.getName());
        mCountryCodeView.setText(properties.getString(Identifier.COUNTRY));
        mPhoneView.setText(properties.getString(Identifier.PHONE_NUMBER));
        mCountryCodeView.setText(properties.getString(Identifier.PHONE_CODE));
    }

}
