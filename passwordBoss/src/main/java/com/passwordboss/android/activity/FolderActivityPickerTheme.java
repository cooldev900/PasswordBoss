package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.passwordboss.android.R;

public class FolderActivityPickerTheme extends FolderActivity {
    public static void start(Context context) {
        context.startActivity(new Intent(context, FolderActivityPickerTheme.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setVisibility(View.GONE);
        setTitle(getIntent().hasExtra(KEY_FOLDER) ? R.string.Edit : R.string.AddNewFolder);
    }
}
