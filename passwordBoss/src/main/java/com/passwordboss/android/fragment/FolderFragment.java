package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.database.bll.FolderBll;
import com.passwordboss.android.dialog.alert.ErrorDialog;
import com.passwordboss.android.event.FolderChooseEvent;
import com.passwordboss.android.event.FolderChooseResultEvent;
import com.passwordboss.android.event.FolderCreatedEvent;
import com.passwordboss.android.event.FolderDeletedEvent;
import com.passwordboss.android.event.FolderSavedEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.widget.AppInputField;
import com.passwordboss.android.widget.AppItemIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class FolderFragment extends ToolbarFragment implements Changeable, Toolbar.OnMenuItemClickListener {
    private static final String KEY_FOLDER = "keyFolder";
    private static final String KEY_HASH_CODE = "keyHashCode";
    @Bind(R.id.fr_fl_icon)
    AppItemIconView mIconView;
    @NotEmpty(messageId = R.string.RequiredField)
    @Bind(R.id.fr_fl_name)
    EditText mNameView;
    @Bind(R.id.fr_fl_parent)
    AppInputField mParentView;
    private Folder mFolder;
    private int mHashCode;

    public static FolderFragment newInstance(@Nullable Folder folder) {
        FolderFragment fragment = new FolderFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_FOLDER, folder);
        fragment.setArguments(arguments);
        return fragment;
    }

    private void confirmDelete() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.FolderDeleteFolderHeadline)
                .setMessage(R.string.FolderDeleteFolderBody)
                .setPositiveButton(R.string.Delete, (dialog, which) -> {
                    deleteFolder();
                })
                .setNegativeButton(R.string.Cancel, null)
                .show();
    }

    private void deleteFolder() {
        FolderBll folderBll = getFolderBll();
        if (null == folderBll) return;
        mFolder.setActive(false);
        mFolder.setLastModifiedDateNow();
        folderBll.insertOrUpdateRow(mFolder);
        EventBus.getDefault().postSticky(new FolderDeletedEvent());
    }

    @Nullable
    private FolderBll getFolderBll() {
        try {
            return new FolderBll(DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY));
        } catch (SQLException e) {
            new ErrorDialog(getContext()).show(e);
        }
        return null;
    }

    @Override
    public boolean hasChanges() {
        populateData();
        return mHashCode != mFolder.hashCode();
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        boolean existing = null != getArguments() && null != getArguments().get(KEY_FOLDER);
        toolbar.setTitle(existing ? R.string.Edit : R.string.AddNewFolder);
        toolbar.displayCloseNavigation();
        if (existing) {
            toolbar.inflateMenu(R.menu.fragment_folder);
            toolbar.setOnMenuItemClickListener(this);
        }
    }

    @OnClick(R.id.fr_fl_button_save)
    void onClickButtonSave() {
        if (!FormValidator.validate(this, new SimpleErrorPopupCallback(getContext(), true))) return;
        FolderBll folderBll = getFolderBll();
        FolderSavedEvent event;
        if (folderBll == null) return;
        if (mFolder.isNew()) {
            event = new FolderCreatedEvent(mFolder);
            mFolder.setId(UUID.randomUUID().toString());
            mFolder.setActive(true);
            mFolder.setCreatedDateNow();
        } else {
            event = new FolderSavedEvent(mFolder);
        }
        mFolder.setName(mNameView.getText().toString());
        mFolder.setLastModifiedDateNow();
        folderBll.insertOrUpdateRow(mFolder);
        EventBus.getDefault().postSticky(event);
    }

    @OnClick(R.id.fr_fl_parent)
    void onClickParent() {
        EventBus.getDefault().post(new FolderChooseEvent(mFolder, false));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_folder, null);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(FolderChooseResultEvent event) {
        // ignore chose event if it was forwarded by create event of this folder
        if (event.getFolder().equals(mFolder)) return;
        EventBus.getDefault().removeStickyEvent(event);
        mFolder.setParent(event.getFolder());
        populateViews();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                confirmDelete();
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFolder.setName(mNameView.getText().toString());
        outState.putSerializable(KEY_FOLDER, mFolder);
        outState.putInt(KEY_HASH_CODE, mHashCode);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mIconView.setItemType(ItemType.Folder);
        Bundle arguments = getArguments();
        if (null != savedInstanceState) {
            mFolder = (Folder) savedInstanceState.getSerializable(KEY_FOLDER); // restore
            mHashCode = savedInstanceState.getInt(KEY_HASH_CODE);
        } else if (null != arguments) {
            mFolder = (Folder) arguments.getSerializable(KEY_FOLDER); // edit
            if (null != mFolder) mHashCode = mFolder.hashCode();
        }
        if (null == mFolder) {
            mFolder = new Folder(); // add
            mHashCode = mFolder.hashCode();
        }
        populateViews();
    }

    private void populateData() {
        if (null == mFolder) return;
        mFolder.setName(mNameView.getText().toString());
    }

    private void populateViews() {
        if (null == mFolder) return;
        mNameView.setText(mFolder.getName());
        Folder parent = mFolder.getParent();
        if (null == parent) return;
        mParentView.setText(parent.getName());
    }
}
