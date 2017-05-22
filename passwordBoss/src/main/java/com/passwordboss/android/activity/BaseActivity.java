package com.passwordboss.android.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.passwordboss.android.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void displayHomeAsUp() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected boolean isPhone() {
        return !isTablet();
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.tablet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupScreenOrientationByDeviceType();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return android.R.id.home == item.getItemId() && onUpPressed() || super.onOptionsItemSelected(item);
    }

    protected boolean onUpPressed() {
        finish();
        return true;
    }

    private void setupScreenOrientationByDeviceType() {
        if (!isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


}
