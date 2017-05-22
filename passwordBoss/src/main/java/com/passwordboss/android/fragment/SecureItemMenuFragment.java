package com.passwordboss.android.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.database.bll.SecureItemDataBll;
import com.passwordboss.android.event.SecureItemChangedEvent;
import com.passwordboss.android.event.SecureItemDeleteEvent;
import com.passwordboss.android.event.SecureItemEditEvent;
import com.passwordboss.android.event.SecureItemMenuCloseEvent;
import com.passwordboss.android.helper.SecureItemSubTitle;
import com.passwordboss.android.helper.ToDo;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.widget.AppItemIconView;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class SecureItemMenuFragment extends BaseFragment {

    private static final String KEY_ID = "keyId";
    @Bind(R.id.fr_simn_icon)
    AppItemIconView mIconView;
    @Bind(R.id.fr_simn_title)
    TextView mTitleView;
    @Bind(R.id.fr_simn_sub_title)
    TextView mSubTitleView;
    @Bind(R.id.fr_simn_open_website)
    View mOpenWebsiteView;
    @Bind(R.id.fr_simn_copy_password)
    View mCopyPasswordView;
    @Bind(R.id.fr_simn_copy_username)
    View mCopyUsernameView;
    @Bind(R.id.fr_simn_favorite)
    TextView mFavoriteView;
    private SecureItem mSecureItem;

    public static SecureItemMenuFragment newInstance(@NonNull SecureItem secureItem) {
        SecureItemMenuFragment fragment = new SecureItemMenuFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, secureItem.getId());
        fragment.setArguments(args);
        return fragment;
    }

    private void copySecureItemDataValueToClipboard(String identifier, String label, int toastMessageResId) {
        DatabaseHelperSecure
                .getObservable()
                .flatMap(helperSecure -> {
                    try {
                        SecureItemData usernameData = new SecureItemDataBll(helperSecure)
                                .findSecureItemData(mSecureItem.getId(), identifier);
                        return Observable.just(usernameData);
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                })
                .compose(new ApplySchedulers<>())
                .subscribe(new BaseObserver<SecureItemData>(getContext()) {
                    @Override
                    public void onNext(SecureItemData secureItemData) {
                        String value = null == secureItemData ? "" : secureItemData.getValue();
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setPrimaryClip(ClipData.newPlainText(label, value));
                        Toast.makeText(getContext(), toastMessageResId, Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(new SecureItemMenuCloseEvent());
                    }
                });
    }

    @Nullable
    private String getSecureItemId() {
        Bundle arguments = getArguments();
        if (null == arguments) return null;
        return arguments.getString(KEY_ID);
    }

    @OnClick(R.id.fr_simn_copy_password)
    void onClickCopyPassword() {
        ItemType itemType = ItemType.from(mSecureItem);
        String identifier = itemType == ItemType.SshKey ? SecureItemData.Identifier.PASSPHRASE : SecureItemData.Identifier.PASSWORD;
        copySecureItemDataValueToClipboard(identifier, "password", R.string.YourPasswordCopiedToClipboard);
    }

    @OnClick(R.id.fr_simn_copy_username)
    void onClickCopyUsername() {
        String identifier = SecureItemData.Identifier.USERNAME;
        copySecureItemDataValueToClipboard(identifier, "username", R.string.YourUsernameCopiedToClipboard);
    }

    @OnClick(R.id.fr_simn_delete)
    void onClickDelete() {
        EventBus.getDefault().postSticky(new SecureItemDeleteEvent(mSecureItem));
    }

    @OnClick(R.id.fr_simn_edit_item)
    void onClickEditItem() {
        EventBus.getDefault().postSticky(new SecureItemEditEvent(mSecureItem));
    }

    @OnClick(R.id.fr_simn_favorite)
    void onClickFavorite() {
        DatabaseHelperSecure.getObservable()
                .flatMap(helperSecure -> {
                    try {
                        mSecureItem.setFavorite(!mSecureItem.isFavorite());
                        return Observable.just(new SecureItemBll(helperSecure).updateRow(mSecureItem));
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                }).compose(new ApplySchedulers<>())
                .subscribe(new BaseObserver<Boolean>(getContext()) {
                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            int textId = mSecureItem.isFavorite() ? R.string.AddedToFavorites : R.string.RemovedFromFavorites;
                            Toast.makeText(getContext(), textId, Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().postSticky(new SecureItemChangedEvent(mSecureItem));
                        }
                    }
                });
    }

    @OnClick(R.id.fr_simn_open_website)
    void onClickOpenWebsite() {
        new ToDo().show(getContext());
    }

    @OnClick(R.id.fr_simn_share_item)
    void onClickShareItem() {
        new ToDo().show(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secure_item_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        SecureItemBll
                .getObservable()
                .flatMap(secureItemBll -> {
                    try {
                        return Observable.just(secureItemBll.findSecureItemById(getSecureItemId()));
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                })
                .compose(new ApplySchedulers<>())
                .subscribe(this::setSecureItem);
    }

    private void setSecureItem(SecureItem secureItem) {
        mSecureItem = secureItem;
        if (null == mSecureItem) return;
        ItemType itemType = ItemType.from(mSecureItem);
        updateMenuVisibility(itemType);
        mIconView.setItemType(itemType, mSecureItem.getColor());
        mTitleView.setText(mSecureItem.getName());
        String subTitle = new SecureItemSubTitle(mSecureItem).get();
        if (Strings.isNullOrEmpty(subTitle)) {
            mSubTitleView.setVisibility(View.GONE);
        } else {
            mSubTitleView.setVisibility(View.VISIBLE);
            mSubTitleView.setText(subTitle);
        }
        int id = mSecureItem.isFavorite() ? R.drawable.ic_menu_favorite_checked : R.drawable.ic_menu_favorite_unchecked;
        mFavoriteView.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0);
    }

    private void updateMenuVisibility(ItemType itemType) {
        int openSiteVisibility = View.GONE;
        int copyPasswordVisibility = View.GONE;
        int copyUsernameVisibility = View.GONE;
        if (itemType == ItemType.Website) {
            openSiteVisibility = View.VISIBLE;
        }
        if (ItemType.Password.contains(itemType)) {
            copyPasswordVisibility = View.VISIBLE;
            copyUsernameVisibility = View.VISIBLE;
        }
        if (itemType == ItemType.WiFi || itemType == ItemType.SshKey) {
            copyUsernameVisibility = View.GONE;
        }
        mOpenWebsiteView.setVisibility(openSiteVisibility);
        mCopyPasswordView.setVisibility(copyPasswordVisibility);
        mCopyUsernameView.setVisibility(copyUsernameVisibility);
    }
}
