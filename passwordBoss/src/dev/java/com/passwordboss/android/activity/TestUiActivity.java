package com.passwordboss.android.activity;

import android.os.Bundle;

import com.passwordboss.android.R;
import com.passwordboss.android.fragment.SecureItemMenuFragment;

public class TestUiActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ui);
        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ac_ttui_fragment, new SecureItemMenuFragment())
                    .commitAllowingStateLoss();
        }
    }

}
