package com.passwordboss.android.database.bll;
import java.sql.SQLException;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.EventDefinition;

public class EventDefinitionBll extends BaseBll<EventDefinition, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public EventDefinitionBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getEventDefinitionDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
}
