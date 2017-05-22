package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.Folder;

public class FolderClickEvent {
    private final Folder mFolder;

    public FolderClickEvent(Folder folder) {
        mFolder = folder;
    }

    public Folder getFolder() {
        return mFolder;
    }
}
