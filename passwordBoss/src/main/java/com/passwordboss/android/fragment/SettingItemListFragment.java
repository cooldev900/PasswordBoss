package com.passwordboss.android.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.passwordboss.android.R;
import com.passwordboss.android.activity.SettingItemListActivity;
import com.passwordboss.android.adapter.SettingsItemAdapter;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.Settings;
import com.passwordboss.android.database.beans.StorageRegion;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.CountryBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.database.bll.StorageRegionBll;
import com.passwordboss.android.database.bll.UserInfoBll;
import com.passwordboss.android.event.AutoLockTimeSelectedEvent;
import com.passwordboss.android.event.CardTypeSelectedEvent;
import com.passwordboss.android.event.CountrySelectedEvent;
import com.passwordboss.android.event.LanguageSelectedEvent;
import com.passwordboss.android.event.StorageRegionSelectedEvent;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.LanguagesUtils;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.passwordboss.android.fragment.SettingsFragment.AutoLockTimeLimitValue;

public class SettingItemListFragment extends ToolbarFragment {
    private static final String ARG_TYPE = "arg_type";
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingItemListFragment.class);
    private ConfigurationBll mConfigurationBll;
    private DatabaseHelperNonSecure mHelperNonSecure;
    private DatabaseHelperSecure mHelperSecure;
    private SettingsBll mSettingsBll;
    private ViewHolder mViewHolder;

    public static SettingItemListFragment newInstance(int type) {
        SettingItemListFragment f = new SettingItemListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        f.setArguments(args);
        return f;
    }

    @Nullable
    private CharSequence determineTitle() {
        int type = getArguments().getInt(ARG_TYPE, 0);
        switch (type) {
            case SettingItemListActivity.TYPE_COUNTRY:
                return getText(R.string.SelectCountry);
            case SettingItemListActivity.TYPE_LANGUAGE:
                return getText(R.string.SelectLanguageMessage);
            case SettingItemListActivity.TYPE_AUTO_LOCK_TIME:
                return getText(R.string.AutoLockTime);
            case SettingItemListActivity.TYPE_STORAGE_REGION:
                return getText(R.string.DataStorageDesc);
            case SettingItemListActivity.TYPE_CREDIT_CARDS:
                return getText(R.string.SelectCardType);
            default:
                return null;
        }
    }

    @NonNull
    private Observable<RecyclerView.Adapter> getAdapterForCountry() {
        return Observable.defer(() -> {
            try {
                return Observable.just(new CountryBll(mHelperNonSecure).getAllCountry());
            } catch (SQLException e) {
                return Observable.error(e);
            }
        })
                .flatMap(l -> Observable.just(l)
                        .flatMapIterable(list -> list)
                        .map(Country::getName)
                        .toList()
                        .map(countryStringList -> new Pair<>(l, countryStringList)))
                .map(pair -> (RecyclerView.Adapter) new SettingsItemAdapter(getActivity(), getCurrentCountry(pair.first).getName(), pair.second, v -> {
                    Integer position = (Integer) v.getTag();
                    Country country = pair.first.get(position);
                    EventBus.getDefault().postSticky(new CountrySelectedEvent(country));
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Observable<RecyclerView.Adapter> getAdapterForCreditCardTypes() {
        return Observable.defer(() -> {
            ArrayList<String> cardTypeList = new ArrayList<>();
            cardTypeList.add(getString(R.string.VisaCategory));
            cardTypeList.add(getString(R.string.MasterCardCategory));
            cardTypeList.add(getString(R.string.credit_card_american_express));
            cardTypeList.add(getString(R.string.credit_card_jcb));
            cardTypeList.add(getString(R.string.credit_card_discover));
            cardTypeList.add(getString(R.string.credit_card_other));

            SettingsItemAdapter adapter = new SettingsItemAdapter(getActivity(), cardTypeList.get(0), cardTypeList, v -> {
                Integer position = (Integer) v.getTag();
                String cardType = cardTypeList.get(position);
                EventBus.getDefault().postSticky(new CardTypeSelectedEvent(cardType));
            });
            return Observable.just(adapter);
        });
    }

    @NonNull
    private Observable<RecyclerView.Adapter> getAdapterForLanguage() {
        return Observable.defer(() -> Observable.just(new LanguagesUtils(getContext()).getListLanguages()))
                .map(l -> (RecyclerView.Adapter) new SettingsItemAdapter(getActivity(), getCurrentLanguage(), l, v -> {
                    Integer position = (Integer) v.getTag();
                    String language = l.get(position);
                    EventBus.getDefault().postSticky(new LanguageSelectedEvent(language));
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Observable<RecyclerView.Adapter> getAdapterForStorageRegion() {
        return Observable.defer(() -> {
            try {
                return Observable.just(new StorageRegionBll(mHelperSecure).getAllStorageRegion());
            } catch (SQLException e) {
                return Observable.error(e);
            }
        })
                .flatMap(l -> Observable.from(l)
                        .map(StorageRegion::getName)
                        .toList()
                        .map(stringList -> new Pair<>(l, stringList)))
                .map(pair -> (RecyclerView.Adapter) new SettingsItemAdapter(getActivity(), getCurrentStorageRegion(pair.first), pair.second, v -> {
                    Integer position = (Integer) v.getTag();
                    StorageRegion storageRegion = pair.first.get(position);
                    EventBus.getDefault().postSticky(new StorageRegionSelectedEvent(storageRegion));
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Observable<RecyclerView.Adapter> getAdapterForTimeValues() {
        return Observable.defer(() -> Observable.from(Utils.getAutoLockTimeValueArray(getResources())))
                .toList()
                .flatMap(list -> Observable.from(list)
                        .map(item -> item.Title)
                        .toList()
                        .map(stringList -> new Pair<>(list, stringList)))
                .map(pair -> (RecyclerView.Adapter) new SettingsItemAdapter(getActivity(), getCurrentTimeValue(), pair.second, v -> {
                    Integer position = (Integer) v.getTag();
                    AutoLockTimeLimitValue selectedItem = pair.first.get(position);
                    EventBus.getDefault().postSticky(new AutoLockTimeSelectedEvent(selectedItem));
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Nullable
    private AutoLockTimeLimitValue getAutoLockTimeLimit(int value) {
        AutoLockTimeLimitValue[] values = Utils.getAutoLockTimeValueArray(getResources());
        for (AutoLockTimeLimitValue autoLockTimeLimitValue : values) {
            if (autoLockTimeLimitValue.Value == value) return autoLockTimeLimitValue;
        }
        return null;
    }

    @Nullable
    private Country getCurrentCountry(List<Country> countries) {
        if (null == mSettingsBll) return null;
        Settings countrySetting = mSettingsBll.getSettingsByKey(DatabaseConstants.ITEM_SETTINGS_COUNTRY);
        if (null == countrySetting) return null;
        String countryCode = countrySetting.getValue();
        if (null == countryCode) return null;
        for (Country country : countries) {
            if (countryCode.equalsIgnoreCase(country.getCode())) return country;
        }
        return null;
    }

    @Nullable
    private String getCurrentLanguage() {
        Configuration configuration = mConfigurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, DatabaseConstants.LANGUAGE);
        return null == configuration ? null : configuration.getValue();
    }

    @Nullable
    private String getCurrentStorageRegion(List<StorageRegion> regionList) {
        if (null == mHelperSecure) return null;
        try {
            UserInfoBll userInfoBll = new UserInfoBll(mHelperSecure);
            UserInfo userInfo = userInfoBll.getUserInfoByEmail(Pref.EMAIL);
            if (null == userInfo) return null;
            StorageRegion userStorageRegion = userInfo.getStorageRegionUuid();
            if (null == userStorageRegion) return null;
            String uuid = userStorageRegion.getUuid();
            if (null == uuid) return null;
            for (StorageRegion storageRegion : regionList) {
                if (uuid.equals(storageRegion.getUuid())) return storageRegion.getName();
            }
        } catch (SQLException e) {
            LOGGER.error("getCurrentStorageRegion", e);
        }
        return null;
    }

    @Nullable
    private String getCurrentTimeValue() {
        AutoLockTimeLimitValue autoLockTimeLimit = getAutoLockTimeLimit(mConfigurationBll.getAutoLockValue(Pref.EMAIL));
        return autoLockTimeLimit != null ? autoLockTimeLimit.Title : null;
    }

    @NonNull
    private Observable<Integer> initializeHelpers() {
        return Observable.defer(() -> Observable.just("Starting initialization")
                .map(str -> {
                    try {
                        mHelperSecure = DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY);
                        mHelperNonSecure = DatabaseHelperNonSecure.getHelper(getContext());
                        mConfigurationBll = new ConfigurationBll(mHelperNonSecure);
                        mSettingsBll = new SettingsBll(mHelperSecure);
                    } catch (SQLException e) {
                        LOGGER.error("onViewCreated", e);
                        throw new IllegalStateException("Can not initialize helpers");
                    }

                    Bundle arguments = getArguments();
                    if (arguments == null ||
                            !arguments.containsKey(ARG_TYPE) ||
                            arguments.getInt(ARG_TYPE, SettingItemListActivity.TYPE_INVALID) == SettingItemListActivity.TYPE_INVALID) {
                        throw new IllegalArgumentException("Type of settings list should be passed");
                    }

                    return arguments.getInt(ARG_TYPE);
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        } else {
            toolbar.displayCloseNavigation();
        }
        toolbar.setTitle(determineTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewHolder = new ViewHolder(view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mViewHolder.settingList.setLayoutManager(llm);

        initializeHelpers()
                .subscribe(this::showSettingListByType, t -> LOGGER.error("onViewCreated", t));
    }

    private void showSettingListByType(int type) {
        Observable<RecyclerView.Adapter> adapterObservable;
        switch (type) {
            case SettingItemListActivity.TYPE_LANGUAGE: {
                adapterObservable = getAdapterForLanguage();
                break;
            }
            case SettingItemListActivity.TYPE_COUNTRY: {
                adapterObservable = getAdapterForCountry();
                break;
            }
            case SettingItemListActivity.TYPE_AUTO_LOCK_TIME: {
                adapterObservable = getAdapterForTimeValues();
                break;
            }
            case SettingItemListActivity.TYPE_STORAGE_REGION: {
                adapterObservable = getAdapterForStorageRegion();
                break;
            }
            case SettingItemListActivity.TYPE_CREDIT_CARDS: {
                adapterObservable = getAdapterForCreditCardTypes();
                break;
            }
            default: {
                throw new IllegalStateException("Adapter not initialized");
            }
        }

        adapterObservable.subscribe(adapter -> {
            mViewHolder.settingList.setAdapter(adapter);
        }, throwable -> LOGGER.error("Error during fetching countries", throwable));
    }

    public static final class ViewHolder {

        public RecyclerView settingList;

        public ViewHolder(View rootView) {
            settingList = (RecyclerView) rootView.findViewById(R.id.fr_set_setting_list);
        }
    }
}
