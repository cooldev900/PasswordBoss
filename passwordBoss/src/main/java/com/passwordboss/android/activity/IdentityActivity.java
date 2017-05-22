package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.UserIdentity;
import com.passwordboss.android.event.ChooseAvatarEvent;
import com.passwordboss.android.event.IdentityRemovedEvent;
import com.passwordboss.android.event.IdentitySavedEvent;
import com.passwordboss.android.event.SecureItemChooseEvent;
import com.passwordboss.android.fragment.IdentityFragment;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

public class IdentityActivity extends ChangeableActivity implements ToolbarOwner {

    private final static String KEY_IDENTITY_ID = "keyIdentityId";
    @Bind(R.id.in_tb_toolbar)
    AppToolbar mToolbar;

    public static void start(Context context, @NonNull UserIdentity identity) {
        Intent intent = new Intent(context, IdentityActivity.class);
        intent.putExtra(IdentityActivity.KEY_IDENTITY_ID, identity.getId());
        context.startActivity(intent);
    }

    @Override
    protected Fragment createFragment() {
        return IdentityFragment.newInstance(getIdentityId());
    }

    private String getIdentityId() {
        return getIntent().getStringExtra(KEY_IDENTITY_ID);
    }

    protected int getLayoutId() {
        return R.layout.activity_fragment_toolbar;
    }

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int id) {
        return mToolbar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseAvatarEvent event) {
        startActivity(new Intent(this, ChooseAvatarActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(IdentitySavedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEvent(IdentityRemovedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemChooseEvent event) {
        ChooseSecureItemActivity.start(this, event.getItemType());
    }
}
