package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.database.bll.SecureItemDataBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.event.ChooseIconColorResultEvent;
import com.passwordboss.android.event.ChooseIconColorStartEvent;
import com.passwordboss.android.event.FolderChooseEvent;
import com.passwordboss.android.event.FolderChooseResultEvent;
import com.passwordboss.android.event.SecureItemCreatedEvent;
import com.passwordboss.android.event.SecureItemSavedEvent;
import com.passwordboss.android.helper.SoftKeyboard;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.widget.AppItemIconView;
import com.passwordboss.android.widget.AppPasswordStrengthView;
import com.passwordboss.android.widget.AppPasswordView;
import com.passwordboss.android.widget.AppSecurityItemNotesView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

abstract public class SecureItemFragment extends ToolbarFragment implements Changeable {

    private static final String KEY_ID = "keyId";
    private static final String KEY_SECURE_ITEM = "keySecureItem";
    private static final String KEY_SECURE_ITEM_PROPERTIES = "keySecureItemProperties";
    private static final String KEY_HASH_CODE = "keyHashCode";
    @Nullable
    @Bind(R.id.fr_si_folder)
    TextView mFolderView;
    @Bind(R.id.fr_si_icon)
    AppItemIconView mIconView;
    @Nullable
    @Bind(R.id.fr_si_notes)
    AppSecurityItemNotesView mNotesView;
    @Nullable
    @Bind(R.id.fr_si_password_strength)
    AppPasswordStrengthView mPasswordStrengthView;
    @Nullable
    @Bind(R.id.fr_si_password)
    AppPasswordView mPasswordView;
    private Folder mFolder;
    private int mHashCode;
    private SecureItem mSecureItem;
    private SecureItemProperties mSecureItemProperties;
    private View mTypeLayoutView;

