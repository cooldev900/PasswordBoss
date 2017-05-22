package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.UserSubscription;
import com.passwordboss.android.utils.Pref;

import java.sql.SQLException;
import java.util.List;

public class UserSubscriptionBll extends BaseBll<UserSubscription, String> {

	private DatabaseHelperSecure mDatabaseHelperSecure;
	
	public UserSubscriptionBll(DatabaseHelperSecure mDatabaseHelperSecure) throws SQLException {
		super(mDatabaseHelperSecure.getUserSubscriptionDao());
		this.mDatabaseHelperSecure = mDatabaseHelperSecure;
	}
	
	public UserSubscription getUserInfoByEmail(String mEmail) {
		try {
			QueryBuilder<UserSubscription, String> queryBuilder = mDao.queryBuilder();
			Where<UserSubscription, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.EMAIL, mEmail);
			PreparedQuery<UserSubscription> preparedQuery = queryBuilder.prepare();
			
			List<UserSubscription> mUserList = mDao.query(preparedQuery);
			
			if (mUserList.size() > 0) {
				return mUserList.get(0);
			}
		} catch (SQLException e) {
			////Log.print(e);
		}

		return null;
	}
	
	public boolean limitShareItem() {
		boolean result = false;
		try {
			ShareBll mShareBll = new ShareBll(mDatabaseHelperSecure);
			QueryBuilder<UserSubscription, String> queryBuilder = mDao.queryBuilder();
			Where<UserSubscription, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.EMAIL, Pref.EMAIL);
			PreparedQuery<UserSubscription> preparedQuery = queryBuilder.prepare();
			
			UserSubscription mUserSubscription = null;
			List<UserSubscription> mUserList = mDao.query(preparedQuery);
			if (mUserList.size() > 0) {
				mUserSubscription = mUserList.get(0);
			}
			
//			if (mUserSubscription != null) {
//				if (mUserSubscription.getName().equals(DatabaseConstants.Free)) {
					result = mShareBll.limitShare();
//				}
//			}
		} catch (SQLException e) {
			////Log.print(e);
		}

		return result;
	}
	
}
