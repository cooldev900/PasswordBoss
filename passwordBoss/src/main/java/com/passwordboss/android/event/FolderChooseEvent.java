package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.Folder;

public class FolderChooseEvent extends BaseEvent {
    public final Folder ToExclude;
    public final boolean CreationAllowed;

    public FolderChooseEvent(Folder toExclude, boolean creationAllowed) {
        ToExclude = toExclude;
        CreationAllowed = creationAllowed;
    }


}
