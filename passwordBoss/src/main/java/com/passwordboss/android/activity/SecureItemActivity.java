package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.event.ChooseIconColorStartEvent;
import com.passwordboss.android.event.FolderChooseEvent;
import com.passwordboss.android.event.SecureItemSavedEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.fragment.SecureItemFragment;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

public class SecureItemActivity extends ChangeableActivity implements ToolbarOwner {

    private static final String KEY_ITEM_TYPE = "keyItemType";
    private static final String KEY_ITEM = "keyItem";
    private static final String KEY_ID = "keyId";
    @Bind(R.id.in_tb_toolbar)
    AppToolbar mToolbar;

    public static void start(Context context, @NonNull SecureItem secureItem) {
        Intent intent = new Intent(context, SecureItemActivity.class);
        intent.putExtra(KEY_ITEM_TYPE, ItemType.from(secureItem));
        if (secureItem.isNew()) {
            intent.putExtra(KEY_ITEM, secureItem);
        } else {
            intent.putExtra(KEY_ID, secureItem.getId());
        }
        context.startActivity(intent);
    }

    public static void start(@NonNull Context context, @NonNull ItemType itemType) {
        Intent intent = new Intent(context, SecureItemActivity.class);
        intent.putExtra(KEY_ITEM_TYPE, itemType);
        context.startActivity(intent);
    }

    @Override
    @Nullable
    protected Fragment createFragment() {
        ItemType itemType = getItemType();
        if (null == itemType) return null;
        SecureItem secureItem = getSecureItem();
        return null == secureItem ? SecureItemFragment.newInstance(itemType, getId()) : SecureItemFragment.newInstance(secureItem);
    }

    private String getId() {
        return getIntent().getStringExtra(KEY_ID);
    }

    private ItemType getItemType() {
        return (ItemType) getIntent().getSerializableExtra(KEY_ITEM_TYPE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_toolbar;
    }

    private SecureItem getSecureItem() {
        return (SecureItem) getIntent().getSerializableExtra(KEY_ITEM);
    }

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int id) {
        return mToolbar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseIconColorStartEvent event) {
        startActivity(new Intent(this, ChooseIconColorActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderChooseEvent event) {
        startActivity(new Intent(this, ChooseFolderActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(SecureItemSavedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SelectSettingEvent event) {
        Intent intent = new Intent(this, SettingItemListActivity.class);
        intent.putExtra(SettingItemListActivity.EXTRA_TYPE, event.getType());
        startActivity(intent);
    }
}
