package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.passwordboss.android.BuildConfig;
import com.passwordboss.android.R;
import com.passwordboss.android.activity.MainActivity;
import com.passwordboss.android.activity.SettingItemListActivity;
import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.beans.Settings;
import com.passwordboss.android.database.beans.StorageRegion;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.beans.UserSubscription;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.CountryBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.database.bll.StorageRegionBll;
import com.passwordboss.android.database.bll.UserInfoBll;
import com.passwordboss.android.database.bll.UserSubscriptionBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.dialog.alert.InfoDialog;
import com.passwordboss.android.event.AutoLockTimeSelectedEvent;
import com.passwordboss.android.event.AutoLockTimeUpdatedEvent;
import com.passwordboss.android.event.BaseEvent;
import com.passwordboss.android.event.ChangeMasterPasswordEvent;
import com.passwordboss.android.event.ChangePinEvent;
import com.passwordboss.android.event.CheckPinEvent;
import com.passwordboss.android.event.CountrySelectedEvent;
import com.passwordboss.android.event.FolderManageEvent;
import com.passwordboss.android.event.ForceChangePinEvent;
import com.passwordboss.android.event.GeneratePasswordEvent;
import com.passwordboss.android.event.LanguageSelectedEvent;
import com.passwordboss.android.event.PinChangeResultEvent;
import com.passwordboss.android.event.PinCheckResultEvent;
import com.passwordboss.android.event.RegisteredDevicesEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.event.StorageRegionSelectedEvent;
import com.passwordboss.android.event.SyncResultEvent;
import com.passwordboss.android.event.TwoStepVerificationValueUpdatedEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.event.UpgradeAccountEvent;
import com.passwordboss.android.helper.EnableChildren;
import com.passwordboss.android.helper.NetworkState;
import com.passwordboss.android.helper.SafeIntent;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.UnexpectedServerError;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.http.beans.StorageRegionHttpPost;
import com.passwordboss.android.pin.PinStore;
import com.passwordboss.android.pin.PinStoreOld;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.subscription.SubscriptionValidator;
import com.passwordboss.android.task.SyncTask;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.LanguagesUtils;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class SettingsFragment extends ToolbarFragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsFragment.class);
    private static final String URL_SUPPORT = "https://www.passwordboss.com/support/?utm_source=Android&utm_medium=Menu&utm_campaign=InAppSupport";
    private static final String URL_LEGAL = "https://www.passwordboss.com/legal/android";
    @Bind(R.id.fr_st_auto_lock_time_limit_value)
    TextView mAutoLockTimeLimitView;
    @Bind(R.id.fr_st_button_backup_now)
    Button mBackupNowButton;
    @Bind(R.id.fr_st_change_pin)
    TextView mChangePinView;
    @Bind(R.id.fr_st_cloud_backup)
    SwitchCompat mCloudBackupView;
    @Bind(R.id.fr_st_country_value)
    TextView mCountryView;
    @Bind(R.id.fr_st_enable_pin)
    SwitchCompat mEnablePinView;
    @Bind(R.id.fr_st_language_value)
    TextView mLanguageView;
    @Bind(R.id.fr_st_last_backup)
    TextView mLastBackupView;
    @Bind(R.id.fr_st_remember_email_after_logout)
    SwitchCompat mRememberEmailAfterLogoutView;
    @Bind(R.id.fr_st_storage_region)
    LinearLayout mStorageRegionRowView;
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListenerCloudBackup = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            new ProgressDialog(SettingsFragment.this).showLoading();
            new SaveCloudBackupSettingTask(buttonView.getContext(), isChecked).start();
            mBackupNowButton.setEnabled(isChecked);
            new EnableChildren().process(mStorageRegionRowView, isChecked);
        }
    };
    @Bind(R.id.fr_st_storage_region_value)
    TextView mStorageRegionView;
    @Bind(R.id.fr_st_button_subscription)
    Button mSubscriptionButton;
    @Bind(R.id.fr_st_subscription_type)
    TextView mSubscriptionTypeView;
    @Bind(R.id.fr_st_trusted_device)
    SwitchCompat mTrustedDeviceView;
    @Bind(R.id.fr_st_two_step_verification)
    SwitchCompat mTwoStepVerificationView;
    @Bind(R.id.fr_st_valid_trough)
    TextView mValidThroughView;
    @Bind(R.id.fr_st_version)
    TextView mVersionView;
    private AutoLockTimeLimitValue[] mAutoLockTimeLimitValues;
    private boolean mBackupRequested = false;
    private ConfigurationBll mConfigurationBll;
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListenerRememberEmail = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (null == mConfigurationBll) return;
            mConfigurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.REMEMBER_EMAIL, isChecked ? "1" : "0");
        }
    };
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListenerTwoStepVerification = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked && !SubscriptionValidator.isFeatureValid(SubscriptionValidator.FEATURE_2_STEP_AUTHENTICATION, buttonView.getContext(), true)) {
                mTwoStepVerificationView.setChecked(false);
                return;
            }
            if (isChecked) {
                // TODO: 4/26/2016 move this into end of wizard
                AnalyticsHelperSegment.logTwoFactorVerification(getContext(), AnalyticsHelperSegment.STATUS_ENABLE);
                mTwoStepVerificationView.setChecked(false); // left is unchecked, until user will go trough wizard
                EventBus.getDefault().post(new TwoStepVerificationWizardNextStepEvent(TwoStepVerificationWizardNextStepEvent.Step.Start));
            } else {
                AnalyticsHelperSegment.logTwoFactorVerification(getContext(), AnalyticsHelperSegment.STATUS_DISABLED);
                if (null != mConfigurationBll) {
                    mConfigurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION, "0");
                }
                mTrustedDeviceView.setEnabled(false);
            }
        }
    };
    private Country[] mCountries;
    private DatabaseHelperNonSecure mHelperNonSecure;
    private DatabaseHelperSecure mHelperSecure;
    private PinStore mPinStore;
    CompoundButton.OnCheckedChangeListener enablePinListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mChangePinView.setEnabled(isChecked);
            if (isChecked) {
                EventBus.getDefault().post(new ForceChangePinEvent());
                mPinStore.enable();
            } else {
                EventBus.getDefault().post(new CheckPinEvent());
            }
        }
    };
    private SettingsBll mSettingsBll;
    private StorageRegion[] mStorageRegions;

    @Nullable
    private AutoLockTimeLimitValue getAutoLockTimeLimit(int value) {
        for (AutoLockTimeLimitValue autoLockTimeLimitValue : mAutoLockTimeLimitValues) {
            if (autoLockTimeLimitValue.Value == value) return autoLockTimeLimitValue;
        }
        return null;
    }

    @Nullable
    private Country getCurrentCountry() {
        if (null == mSettingsBll) return null;
        Settings countrySetting = mSettingsBll.getSettingsByKey(DatabaseConstants.ITEM_SETTINGS_COUNTRY);
        if (null == countrySetting) return null;
        String countryCode = countrySetting.getValue();
        if (null == countryCode) return null;
        for (Country country : mCountries) {
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
    private StorageRegion getCurrentStorageRegion() {
        if (null == mHelperSecure) return null;
        try {
            UserInfoBll userInfoBll = new UserInfoBll(mHelperSecure);
            UserInfo userInfo = userInfoBll.getUserInfoByEmail(Pref.EMAIL);
            if (null == userInfo) return null;
            StorageRegion userStorageRegion = userInfo.getStorageRegionUuid();
            if (null == userStorageRegion) return null;
            String uuid = userStorageRegion.getUuid();
            if (null == uuid) return null;
            for (StorageRegion storageRegion : mStorageRegions) {
                if (uuid.equals(storageRegion.getUuid())) return storageRegion;
            }
        } catch (SQLException e) {
            LOGGER.error("getCurrentStorageRegion", e);
        }
        return null;
    }

    @Nullable
    private UserSubscription getUserSubscription() {
        try {
            DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(getActivity(), Pref.DATABASE_KEY);
            UserSubscriptionBll userSubscriptionBll = new UserSubscriptionBll(helperSecure);
            return userSubscriptionBll.getUserInfoByEmail(Pref.EMAIL);
        } catch (Exception e) {
            LOGGER.error("getUserSubscription", e);
        }
        return null;
    }

    private void initializeAutoLockTimeLimitValues() {
        mAutoLockTimeLimitValues = Utils.getAutoLockTimeValueArray(getResources());
    }

    private void initializeCountries() {
        if (null == mHelperNonSecure) return;
        try {
            CountryBll countryBll = new CountryBll(mHelperNonSecure);
            List<Country> list = countryBll.getAllCountry();
            mCountries = new Country[list.size()];
            list.toArray(mCountries);
        } catch (SQLException e) {
            LOGGER.error("initializeCountries", e);
        }
    }

    private void initializeStorageRegions() {
        if (null == mHelperSecure) return;
        try {
            StorageRegionBll storageRegionBll = new StorageRegionBll(mHelperSecure);
            List<StorageRegion> list = storageRegionBll.getAllStorageRegion();
            mStorageRegions = new StorageRegion[list.size()];
            list.toArray(mStorageRegions);
        } catch (SQLException e) {
            LOGGER.error("initializeStorageRegions", e);
        }
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        toolbar.setTitle(R.string.Settings);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        }
    }

    @OnClick(R.id.fr_st_auto_lock_time_limit)
    void onClickAutoLockTimeLimit() {
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_AUTO_LOCK_TIME));
    }

    @OnClick(R.id.fr_st_button_backup_now)
    void onClickButtonBackupNow() {
        if (!SubscriptionValidator.isFeatureValid(SubscriptionValidator.FEATURE_ONLINE_BACKUP, getContext(), true)) {
            return;
        }
        if (new NetworkState(getContext()).isDisconnected()) {
            new ErrorDialog(getContext()).show(getString(R.string.NoInternetConnection), null);
            return;
        }
        new ProgressDialog(this).show(getText(R.string.BackingUp));
        mBackupRequested = true;
        new SyncTask(getContext()).start();
    }

    @OnClick(R.id.fr_st_button_subscription)
    void onClickButtonSubscription() {
        EventBus.getDefault().post(new UpgradeAccountEvent());
    }

    @OnClick(R.id.fr_st_change_master_password)
    void onClickChangeMasterPassword() {
        EventBus.getDefault().post(new ChangeMasterPasswordEvent());
    }

    @OnClick(R.id.fr_st_change_pin)
    void onClickChangePin() {
        EventBus.getDefault().post(new ChangePinEvent());
    }

    @OnClick(R.id.fr_st_country)
    void onClickCountry() {
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_COUNTRY));
    }

    @OnClick(R.id.fr_st_devices)
    void onClickDevices() {
        EventBus.getDefault().post(new RegisteredDevicesEvent());
    }

    @OnClick(R.id.fr_st_language)
    void onClickLanguage() {
        if (null == mConfigurationBll) return;
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_LANGUAGE));
    }

    @OnClick(R.id.fr_st_legal)
    void onClickLegal() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_LEGAL));
        if (new SafeIntent(getContext(), intent).check()) startActivity(intent);
    }

    @OnClick(R.id.fr_st_manage_folders)
    void onClickManageFolders() {
        EventBus.getDefault().post(new FolderManageEvent());
    }

    @OnClick(R.id.fr_st_password_generator)
    void onClickPasswordGenerator() {
        EventBus.getDefault().post(new GeneratePasswordEvent());
    }

    @OnClick(R.id.fr_st_storage_region)
    void onClickStorageRegion() {
        EventBus.getDefault().post(new SelectSettingEvent(SettingItemListActivity.TYPE_STORAGE_REGION));
    }

    @OnClick(R.id.fr_st_support_center)
    void onClickSupportCenter() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_SUPPORT));
        if (new SafeIntent(getContext(), intent).check()) startActivity(intent);
    }

    @OnClick(R.id.fr_st_trusted_device_info)
    void onClickTrustedDeviceInfo() {
        new InfoDialog(getContext()).show(getText(R.string.AccountSettingTrustedDeviceTooltipText));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventAutoLockTimeSelected(AutoLockTimeSelectedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        AutoLockTimeLimitValue value = event.getValue();
        if (null == mConfigurationBll) return;
        mConfigurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.AUTOLOCK, Integer.toString(value.Value));
        updateAutoLockTimeLimitView();
        EventBus.getDefault().post(new AutoLockTimeUpdatedEvent());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventCheckPinFinished(PinCheckResultEvent e) {
        EventBus.getDefault().removeStickyEvent(e);
        boolean isPinValid = e.isCorrect();
        mEnablePinView.setOnCheckedChangeListener(null);
        if (isPinValid) {
            mEnablePinView.setChecked(false);
            mPinStore.clear();
            mPinStore.disable();
        } else {
            mEnablePinView.setChecked(true);
        }
        mEnablePinView.setOnCheckedChangeListener(enablePinListener);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventCountrySelected(CountrySelectedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (null == mSettingsBll) return;
        Country country = event.getCountry();
        mSettingsBll.insertOrUpdate(DatabaseConstants.ITEM_SETTINGS_COUNTRY, country.getCode());
        updateCountryView();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventForceChangePinFinished(PinChangeResultEvent e) {
        EventBus.getDefault().removeStickyEvent(e);
        boolean hasPinSetUp = e.isPinAdded();
        if (!hasPinSetUp) {
            mEnablePinView.setChecked(false);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventLanguageSelected(LanguageSelectedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        String selectedLanguage = event.getSelectedLanguage();
        if (selectedLanguage.equals(getCurrentLanguage())) return;
        mConfigurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.LANGUAGE, selectedLanguage);
        new LanguagesUtils(getContext()).changeLanguage(selectedLanguage);
        MainActivity.start(getContext());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventSaveCloudBackupSettingResult(SaveCloudBackupSettingResultEvent event) {
        try {
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                boolean oldValue = null != event.OldValue && event.OldValue;
                updateSwitchWithoutChangeListener(mCloudBackupView, oldValue, mOnCheckedChangeListenerCloudBackup);
                mBackupNowButton.setEnabled(oldValue);
                new EnableChildren().process(mStorageRegionRowView, oldValue);
                new ErrorDialog(getContext()).show(event.getError());
            }
        } finally {
            new ProgressDialog(this).dismiss();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventSaveStorageRegionSettingResult(SaveStorageRegionSettingResultEvent event) {
        try {
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                new ErrorDialog(getContext()).show(event.getError());
                return;
            }
            updateStorageRegionView();
        } finally {
            new ProgressDialog(this).dismiss();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventStorageRegionSelected(StorageRegionSelectedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        StorageRegion region = event.getRegion();
        new ProgressDialog(SettingsFragment.this).showLoading();
        new SaveStorageRegionSettingTask(getContext(), region, mCloudBackupView.isChecked()).start();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventSyncResult(SyncResultEvent event) {
        try {
            EventBus.getDefault().removeStickyEvent(event);
            if (event.hasError()) {
                new ErrorDialog(getContext()).show(event.getError());
                return;
            }
            updateBackupViews();
            if (mBackupRequested) {
                Toast.makeText(getContext(), R.string.BackupSuccessfullyDone, Toast.LENGTH_LONG).show();
                mBackupRequested = false;
            }
        } finally {
            new ProgressDialog(this).dismiss();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventTwoStepVerificationValueUpdated(TwoStepVerificationValueUpdatedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (null == mConfigurationBll) return;
        boolean value = mConfigurationBll.valueEquals(Pref.EMAIL, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION, "1");
        updateSwitchWithoutChangeListener(mTwoStepVerificationView, value, mOnCheckedChangeListenerTwoStepVerification);
        mTrustedDeviceView.setEnabled(value);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        DatabaseHelperSecure
                .getObservable()
                .map(helperSecure -> {
                    try {
                        mHelperSecure = DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY);
                        mSettingsBll = new SettingsBll(mHelperSecure);
                        mPinStore = new PinStoreOld(getActivity(), new DeviceBll(mHelperSecure));
                        mHelperNonSecure = DatabaseHelperNonSecure.getHelper(getContext());
                        mConfigurationBll = new ConfigurationBll(mHelperNonSecure);
                        initializeStorageRegions();
                        initializeAutoLockTimeLimitValues();
                        initializeCountries();
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                    return null;
                })
                .subscribe(new BaseObserver<Object>(getContext()) {
                    @Override
                    public void onNext(Object o) {
                        updateViews();
                    }
                });
    }

    private void updateAutoLockTimeLimitView() {
        if (null == mConfigurationBll) return;
        AutoLockTimeLimitValue autoLockTimeLimit = getAutoLockTimeLimit(mConfigurationBll.getAutoLockValue(Pref.EMAIL));
        if (null == autoLockTimeLimit) return;
        mAutoLockTimeLimitView.setText(autoLockTimeLimit.Title);
    }

    private void updateBackupViews() {
        if (null == mHelperSecure || null == mSettingsBll) return;
        try {
            Settings settings = mSettingsBll.getSettingsByKey(DatabaseConstants.CLOUD_STORAGE_ENABLE);
            boolean cloudBackup = null != settings && Boolean.parseBoolean(settings.getValue());
            updateSwitchWithoutChangeListener(mCloudBackupView, cloudBackup, mOnCheckedChangeListenerCloudBackup);
            mBackupNowButton.setEnabled(cloudBackup);
            new EnableChildren().process(mStorageRegionRowView, cloudBackup);
            DeviceBll deviceBll = new DeviceBll(mHelperSecure);
            Device device = deviceBll.getDeviceByInstallationUuid(Pref.INSTALLATION_UUID);
            if (null == device) return;
            DateTimeFormatter formatter = DateTimeFormat.forPattern("EE MMM d, yyyy hh:mma");
            String latestSync = device.getLatestSync();
            if (TextUtils.isEmpty(latestSync)) return;
            DateTime dateTime = new DateTime(latestSync);
            String text = getText(R.string.LastBackup) + " " + dateTime.toString(formatter);
            mLastBackupView.setText(text);
        } catch (SQLException e) {
            LOGGER.error("updateBackupViews", e);
        }
    }

    private void updateCountryView() {
        Country country = getCurrentCountry();
        String text = null == country ? null : country.getName();
        mCountryView.setText(text);
    }

    private void updateLanguageView() {
        if (null == mConfigurationBll) return;
        mLanguageView.setText(getCurrentLanguage());
    }

    private void updateMyAccountViews() {
        UserSubscription userSubscription = getUserSubscription();
        if (null == userSubscription) return;
        int numberOfDays = Utils.calculateNumberOfDay(new DateTime(userSubscription.getExpirationDate(), DateTimeZone.UTC));
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, YYYY");
        mValidThroughView.setText(new DateTime(userSubscription.getExpirationDate(), DateTimeZone.UTC).toDateTimeISO().toString(formatter));
        if (Constants.PAID.equals(userSubscription.getName())) {
            mSubscriptionTypeView.setText(getResources().getString(R.string.Premium));
        } else {
            if (Constants.FREE.equals(userSubscription.getName()) || numberOfDays <= 0) {
                mSubscriptionTypeView.setText(getResources().getString(R.string.SubscriptionTypeFree));
                mValidThroughView.setText(getResources().getString(R.string.State_expired));
            } else {
                mSubscriptionTypeView.setText(getResources().getString(R.string.TrialPeriod));
            }
        }
        if (DatabaseConstants.Trial.equals(userSubscription.getName()) || DatabaseConstants.Free.equals(userSubscription.getName()) || numberOfDays <= 0) {
            mSubscriptionButton.setText(getString(R.string.Upgrade));
        } else if (userSubscription.getName().equals(DatabaseConstants.Paid) || userSubscription.getName().equals(DatabaseConstants.Donation)) {
            mSubscriptionButton.setText(getString(R.string.Renew));
        }
    }

    private void updateRememberEmailAfterLogoutView() {
        if (null == mConfigurationBll) return;
        final Configuration configuration = mConfigurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, DatabaseConstants.REMEMBER_EMAIL);
        boolean rememberEmail = configuration != null && configuration.valueEquals("1");
        updateSwitchWithoutChangeListener(mRememberEmailAfterLogoutView, rememberEmail, mOnCheckedChangeListenerRememberEmail);
    }

    private void updateSecurityViews() {
        if (null == mConfigurationBll) return;
        mEnablePinView.setChecked(mPinStore.isEnabled());
        mEnablePinView.setOnCheckedChangeListener(enablePinListener);
        mChangePinView.setEnabled(mEnablePinView.isChecked());
        mTwoStepVerificationView.setChecked(mConfigurationBll.valueEquals(Pref.EMAIL, DatabaseConstants.ADVANCED_TWO_STEP_VERIFICATION, "1"));
        mTwoStepVerificationView.setOnCheckedChangeListener(mOnCheckedChangeListenerTwoStepVerification);
        mTrustedDeviceView.setChecked(mConfigurationBll.valueEquals(Pref.EMAIL, DatabaseConstants.ADVANCED_TRUSTED_DEVICE, "1"));
        mTrustedDeviceView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mConfigurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ADVANCED_TRUSTED_DEVICE, isChecked ? "1" : "0");
            if (isChecked) {
                if (null != mConfigurationBll) {
                    mConfigurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ADVANCED_TRUSTED_DEVICE_DATE, DateTime.now().toString("MM/dd/yyyy HH:mm:ss"));
                    Toast.makeText(getContext(), R.string.DeviceAddedAsTrustedDevice, Toast.LENGTH_LONG).show();
                }
            }
        });
        mTrustedDeviceView.setEnabled(mTwoStepVerificationView.isChecked());
    }

    private void updateStorageRegionView() {
        StorageRegion storageRegion = getCurrentStorageRegion();
        String text = null == storageRegion ? null : storageRegion.getName();
        mStorageRegionView.setText(text);
    }

    private void updateSwitchWithoutChangeListener(SwitchCompat switchCompat, boolean value, CompoundButton.OnCheckedChangeListener listener) {
        switchCompat.setOnCheckedChangeListener(null);
        switchCompat.setChecked(value);
        switchCompat.setOnCheckedChangeListener(listener);
    }

    private void updateViews() {
        updateMyAccountViews();
        updateSecurityViews();
        updateBackupViews();
        updateStorageRegionView();
        updateAutoLockTimeLimitView();
        updateRememberEmailAfterLogoutView();
        updateLanguageView();
        updateCountryView();
        mVersionView.setText(BuildConfig.VERSION_NAME);
    }

    private static class SaveCloudBackupSettingResultEvent extends BaseEvent {
        public final Boolean OldValue;

        private SaveCloudBackupSettingResultEvent() {
            OldValue = null;
        }

        public SaveCloudBackupSettingResultEvent(Throwable throwable, boolean oldValue) {
            super(throwable);
            OldValue = oldValue;
        }
    }

    private static class SaveStorageRegionSettingResultEvent extends BaseEvent {
        public SaveStorageRegionSettingResultEvent(Throwable throwable) {
            super(throwable);
        }

        public SaveStorageRegionSettingResultEvent() {
        }
    }

    private static class SaveStorageRegionSettingTask extends Thread {
        private final boolean mCloudBackup;
        private final StorageRegion mStorageRegion;
        private final Context mContext;

        private SaveStorageRegionSettingTask(Context context, StorageRegion storageRegion, boolean cloudBackup) {
            mStorageRegion = storageRegion;
            mCloudBackup = cloudBackup;
            mContext = context;
        }

        @Override
        public void run() {
            try {
                runImpl();
                EventBus.getDefault().postSticky(new SaveStorageRegionSettingResultEvent());
            } catch (Exception e) {
                EventBus.getDefault().postSticky(new SaveStorageRegionSettingResultEvent(e));
            }
        }

        private void runImpl() throws Exception {
            DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            ServerAPI api = new ServerAPI();
            StorageRegionHttpPost regionHttpPost = new StorageRegionHttpPost(mCloudBackup, mStorageRegion.getUuid(), Pref.EMAIL, Pref.INSTALLATION_UUID);
            ServerResponseHttpBean response = api.storageRegionEndpointPatch(regionHttpPost);
            if (null != response.getError()) {
                throw new UnexpectedServerError(mContext, response.getError());
            }
            UserInfoBll userInfoBll = new UserInfoBll(helperSecure);
            UserInfo userInfo = userInfoBll.getUserInfoByEmail(Pref.EMAIL);
            userInfo.setStorageRegionUuid(mStorageRegion);
            userInfoBll.updateRow(userInfo);
        }
    }

    private static class SaveCloudBackupSettingTask extends Thread {
        private final boolean mCloudBackup;
        private final Context mContext;

        private SaveCloudBackupSettingTask(Context context, boolean cloudBackup) {
            mCloudBackup = cloudBackup;
            mContext = context;
        }

        @Override
        public void run() {
            try {
                runImpl();
                EventBus.getDefault().postSticky(new SaveCloudBackupSettingResultEvent());
            } catch (Exception e) {
                EventBus.getDefault().postSticky(new SaveCloudBackupSettingResultEvent(e, !mCloudBackup));
            }
        }

        private void runImpl() throws Exception {
            DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            SettingsBll settingsBll = new SettingsBll(helperSecure);
            ServerAPI api = new ServerAPI();
            StorageRegionHttpPost regionHttpPost = new StorageRegionHttpPost(mCloudBackup, Pref.EMAIL, Pref.INSTALLATION_UUID);
            ServerResponseHttpBean response = api.storageRegionEndpointPatch(regionHttpPost);
            if (null != response.getError()) {
                throw new UnexpectedServerError(mContext, response.getError());
            }
            settingsBll.insertOrUpdate(DatabaseConstants.CLOUD_STORAGE_ENABLE, Boolean.toString(mCloudBackup));
        }
    }

    public static class AutoLockTimeLimitValue {
        public final String Title;
        public final int Value;

        public AutoLockTimeLimitValue(String title, int value) {
            Title = title;
            Value = value;
        }

        @Override
        public String toString() {
            return Title;
        }
    }
}
