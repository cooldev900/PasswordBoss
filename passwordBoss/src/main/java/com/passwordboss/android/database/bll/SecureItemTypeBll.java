package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItemType;

import java.sql.SQLException;
import java.util.List;


public class SecureItemTypeBll extends BaseBll<SecureItemType, String> {

	public SecureItemTypeBll(DatabaseHelperSecure helperSecure) throws SQLException {
		super(helperSecure.getSecureItemTypeDao());
	}
	
	public SecureItemType getSecureItemTypeByName(String name) {
		SecureItemType secureItemType = null;
		try {
			QueryBuilder<SecureItemType, String> queryBuilder = mDao.queryBuilder();
			Where<SecureItemType, String> where = queryBuilder.where();
			where.and(where.eq(DatabaseConstants.NAME, name), 
					where.eq(DatabaseConstants.ACTIVE, true));
			PreparedQuery<SecureItemType> preparedQuery = queryBuilder.prepare();
			List<SecureItemType> allSecureItemType = mDao.query(preparedQuery);
			if (allSecureItemType.size() > 0) {
				secureItemType =  allSecureItemType.get(0);
			}
		} catch(Exception e) {
			////Log.print(e);
		}
		
		return secureItemType;
	}
}
