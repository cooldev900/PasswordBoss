package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.SiteDomain;

import java.sql.SQLException;
import java.util.List;



public class SiteDomainBll extends BaseBll<SiteDomain, Integer> {
	private DatabaseHelperNonSecure mDatabseHelper;	
	public SiteDomainBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getSiteDomainDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
	public void updateOrInsertItem(String uuid, String domain, String creatDate, String last_modified_data, boolean active) {
		String query = "INSERT OR REPLACE INTO site_domains  ('uuid','domain','created_date','last_modified_date','active') VALUES (?,?,?,?,?)";
		try {
			
		} catch (Exception e) {
			////Log.print(e);
		}
	}
	public SiteDomain getSiteDomainByDomain(String domain) {
		try {
			try {
				List<SiteDomain> mSiteDomainList = null;
				QueryBuilder<SiteDomain, Integer> queryBuilder = mDao.queryBuilder();
				Where<SiteDomain, Integer> where = queryBuilder.where();
				where.eq(DatabaseConstants.DOMAIN, domain).and()
					.eq(DatabaseConstants.ACTIVE, true);
				PreparedQuery<SiteDomain> preparedQuery = queryBuilder.prepare();
				mSiteDomainList = mDao.query(preparedQuery);
				if (mSiteDomainList != null && mSiteDomainList.size() > 0) {
					return mSiteDomainList.get(0);
				}
			} catch (Exception e) {
				////Log.print(e);
			}
			
		} catch (Exception e) {
			////Log.print(e);
		}
		return null;
	}
}
