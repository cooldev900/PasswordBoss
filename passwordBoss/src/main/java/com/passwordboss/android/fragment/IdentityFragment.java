package com.passwordboss.android.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.UserIdentity;
import com.passwordboss.android.database.bll.UserIdentityBll;
import com.passwordboss.android.dialog.ProgressDialog;
import com.passwordboss.android.dialog.alert.DeleteItemDialog;
import com.passwordboss.android.event.AvatarChosenEvent;
import com.passwordboss.android.event.ChooseAvatarEvent;
import com.passwordboss.android.event.IdentityRemovedEvent;
import com.passwordboss.android.event.IdentitySavedEvent;
import com.passwordboss.android.event.SecureItemChooseEvent;
import com.passwordboss.android.event.SecureItemChooseResultEvent;
import com.passwordboss.android.helper.SoftKeyboard;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.AvatarResourceMap;
import com.passwordboss.android.utils.Utils;
import com.passwordboss.android.widget.IdentitySecureItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IdentityFragment extends ToolbarFragment implements Changeable, Toolbar.OnMenuItemClickListener {

    public static final String KEY_IDENTITY = "keyIdentity";
    private static final String KEY_ID = "keyId";
    private static final String KEY_HASH_CODE = "keyHashCode";
    @Bind(R.id.fr_id_avatar)
    ImageView mAvatarView;
    @Bind(R.id.fr_id_default)
    CheckBox mDefaultView;
    @Bind(R.id.fr_id_item_address)
    IdentitySecureItemView mItemAddressView;
    @Bind(R.id.fr_id_item_bank_account)
    IdentitySecureItemView mItemBankAccountView;
    @Bind(R.id.fr_id_item_company)
    IdentitySecureItemView mItemCompanyView;
    @Bind(R.id.fr_id_item_credit_card)
    IdentitySecureItemView mItemCreditCardView;
    @Bind(R.id.fr_id_item_email)
    IdentitySecureItemView mItemEmailView;
    @Bind(R.id.fr_id_item_name)
    IdentitySecureItemView mItemNameView;
    @Bind(R.id.fr_id_item_phone)
    IdentitySecureItemView mItemPhoneView;
    @Bind(R.id.fr_id_name)
    @NotEmpty(messageId = R.string.RequiredField, order = 1)
    EditText mNameView;
    @Bind(R.id.fr_id_button_save)
    Button mSaveButton;
    @Bind(R.id.fr_id_star)
    View mStarView;
    private int mAvatarNumber = -1;
    private int mHashCode;
    private UserIdentity mIdentity;
    private AvatarResourceMap mResourceMap = Utils.getAvatarResourceMap(); // FIXME: 6/8/2016 it would be class

    public static IdentityFragment newInstance(String id) {
        IdentityFragment fragment = new IdentityFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    private void bindIcons() {
        mItemNameView.setEmptyIconId(R.drawable.ic_identity_name);
        mItemAddressView.setEmptyIconId(R.drawable.ic_identity_address);
        mItemPhoneView.setEmptyIconId(R.drawable.ic_identity_phone);
        mItemEmailView.setEmptyIconId(R.drawable.ic_identity_email);
        mItemCompanyView.setEmptyIconId(R.drawable.ic_identity_company);
        mItemCreditCardView.setEmptyIconId(R.drawable.ic_identity_credit_card);
        mItemBankAccountView.setEmptyIconId(R.drawable.ic_identity_bank_account);
    }

    private void deleteIdentity() {
        DatabaseHelperSecure
                .getObservable()
                .map(h -> {
                            try {
                                new UserIdentityBll(h).delete(mIdentity);
                                return null;
                            } catch (SQLException e) {
                                throw new IllegalStateException(e);
                            }
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Object>(getContext()) {
                    @Override
                    public void onNext(Object o) {
                        EventBus.getDefault().postSticky(new IdentityRemovedEvent());
                    }
                });
    }

    @Nullable
    private String getIdentityId() {
        Bundle arguments = getArguments();
        return null == arguments ? null : arguments.getString(KEY_ID);
    }

    @Override
    public boolean hasChanges() {
        populateData();
        return mHashCode != mIdentity.hashCode();
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        boolean isNew = Strings.isNullOrEmpty(getIdentityId());
        toolbar.displayCloseNavigation();
        toolbar.setTitle((isNew ? R.string.IdentitiesAddIdentityHeadline : R.string.IdentityEdit));
        toolbar.inflateMenu(R.menu.fragment_identity);
        toolbar.setOnMenuItemClickListener(this);
        setToolbarMenuVisibility(R.id.menu_delete, !isNew);
    }

    private void loadIdentity(String id) {
        DatabaseHelperSecure.getObservable()
                .map(h -> {
                    try {
                        UserIdentity userIdentity = new UserIdentityBll(h).getById(id);
                        if (null == userIdentity) {
                            throw new IllegalArgumentException("Could not find identity by id: " + id);
                        }
                        return userIdentity;
                    } catch (SQLException e) {
                        throw new IllegalStateException("Could not initialize database");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UserIdentity>(getContext()) {
                    @Override
                    public void onNext(UserIdentity userIdentity) {
                        setIdentity(userIdentity);
                    }
                });
    }

    @OnClick(R.id.fr_id_button_save)
    public void onClickButtonSave() {
        if (!FormValidator.validate(this, new SimpleErrorPopupCallback(getContext(), true))) return;
        new ProgressDialog(this).show(getText(R.string.SavingData));
        if (mIdentity.isNew()) {
            mIdentity.setId(UUID.randomUUID().toString());
            mIdentity.setActive(true);
            mIdentity.setCreatedDateNow();
        }
        populateData();
        DatabaseHelperSecure
                .getObservable()
                .map(h -> {
                    try {
                        UserIdentityBll bll = new UserIdentityBll(h);
                        UserIdentity currentDefault = bll.getDefault();
                        if (null == currentDefault) {
                            mIdentity.setDefault(true);
                        }
                        if (mIdentity.isDefault()) {
                            if (null != currentDefault && !mIdentity.getId().equals(currentDefault.getId())) {
                                currentDefault.setDefault(false);
                                bll.insertOrUpdateRow(currentDefault);
                            }
                        }
                        bll.insertOrUpdateRow(mIdentity);
                        return null;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Object>(getContext()) {
                    @Override
                    public void onNext(Object o) {
                        new ProgressDialog(IdentityFragment.this).dismiss();
                        EventBus.getDefault().postSticky(new IdentitySavedEvent());

                    }
                });
    }

    @OnClick({R.id.fr_id_button_choose_picture, R.id.fr_id_avatar})
    public void onClickChoosePicture() {
        EventBus.getDefault().post(new ChooseAvatarEvent());
    }

    @OnClick({
            R.id.fr_id_item_name,
            R.id.fr_id_item_address,
            R.id.fr_id_item_phone,
            R.id.fr_id_item_email,
            R.id.fr_id_item_company,
            R.id.fr_id_item_credit_card,
            R.id.fr_id_item_bank_account
    })
    void onClickItem(IdentitySecureItemView view) {
        EventBus.getDefault().post(new SecureItemChooseEvent(view.getItemType()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_identity, container, false);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(AvatarChosenEvent e) {
        EventBus.getDefault().removeStickyEvent(e);
        mAvatarNumber = e.getNumber();
        int resId = e.getResId();
        mAvatarView.setImageResource(resId);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemChooseResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        SecureItem secureItem = event.getSecureItem();
        if (null == secureItem) return;
        ItemType itemType = ItemType.from(secureItem);
        if (null == itemType) return;
        switch (itemType) {
            case Name:
                mItemNameView.setData(itemType, secureItem);
                break;
            case Address:
                mItemAddressView.setData(itemType, secureItem);
                break;
            case Phone:
                mItemPhoneView.setData(itemType, secureItem);
                break;
            case Company:
                mItemCompanyView.setData(itemType, secureItem);
                break;
            case Email:
                mItemEmailView.setData(itemType, secureItem);
                break;
            case CreditCard:
                mItemCreditCardView.setData(itemType, secureItem);
                break;
            case BankAccount:
                mItemBankAccountView.setData(itemType, secureItem);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete: {
                new DeleteItemDialog(getContext()).show(this::deleteIdentity);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        populateData();
        outState.putSerializable(KEY_IDENTITY, mIdentity);
        outState.putInt(KEY_HASH_CODE, mHashCode);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mDefaultView.setOnCheckedChangeListener((buttonView, isChecked) -> mStarView.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE));
        if (null == savedInstanceState) {
            bindIcons();
            String id = getIdentityId();
            if (Strings.isNullOrEmpty(id)) {
                setIdentity(new UserIdentity());
                new SoftKeyboard().showImplicit(mNameView);
            } else {
                loadIdentity(id);
            }
        } else {
            setIdentity((UserIdentity) savedInstanceState.getSerializable(KEY_IDENTITY));
            mHashCode = savedInstanceState.getInt(KEY_HASH_CODE);
        }
    }

    private void populateData() {
        if (null == mIdentity) return;
        if (mAvatarNumber != -1) {
            mIdentity.setAvatar(String.format(Locale.getDefault(), "%02d", mAvatarNumber));
        }
        mIdentity.setIdentityName(mNameView.getText().toString().trim());
        mIdentity.setDefault(mDefaultView.isChecked());
        mIdentity.setName(mItemNameView.getSecureItem());
        mIdentity.setAddress(mItemAddressView.getSecureItem());
        mIdentity.setPhoneNumber(mItemPhoneView.getSecureItem());
        mIdentity.setEmail(mItemEmailView.getSecureItem());
        mIdentity.setCompany(mItemCompanyView.getSecureItem());
        mIdentity.setCreditCard(mItemCreditCardView.getSecureItem());
        mIdentity.setBankAccount(mItemBankAccountView.getSecureItem());
    }

    private void populateViews() {
        if (null == mIdentity) return;
        Glide.with(getActivity())
                .load(mResourceMap.get(mIdentity.getAvatar()))
                .into(mAvatarView);
        mNameView.setText(mIdentity.getIdentityName());
        mItemNameView.setData(ItemType.Name, mIdentity.getName());
        mItemAddressView.setData(ItemType.Address, mIdentity.getAddress());
        mItemPhoneView.setData(ItemType.Phone, mIdentity.getPhoneNumber());
        mItemEmailView.setData(ItemType.Email, mIdentity.getEmail());
        mItemCompanyView.setData(ItemType.Company, mIdentity.getCompany());
        mItemCreditCardView.setData(ItemType.CreditCard, mIdentity.getCreditCard());
        mItemBankAccountView.setData(ItemType.BankAccount, mIdentity.getBankAccount());
        mDefaultView.setChecked(mIdentity.isDefault());
    }

    private void setIdentity(UserIdentity identity) {
        mIdentity = identity;
        mHashCode = mIdentity.hashCode();
        populateViews();
    }
}
