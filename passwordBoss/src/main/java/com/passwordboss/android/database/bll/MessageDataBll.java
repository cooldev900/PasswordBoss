package com.passwordboss.android.database.bll;
import java.sql.SQLException;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.MessageData;

public class MessageDataBll extends BaseBll<MessageData, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public MessageDataBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getMessageDataDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
}
