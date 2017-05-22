package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Country;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;



public class CountryBll extends BaseBll<Country, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	public CountryBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getCountryDao());
		this.mDatabseHelper = mDatabseHelper;
	}

	public String[] getAllCountryName() {
		String[] result = {""};
		try {
			QueryBuilder<Country, Integer> queryBuilder = mDao.queryBuilder();
			queryBuilder.where().eq(DatabaseConstants.ACTIVE, true);

			PreparedQuery<Country> preparedQuery = queryBuilder.prepare();
			List<Country> allCountry = null;
			if (mDao != null){
				allCountry = mDao.query(preparedQuery);
			}

			if (allCountry != null && allCountry.size() > 0) {
				String r[] = new String[allCountry.size()];
				for (int i = 0; i < allCountry.size(); i++) {
					r[i] = allCountry.get(i).getName();
				}
				result = r;
			}
		} catch (SQLException e) {
			////Log.print(e);
		}
		return result;
	}


	public Country getCategoryByCode(String code) {
	    try {
	        QueryBuilder<Country, Integer> queryBuilder = mDao.queryBuilder();
	        Where<Country, Integer> where = queryBuilder.where();

	        where.and(where.eq(DatabaseConstants.CODE, code),
	                where.eq(DatabaseConstants.ACTIVE, true));
	        PreparedQuery<Country> preparedQuery = queryBuilder.prepare();

	        List<Country> mCategoryList = mDao.query(preparedQuery);
	        if (mCategoryList.size() > 0) {
	            return mCategoryList.get(0);
	        }
	    } catch (SQLException e) {
	        ////Log.print(e);
	    }
	    return null;
	}

	public Country getCategoryByName(String name) {
		try {
			QueryBuilder<Country, Integer> queryBuilder = mDao.queryBuilder();
			Where<Country, Integer> where = queryBuilder.where();

			where.and(where.eq(DatabaseConstants.NAME, name),
					where.eq(DatabaseConstants.ACTIVE, true));
			PreparedQuery<Country> preparedQuery = queryBuilder.prepare();

			List<Country> mCategoryList = mDao.query(preparedQuery);
			if (mCategoryList.size() > 0) {
				return mCategoryList.get(0);
			}
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}

	@NonNull
	public List<Country> getAllCountry() {
		try {
			QueryBuilder<Country, Integer> queryBuilder = mDao.queryBuilder();
			queryBuilder.where().eq(DatabaseConstants.ACTIVE, true);
			queryBuilder.orderBy(DatabaseConstants.NAME, true);
			PreparedQuery<Country> preparedQuery = queryBuilder.prepare();
			if (mDao != null){
				return mDao.query(preparedQuery);
			}
		} catch (SQLException e) {
			////Log.print(e);
		}
		return Collections.emptyList();
	}

}