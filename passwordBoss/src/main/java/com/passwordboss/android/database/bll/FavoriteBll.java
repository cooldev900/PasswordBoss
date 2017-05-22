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
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Favorite;

public class FavoriteBll extends BaseBll<Favorite, String> {
	public FavoriteBll(DatabaseHelperSecure helperSecure) throws SQLException {
		super(helperSecure.getFavoriteDao());
	}
	
	public void insertFavorite(String url, String name) {
		try {
			Favorite mFavorite = new Favorite();
			mFavorite.setActive(true);
			mFavorite.setOrder(0);
			mFavorite.setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
			mFavorite.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
			mFavorite.setUuid(UUID.randomUUID().toString());
			mFavorite.setUrl(url);
			mFavorite.setName(name);
			mFavorite.calculateHash();
			insertRow(mFavorite);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFavorite(String uuid, boolean delete){
		try {
			Favorite mFavorite = getById(uuid);
			if (mFavorite != null) {
				mFavorite.setActive(delete);
				mFavorite.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
				mFavorite.calculateHash();
				updateRow(mFavorite);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void updateFavorite(String uuid, String url, String name){
		try {
			Favorite mFavorite = getById(uuid);
			mFavorite.setUrl(url);
			mFavorite.setName(name);
			mFavorite.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
			mFavorite.calculateHash();
			updateRow(mFavorite);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public Favorite getById(String uuid) {
		try {
			QueryBuilder<Favorite, String> queryBuilder = mDao.queryBuilder();
			Where<Favorite, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.UUID, uuid);
			PreparedQuery<Favorite> preparedQuery = queryBuilder.prepare();
			List<Favorite> mFavoriteList = mDao.query(preparedQuery);
			if (mFavoriteList != null && mFavoriteList.size() > 0) {
				return mFavoriteList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Favorite getByUrl(String url) {
		try {
			QueryBuilder<Favorite, String> queryBuilder = mDao.queryBuilder();
			Where<Favorite, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.URL, url);
			PreparedQuery<Favorite> preparedQuery = queryBuilder.prepare();
			List<Favorite> mFavoriteList = mDao.query(preparedQuery);
			if (mFavoriteList != null && mFavoriteList.size() > 0) {
				return mFavoriteList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Favorite> getAllFavorites() {
		try {
			QueryBuilder<Favorite, String> queryBuilder = mDao.queryBuilder();
			Where<Favorite, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.ACTIVE, true);
			PreparedQuery<Favorite> preparedQuery = queryBuilder.prepare();
			List<Favorite> mFavoriteList = mDao.query(preparedQuery);
			return mFavoriteList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}


