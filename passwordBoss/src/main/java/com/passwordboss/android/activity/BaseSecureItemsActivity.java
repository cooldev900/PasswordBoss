package com.passwordboss.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.passwordboss.android.R;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.dialog.alert.DeleteItemDialog;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.ItemTypeAddEvent;
import com.passwordboss.android.event.SecureItemChangedEvent;
import com.passwordboss.android.event.SecureItemClickEvent;
import com.passwordboss.android.event.SecureItemDeleteEvent;
import com.passwordboss.android.event.SecureItemEditEvent;
import com.passwordboss.android.event.SecureItemsRefreshEvent;
import com.passwordboss.android.fragment.BaseFragment;
import com.passwordboss.android.helper.ToDo;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

abstract class BaseSecureItemsActivity extends AutoLockActivity implements ToolbarOwner {

    private AppToolbar mToolbar;

    @NonNull
    protected abstract BaseFragment createFragment();

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int id) {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_toolbar);
        mToolbar = (AppToolbar) findViewById(R.id.in_tb_toolbar);
        displayHomeAsUp();
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ac_fr_fragment, createFragment())
                    .commit();
        }
    }

    @Subscribe
    public void onEvent(ItemTypeAddEvent event) {
        ItemTypesActivity.start(this, event.getItemType());
    }

    @Subscribe
    public void onEvent(SecureItemClickEvent event) {
        SecureItemMenuActivity.start(this, event.getSecureItem());
    }

    @Subscribe(sticky = true)
    public void onEvent(SecureItemEditEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        SecureItemActivity.start(this, event.getSecureItem());
    }

    @Subscribe(sticky = true)
    public void onEvent(SecureItemDeleteEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        new DeleteItemDialog(this).show(() -> SecureItemBll
                .getObservable()
                .map(secureItemBll -> secureItemBll.deleteItemByID(event.getSecureItem().getId()))
                .compose(new ApplySchedulers<>())
                .subscribe(result -> {
                    EventBus.getDefault().post(new SecureItemsRefreshEvent());
                }));
    }

    @Subscribe(sticky = true)
    public void onEvent(SecureItemChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        EventBus.getDefault().post(new SecureItemsRefreshEvent());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(AddItemEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        switch (event.getItemType()) {
            case Identity:
                startActivity(new Intent(this, IdentityActivity.class));
                break;
            case SharedItem:
            case EmergencyContact:
                new ToDo().show(this);
                break;
            case Folder:
                FolderActivity.start(this);
                break;
            default:
                finish();
                SecureItemActivity.start(this, event.getItemType());
                break;
        }
    }

}