    @Nullable
    private static SecureItemFragment createFragment(ItemType itemType) {
        if (null == itemType) return null;
        SecureItemFragment fragment;
        switch (itemType) {
            case Address:
                fragment = new SecureItemAddressFragment();
                break;
            case Alarm:
                fragment = new SecureItemAlarmFragment();
                break;
            case Application:
                fragment = new SecureItemApplicationFragment();
                break;
            case BankAccount:
                fragment = new SecureItemBankAccountFragment();
                break;
            case Company:
                fragment = new SecureItemCompanyFragment();
                break;
            case CreditCard:
                fragment = new SecureItemCreditCardFragment();
                break;
            case Database:
                fragment = new SecureItemDatabaseFragment();
                break;
            case DriversLicense:
                fragment = new SecureItemDriversLicenseFragment();
                break;
            case Email:
                fragment = new SecureItemEmailFragment();
                break;
            case EmailAccount:
                fragment = new SecureItemEmailAccountFragment();
                break;
            case EstatePlan:
                fragment = new SecureItemEstatePlanFragment();
                break;
            case FrequentFlyer:
                fragment = new SecureItemFrequentFlyerFragment();
                break;
            case HealthInsurance:
                fragment = new SecureItemHealthInsuranceFragment();
                break;
            case HotelRewards:
                fragment = new SecureItemHotelRewardsFragment();
                break;
            case InstantMessenger:
                fragment = new SecureItemInstantMessengerFragment();
                break;
            case Insurance:
                fragment = new SecureItemInsuranceFragment();
                break;
            case MemberId:
                fragment = new SecureItemMemberIdFragment();
                break;
            case Name:
                fragment = new SecureItemNameFragment();
                break;
            case Note:
                fragment = new SecureItemNoteFragment();
                break;
            case Passport:
                fragment = new SecureItemPassportFragment();
                break;
            case Phone:
                fragment = new SecureItemPhoneFragment();
                break;
            case Prescription:
                fragment = new SecureItemPrescriptionFragment();
                break;
            case SshKey:
                fragment = new SecureItemSshKeyFragment();
                break;
            case Server:
                fragment = new SecureItemServerFragment();
                break;
            case SocialSecurity:
                fragment = new SecureItemSocialSecurityFragment();
                break;
            case SoftwareLicense:
                fragment = new SecureItemSoftwareLicenseFragment();
                break;
            case Website:
                fragment = new SecureItemWebsiteFragment();
                break;
            case WiFi:
                fragment = new SecureItemWiFiFragment();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Nullable
    public static SecureItemFragment newInstance(ItemType itemType, String id) {
        SecureItemFragment fragment = createFragment(itemType);
        if (null == fragment) return null;
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    public static SecureItemFragment newInstance(ItemType itemType) {
        return createFragment(itemType);
    }

    public static SecureItemFragment newInstance(SecureItem secureItem) {
        ItemType itemType = ItemType.from(secureItem);
        SecureItemFragment fragment = createFragment(itemType);
        if (null == fragment) return null;
        Bundle args = new Bundle();
        if (secureItem.isNew()) {
            args.putSerializable(KEY_SECURE_ITEM, secureItem); // pre-filled new entity
        } else {
            args.putString(KEY_ID, secureItem.getId()); // use id to load actual data from db during loading screen
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    private String getItemId() {
        Bundle arguments = getArguments();
        if (null == arguments) return null;
        return arguments.getString(KEY_ID);
    }

    @NonNull
    abstract protected ItemType getItemType();

    @Nullable
    private SecureItem getPreFilledItem() {
        Bundle arguments = getArguments();
        if (null == arguments) return null;
        return (SecureItem) arguments.getSerializable(KEY_SECURE_ITEM);
    }

    @LayoutRes
    abstract int getTypeLayoutId();

    @Override
    public boolean hasChanges() {
        populateData(mSecureItem, mSecureItemProperties);
        return mHashCode != Objects.hashCode(mSecureItem, mSecureItemProperties);
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        toolbar.setTitle(Strings.isNullOrEmpty(getItemId()) ? R.string.AddNewItemCapitalCase : R.string.MenuEditItem);
        toolbar.displayCloseNavigation();
    }

    @OnClick(R.id.fr_si_button_save)
    void onClickButtonSave() {
        if (!validate()) return;
        populateData(mSecureItem, mSecureItemProperties);
        new ProgressDialog(this).show(getText(R.string.SavingData));
        DatabaseHelperSecure
                .getObservable()
                .map(helperSecure -> {
                    try {
                        SecureItemSavedEvent event;
                        if (mSecureItem.isNew()) {
                            event = new SecureItemCreatedEvent(mSecureItem);
                            mSecureItem.setId(UUID.randomUUID().toString());
                            mSecureItem.setActive(true);
                            mSecureItem.setCreatedDateNow();
                        } else {
                            event = new SecureItemSavedEvent(mSecureItem);
                        }
                        mSecureItem.setLastModifiedDateNow();
                        SecureItemBll itemBll = new SecureItemBll(helperSecure);
                        itemBll.insertOrUpdateRow(mSecureItem);
                        SecureItemDataBll dataBll = new SecureItemDataBll(helperSecure);
                        dataBll.deleteAll(mSecureItem);
                        Collection<SecureItemData> values = mSecureItemProperties.mSecureItemData.values();
                        for (SecureItemData value : values) {
                            value.setSecureItemId(mSecureItem);
                            value.setCreatedDateNow();
                            value.setLastModifiedDateNow();
                            dataBll.insertRow(value);
                        }
                        return event;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    new ProgressDialog(this).dismiss();
                    EventBus.getDefault().postSticky(event);
                });
    }

    @OnClick({R.id.fr_si_choose_icon_color, R.id.fr_si_icon})
    void onClickChooseIconColor() {
        EventBus.getDefault().post(new ChooseIconColorStartEvent());
    }

    @SuppressWarnings("NullableProblems")
    @Nullable
    @OnClick(R.id.fr_si_folder)
    void onClickFolder() {
        EventBus.getDefault().post(new FolderChooseEvent(null, true));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secure_item, null);
        ViewStub viewStub = (ViewStub) view.findViewById(R.id.fr_si_layout_of_type);
        viewStub.setLayoutResource(getTypeLayoutId());
        mTypeLayoutView = viewStub.inflate();
        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseIconColorResultEvent event) {
        mIconView.setIconColor(event.Color);
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(FolderChooseResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        setFolder(event.getFolder());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        populateData(mSecureItem, mSecureItemProperties);
        outState.putSerializable(KEY_SECURE_ITEM, mSecureItem);
        outState.putSerializable(KEY_SECURE_ITEM_PROPERTIES, mSecureItemProperties);
        outState.putInt(KEY_HASH_CODE, mHashCode);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (null != mPasswordView
                && null != mPasswordStrengthView) {
            mPasswordStrengthView.linkTo(mPasswordView);
        }
        if (null != savedInstanceState) {
            mSecureItem = (SecureItem) savedInstanceState.getSerializable(KEY_SECURE_ITEM);
            mSecureItemProperties = (SecureItemProperties) savedInstanceState.getSerializable(KEY_SECURE_ITEM_PROPERTIES);
            mHashCode = savedInstanceState.getInt(KEY_HASH_CODE);
            return;
        }
        mSecureItemProperties = new SecureItemProperties();
        DatabaseHelperSecure
                .getObservable() // to be sure that secure db is unlocked
                .flatMap(helperSecure -> {
                    mSecureItem = getPreFilledItem(); // special case, when screen works with some pre-filled value (recommended site, etc.)
                    if (null == mSecureItem) {
                        String id = getItemId();
                        if (null != id) {
                            try {
                                SecureItemBll itemBll = new SecureItemBll(helperSecure);
                                mSecureItem = itemBll.findSecureItemById(id);
                                SecureItemDataBll dataBll = new SecureItemDataBll(helperSecure);
                                List<SecureItemData> list = dataBll.getSecureItemDataList(id);
                                for (SecureItemData l : list) {
                                    mSecureItemProperties.mSecureItemData.put(l.getIdentifier(), l);
                                }
                            } catch (SQLException e) {
                                return Observable.error(e);
                            }
                        }
                        if (null == mSecureItem) {
                            mSecureItem = new SecureItem();
                            mSecureItem.setItemType(getItemType());
                        }
                    }
                    return Observable.just(true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Boolean>(getContext()) {
                    @Override
                    public void onNext(Boolean ready) {
                        if (mSecureItem.isNew()) {
                            requestFocusAtFirstInputField();
                        }
                        populateViews(mSecureItem, mSecureItemProperties);
                        mHashCode = Objects.hashCode(mSecureItem, mSecureItemProperties);
                    }
                });
    }

    protected void populateData(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        item.setFolder(mFolder);
        item.setColor(mIconView.getIconColor());
        if (null != mPasswordView) {
            properties.setString(SecureItemData.Identifier.PASSWORD, mPasswordView.getText().toString());
        }
        if (null != mNotesView) {
            properties.setString(SecureItemData.Identifier.NOTES, mNotesView.getText().toString());
        }
    }

    public void populateViews(@NonNull SecureItem item, @NonNull SecureItemProperties properties) {
        setFolder(item.getFolder());
        mIconView.setItemType(getItemType(), item.getColor());
        if (null != mPasswordView) {
            mPasswordView.setText(properties.getString(SecureItemData.Identifier.PASSWORD));
        }
        if (null != mNotesView) {
            mNotesView.setText(properties.getString(SecureItemData.Identifier.NOTES));
        }
    }

    private void requestFocusAtFirstInputField() {
        ArrayList<View> focusables = mTypeLayoutView.getFocusables(View.FOCUS_FORWARD);
        if (null == focusables || focusables.size() == 0) return;
        new SoftKeyboard().showImplicit(focusables.get(0));
    }

    private void setFolder(Folder folder) {
        mFolder = folder;
        if (null != mFolder && null != mFolderView) {
            mFolderView.setText(mFolder.getName());
        }
    }

    protected boolean validate() {
        return FormValidator.validate(this, new SimpleErrorPopupCallback(getContext(), true));
    }

    protected static class SecureItemProperties implements Serializable {
        private final HashMap<String, SecureItemData> mSecureItemData = new HashMap<>();

        public boolean getBoolean(String key, boolean defaultValue) {
            final SecureItemData data = getItemData(key);
            if (null == data) return defaultValue;
            return "1".equals(data.getValue());
        }

        @Nullable
        private SecureItemData getItemData(String key) {
            return mSecureItemData.get(key);
        }

        @Nullable
        public String getString(String key) {
            final SecureItemData data = getItemData(key);
            if (null == data) return null;
            return data.getValue();
        }

        @Override
        public int hashCode() {
            ArrayList<SecureItemData> listToHash = new ArrayList<>();
            for (SecureItemData itemData : mSecureItemData.values()) {
                if (Strings.isNullOrEmpty(itemData.getValue())) continue;
                listToHash.add(itemData);
            }
            SecureItemData[] objectsToHash = new SecureItemData[listToHash.size()];
            listToHash.toArray(objectsToHash);
            return Objects.hashCode(objectsToHash);
        }

        public void setBoolean(String key, boolean value) {
            setString(key, value ? "1" : "0");
        }

        public void setString(String key, EditText editText) {
            setString(key, editText.getText().toString());
        }

        public void setString(String key, String value) {
            SecureItemData data = getItemData(key);
            if (null == data) {
                data = new SecureItemData();
                data.setActive(true);
                data.setIdentifier(key);
                mSecureItemData.put(key, data);
            }
            data.setValue(value);
        }
    }


}
