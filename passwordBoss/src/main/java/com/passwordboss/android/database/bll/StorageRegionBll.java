package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.StorageRegion;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class StorageRegionBll extends BaseBll<StorageRegion, String> {
	public StorageRegionBll(DatabaseHelperSecure helperSecure) throws SQLException {
		super(helperSecure.getStorageRegionDao());
	}
	
	public StorageRegion getStorageRegionByUuid(String uuid) {
		try {
			QueryBuilder<StorageRegion, String> queryBuilder = mDao.queryBuilder();
			Where<StorageRegion, String> where = queryBuilder.where();
			
			where.eq(DatabaseConstants.UUID, uuid).and().eq(DatabaseConstants.ACTIVE, true);
			PreparedQuery<StorageRegion> preparedQuery = queryBuilder.prepare();
			List<StorageRegion> mStorageRegionList = mDao.query(preparedQuery);
			if (mStorageRegionList.size() > 0) {
				return mStorageRegionList.get(0);
			} 
		} catch (Exception e) {
			////Log.print(e);
		}
		return null;
	}

	@NonNull
	public List<StorageRegion> getAllStorageRegion() {
		try {
			QueryBuilder<StorageRegion, String> queryBuilder = mDao.queryBuilder();
			Where<StorageRegion, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.ACTIVE, true);
			PreparedQuery<StorageRegion> preparedQuery = queryBuilder.prepare();
			return mDao.query(preparedQuery);
		} catch (Exception e) {
			////Log.print(e);
		}
		return Collections.emptyList();
	}
	
}