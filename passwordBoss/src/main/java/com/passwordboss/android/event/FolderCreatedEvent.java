package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.Folder;

public class FolderCreatedEvent extends FolderSavedEvent {
    public FolderCreatedEvent(Folder folder) {
        super(folder);
    }
}
