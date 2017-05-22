package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.UserInfo;

import java.sql.SQLException;
import java.util.List;


public class UserInfoBll extends BaseBll<UserInfo,String> {
	
	public UserInfoBll(DatabaseHelperSecure helperSecure) throws SQLException{
		super(helperSecure.getUserInfoDao());
	}

	public UserInfo getUserInfoByEmail(String mEmail) {
		try {
			QueryBuilder<UserInfo, String> queryBuilder = mDao.queryBuilder();
			Where<UserInfo, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.EMAIL, mEmail);
			PreparedQuery<UserInfo> preparedQuery = queryBuilder.prepare();
			
			List<UserInfo> mUserList = mDao.query(preparedQuery);
			
			if (mUserList.size() > 0) {
				return mUserList.get(0);
			}

		} catch (SQLException e) {
			////Log.print(e);
		}

		return null;
	}

}
