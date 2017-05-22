package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.passwordboss.android.R;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.fragment.ItemTypesFragment;
import com.passwordboss.android.model.ItemType;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ItemTypesActivity extends AutoLockActivity {

    public static final String KEY_ITEM_TYPE = "keyItemType";

    public static void start(Context context, ItemType itemType) {
        Intent intent = new Intent(context, ItemTypesActivity.class);
        intent.putExtra(KEY_ITEM_TYPE, itemType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_types);
        setTitle(R.string.AddNewItemCapitalCase);
        displayHomeAsUp();
        if (null == savedInstanceState) {
            ItemType itemType = (ItemType) getIntent().getSerializableExtra(KEY_ITEM_TYPE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ac_it_fragment, ItemTypesFragment.newInstance(itemType))
                    .commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddItemEvent event) {
        finish();
    }
}
