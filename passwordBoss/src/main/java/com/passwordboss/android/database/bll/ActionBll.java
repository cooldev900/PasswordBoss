package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Action;
import com.passwordboss.android.utils.Pref;

import java.sql.SQLException;
import java.util.List;

public class ActionBll extends BaseBll<Action, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public ActionBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getActionDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
	public List<Action> getActionForQueue() {
		try {
			QueryBuilder<Action, Integer> queryBuilder = mDao.queryBuilder();
			queryBuilder.orderBy(DatabaseConstants.ACTION_START_DATE, true);
			Where<Action, Integer> where = queryBuilder.where();
			where.eq(DatabaseConstants.STATUS, DatabaseConstants.INCOMPLITED)
				.and().eq(DatabaseConstants.ACTIVE, true).and().eq(DatabaseConstants.ACCOUNT_EMAIL, Pref.EMAIL)
				.and().isNull(DatabaseConstants.ACTION_COMPLETED_DATE);
			PreparedQuery<Action> preparedQuery = queryBuilder.prepare();
			List<Action> mActionList = mDao.query(preparedQuery);
			//android.util.Log.v("Test", "mActionList:" + mActionList.size());
			return mActionList;
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}
	
	public Action getActionById(String id) {
		try {
			QueryBuilder<Action, Integer> queryBuilder = mDao.queryBuilder();
			Where<Action, Integer> where = queryBuilder.where();
			where.and(where.eq(DatabaseConstants.ID, id), where.eq(DatabaseConstants.ACTIVE, true));
			PreparedQuery<Action> preparedQuery = queryBuilder.prepare();
			List<Action> mActionList = mDao.query(preparedQuery);
			if (mActionList.size() > 0) {
				return mActionList.get(0);
			} 
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}	
	
	public Action getActionByName(String name) {
		try {
			QueryBuilder<Action, Integer> queryBuilder = mDao.queryBuilder();
			Where<Action, Integer> where = queryBuilder.where();
			where.eq(DatabaseConstants.NAME, name);
			
			PreparedQuery<Action> preparedQuery = queryBuilder.prepare();
			List<Action> mActionList = mDao.query(preparedQuery);

			if (mActionList.size() > 0) {
				return mActionList.get(0);
			} 
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}
	
	public boolean deleteByNamePhysical(String name) {
		try {
	        DeleteBuilder<Action, Integer> deleteBuilder = mDao
                .deleteBuilder();
	        deleteBuilder.where().eq(DatabaseConstants.NAME, name);
	        mDao.delete(deleteBuilder.prepare());
		} catch (Exception e) {
			////Log.print(e);
		}
		return false;
	}
}
