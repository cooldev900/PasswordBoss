package com.passwordboss.android.database.bll;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.MessageDefinitions;

import java.sql.SQLException;


public class MessageDefinitionsBll extends BaseBll<MessageDefinitions, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public MessageDefinitionsBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getMessageDefinitionsDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
}
