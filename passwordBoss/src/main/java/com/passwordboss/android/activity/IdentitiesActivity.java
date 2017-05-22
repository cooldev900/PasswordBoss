package com.passwordboss.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.passwordboss.android.R;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.IdentitiesRefreshEvent;
import com.passwordboss.android.event.IdentityChangedEvent;
import com.passwordboss.android.event.IdentityViewEvent;
import com.passwordboss.android.fragment.IdentitiesFragment;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IdentitiesActivity extends AutoLockActivity implements ToolbarOwner {

    @Bind(R.id.in_tb_toolbar)
    AppToolbar mToolbar;

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int id) {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_toolbar);
        ButterKnife.bind(this);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ac_fr_fragment, new IdentitiesFragment())
                    .commit();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddItemEvent event) {
        if (event.getItemType() == ItemType.Identity) {
            startActivity(new Intent(this, IdentityActivity.class));
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(IdentityChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        EventBus.getDefault().post(new IdentitiesRefreshEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IdentityViewEvent event) {
        IdentityActivity.start(this, event.getIdentity());
    }


}
