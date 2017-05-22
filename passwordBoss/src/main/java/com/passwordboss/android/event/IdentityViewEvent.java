package com.passwordboss.android.event;

import com.passwordboss.android.database.beans.UserIdentity;

public class IdentityViewEvent extends BaseEvent {
    final private UserIdentity mIdentity;

    public IdentityViewEvent(UserIdentity identity) {
        mIdentity = identity;
    }

    public UserIdentity getIdentity() {
        return mIdentity;
    }
}
