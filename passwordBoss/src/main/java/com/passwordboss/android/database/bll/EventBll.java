package com.passwordboss.android.database.bll;
import java.sql.SQLException;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Event;

public class EventBll extends BaseBll<Event, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public EventBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getEventDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
}
