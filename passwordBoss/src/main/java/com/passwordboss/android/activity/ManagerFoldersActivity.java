package com.passwordboss.android.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.FolderChangedEvent;
import com.passwordboss.android.event.FolderClickEvent;
import com.passwordboss.android.event.FoldersRefreshEvent;
import com.passwordboss.android.fragment.FolderListFragment;
import com.passwordboss.android.fragment.FolderListFragment.Params;
import com.passwordboss.android.model.ItemType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ManagerFoldersActivity extends ToolbarWrappedFragmentActivity {

    @Nullable
    @Override
    protected Fragment createFragment() {
        Params params = new Params.Builder()
                .expandChildren(true)
                .create();
        return FolderListFragment.newInstance(params);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(FolderClickEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        FolderActivity.start(this, event.getFolder());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddItemEvent event) {
        if (event.getItemType() == ItemType.Folder) {
            FolderActivity.start(this);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(FolderChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        EventBus.getDefault().post(new FoldersRefreshEvent());
    }
}
