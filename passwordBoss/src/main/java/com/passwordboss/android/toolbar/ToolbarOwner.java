package com.passwordboss.android.toolbar;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

public interface ToolbarOwner {
    @Nullable
    AppToolbar getToolbar(@IdRes int id);
}
