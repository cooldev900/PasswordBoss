package com.passwordboss.android.event;


import com.passwordboss.android.database.beans.Folder;

public class FolderChooseResultEvent extends BaseEvent {
    private final Folder mFolder;

    public Folder getFolder() {
        return mFolder;
    }

    public FolderChooseResultEvent(Folder folder) {
        mFolder = folder;
    }
}
