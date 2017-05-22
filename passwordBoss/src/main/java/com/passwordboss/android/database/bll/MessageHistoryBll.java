package com.passwordboss.android.database.bll;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.MessageHistory;
import com.passwordboss.android.database.beans.UserSubscription;
import com.passwordboss.android.utils.Pref;

import android.os.Message;

public class MessageHistoryBll extends BaseBll<MessageHistory, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public MessageHistoryBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getMessageHistoryDao());
		this.mDatabseHelper = mDatabseHelper;
	}

	public MessageHistory getLastFormDB() {
		try {
			List<MessageHistory> messageHistories = mDao.query(mDao.queryBuilder()
		         .orderBy("id", false).limit(1L).prepare());
			if (messageHistories != null && messageHistories.size() > 0) {
				return messageHistories.get(0);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public MessageHistory getByID(String id) {
		try {
			 QueryBuilder<MessageHistory, Integer> queryBuilder = mDao.queryBuilder();
			 Where<MessageHistory, Integer> where = queryBuilder.where();
			 PreparedQuery<MessageHistory> preparedQuery = queryBuilder.prepare();
			 where.eq(DatabaseConstants.ID, id);
			 List<MessageHistory> mMessageHistoryList = mDao.query(preparedQuery);
			 if (mMessageHistoryList.size() > 0) {
	            return mMessageHistoryList.get(0);
			 }
		} catch (Exception e) {
		}
		return null;
	}
	
	public void insert(String analyticsCode, String daysSinceAccountCreated, String msgType, String theme, String buttonClicked, String buyButton) {
		try {
			MessageHistory mMessageHistory = new MessageHistory();
			mMessageHistory.setId(UUID.randomUUID().toString());
			mMessageHistory.setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
			mMessageHistory.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
			mMessageHistory.setAnaliticsCode(analyticsCode);
			mMessageHistory.setMsgType(msgType);
			mMessageHistory.setTheme(theme);
			mMessageHistory.setButtonClicked(buttonClicked);
			mMessageHistory.setBuyButton(buyButton);
			mMessageHistory.setDaysSinceAccountCreated(daysSinceAccountCreated);
			insertRow(mMessageHistory);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}