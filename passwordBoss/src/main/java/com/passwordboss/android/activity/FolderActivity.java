package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.event.FolderChooseEvent;
import com.passwordboss.android.event.FolderDeletedEvent;
import com.passwordboss.android.event.FolderSavedEvent;
import com.passwordboss.android.fragment.FolderFragment;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

public class FolderActivity extends ChangeableActivity implements ToolbarOwner{

    protected static final String KEY_FOLDER = "keyFolder";
    @Bind(R.id.in_tb_toolbar)
    AppToolbar mToolbar;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, @Nullable Folder folder) {
        Intent intent = new Intent(context, FolderActivity.class);
        if (null != folder) intent.putExtra(KEY_FOLDER, folder);
        context.startActivity(intent);
    }


    @Override
    protected Fragment createFragment() {
        return FolderFragment.newInstance((Folder) getIntent().getSerializableExtra(KEY_FOLDER));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_toolbar;
    }

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int id) {
        return mToolbar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderSavedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderDeletedEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderChooseEvent event) {
        ChooseFolderActivity.start(this, event.ToExclude, event.CreationAllowed);
    }

}
