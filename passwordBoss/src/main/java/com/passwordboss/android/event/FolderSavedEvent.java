package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.Folder;

public class FolderSavedEvent extends FolderChangedEvent {
    private final Folder mFolder;

    public FolderSavedEvent(Folder folder) {
        mFolder = folder;
    }

    public Folder getFolder() {
        return mFolder;
    }
}
