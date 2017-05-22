package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Language;

import java.sql.SQLException;
import java.util.List;


public class LanguageBll extends BaseBll<Language, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public LanguageBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getLanguageDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
	public Language getLanguagesByUuid(String uuid) {
		try {
			QueryBuilder<Language, Integer> queryBuilder = mDao.queryBuilder();
			Where<Language, Integer> where = queryBuilder.where();
			
			where.and(where.eq(DatabaseConstants.UUID, uuid), 
					where.eq(DatabaseConstants.ACTIVE, true));
			PreparedQuery<Language> preparedQuery = queryBuilder.prepare();

			List<Language> mLanguageList = mDao.query(preparedQuery);
			if (mLanguageList.size() > 0) {
				return mLanguageList.get(0);
			} 
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}	
	
	public List<Language> getAllLanguages() {
		try {
			QueryBuilder<Language, Integer> queryBuilder = mDao.queryBuilder();
			Where<Language, Integer> where = queryBuilder.where();
			where.eq(DatabaseConstants.ACTIVE, true);
			PreparedQuery<Language> preparedQuery = queryBuilder.prepare();
			List<Language> mLanguageList = mDao.query(preparedQuery);
			return mLanguageList;	 
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}
	
}
