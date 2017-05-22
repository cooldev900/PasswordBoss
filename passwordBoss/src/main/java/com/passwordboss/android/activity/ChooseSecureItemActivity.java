package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.passwordboss.android.R;
import com.passwordboss.android.event.ItemTypeAddEvent;
import com.passwordboss.android.event.SecureItemChooseResultEvent;
import com.passwordboss.android.event.SecureItemClickEvent;
import com.passwordboss.android.event.SecureItemCreatedEvent;
import com.passwordboss.android.fragment.SecureItemsTabsFragment;
import com.passwordboss.android.model.ItemType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChooseSecureItemActivity extends WrappedFragmentActivity {

    public static final String KEY_ITEM_TYPE = "keyItemType";

    public static void start(Context context, ItemType itemType) {
        Intent intent = new Intent(context, ChooseSecureItemActivity.class);
        intent.putExtra(KEY_ITEM_TYPE, itemType);
        context.startActivity(intent);
    }

    @Override
    protected Fragment createFragment() {
        ItemType itemType = (ItemType) getIntent().getSerializableExtra(KEY_ITEM_TYPE);
        return SecureItemsTabsFragment.newInstance(itemType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.FillDataOverlayHeaderTextFallback);
        displayHomeAsUp();
    }

    @Subscribe(priority = 100)
    public void onEvent(SecureItemClickEvent event) {
        EventBus.getDefault().cancelEventDelivery(event); // to prevent to consume it at MainActivity at tablet mode
        EventBus.getDefault().postSticky(new SecureItemChooseResultEvent(event.getSecureItem()));
        finish();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemCreatedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        // forward created item to requester
        EventBus.getDefault().postSticky(new SecureItemChooseResultEvent(event.getSecureItem()));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemTypeAddEvent event) {
        SecureItemActivity.start(this, event.getItemType());
    }


}
