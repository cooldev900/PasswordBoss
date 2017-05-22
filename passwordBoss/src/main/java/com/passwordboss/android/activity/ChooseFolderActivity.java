package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.FolderChangedEvent;
import com.passwordboss.android.event.FolderChooseResultEvent;
import com.passwordboss.android.event.FolderClickEvent;
import com.passwordboss.android.event.FolderCreatedEvent;
import com.passwordboss.android.fragment.FolderListFragment;
import com.passwordboss.android.model.ItemType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChooseFolderActivity extends AutoLockActivity {

    public static final String KEY_TO_EXCLUDE = "keyToExclude";
    public static final String KEY_CREATION_ALLOWED = "keyCreationAllowed";

    public static void start(Context context, Folder toExclude, boolean creationAllowed) {
        Intent intent = new Intent(context, ChooseFolderActivity.class);
        intent.putExtra(KEY_TO_EXCLUDE, toExclude);
        intent.putExtra(KEY_CREATION_ALLOWED, creationAllowed);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_folder);
        setTitle(R.string.ChooseFolder);
        displayHomeAsUp();
        if (null == savedInstanceState) {
            FolderListFragment.Params params = new FolderListFragment.Params.Builder()
                    .toExclude((Folder) getIntent().getSerializableExtra(KEY_TO_EXCLUDE))
                    .allowCreate(getIntent().getBooleanExtra(KEY_CREATION_ALLOWED, true))
                    .create();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ac_cf_fragment, FolderListFragment.newInstance(params))
                    .commit();
        }
    }

    @Subscribe(priority = 100)
    public void onEvent(FolderClickEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        EventBus.getDefault().postSticky(new FolderChooseResultEvent(event.getFolder()));
        finish();
    }

    @Subscribe(priority = 100)
    public void onEvent(AddItemEvent event) {
        if (event.getItemType() == ItemType.Folder) {
            EventBus.getDefault().cancelEventDelivery(event);
            FolderActivityPickerTheme.start(this);
        }
    }

    @Subscribe(sticky = true, priority = 100)
    public void onEvent(FolderChangedEvent event) {
        // we should use subscribe for same class, to be able cancel event for other subscribers
        // need to convert creating into choose event, without this app would close third panel at
        // tablet mode
        if (event instanceof FolderCreatedEvent) {
            Folder folder = ((FolderCreatedEvent) event).getFolder();
            EventBus.getDefault().cancelEventDelivery(event);
            EventBus.getDefault().removeStickyEvent(event);
            EventBus.getDefault().postSticky(new FolderChooseResultEvent(folder));
            finish();
        }
    }

}
