package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.event.SecureItemChangedEvent;
import com.passwordboss.android.event.SecureItemDeleteEvent;
import com.passwordboss.android.event.SecureItemEditEvent;
import com.passwordboss.android.event.SecureItemMenuCloseEvent;
import com.passwordboss.android.fragment.SecureItemMenuFragment;

import org.greenrobot.eventbus.Subscribe;

public class SecureItemMenuActivity extends WrappedFragmentActivity {

    private static final String KEY_SECURE_ITEM = "keySecureItem";

    public static void start(@NonNull Context context, @NonNull SecureItem secureItem) {
        Intent intent = new Intent(context, SecureItemMenuActivity.class);
        intent.putExtra(KEY_SECURE_ITEM, secureItem);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    protected Fragment createFragment() {
        SecureItem secureItem = getSecureItem();
        return SecureItemMenuFragment.newInstance(secureItem);
    }

    private SecureItem getSecureItem() {
        return (SecureItem) getIntent().getSerializableExtra(KEY_SECURE_ITEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayHomeAsUp();
        setTitle("");
    }

    @Subscribe(priority = 100)
    public void onEvent(SecureItemEditEvent event) {
        finish();
    }

    @Subscribe
    public void onEvent(SecureItemMenuCloseEvent event) {
        finish();
    }

    @Subscribe(priority = 100)
    public void onEvent(SecureItemDeleteEvent event) {
        finish();
    }

    @Subscribe(priority = 100)
    public void onEvent(SecureItemChangedEvent event) {
        finish();
    }

}
