package com.passwordboss.android.database.bll;
import java.sql.SQLException;

import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.AlertMessages;

public class AlertMessageBll extends BaseBll<AlertMessages, String> {

	public AlertMessageBll(DatabaseHelperSecure helperSecure) throws SQLException {
		super(helperSecure.getAlertMessagesDao());
	}
	
}
