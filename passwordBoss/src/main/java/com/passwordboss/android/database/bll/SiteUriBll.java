package com.passwordboss.android.database.bll;

import android.support.annotation.Nullable;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Site;
import com.passwordboss.android.database.beans.SiteUri;
import com.passwordboss.android.logback.AppSqlError;

import java.sql.SQLException;
import java.util.List;


public class SiteUriBll extends BaseBll<SiteUri, String> {

    public SiteUriBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getSiteUriDao());
    }

    public boolean deleteSiteUriByUriAndSiteId(String uri, String siteId) {
        try {
            String query = "DELETE FROM site_uri WHERE uri='" + uri + "' AND site_id='" + siteId + "'";

            mDao.executeRaw(query);
            return true;
        } catch (Exception e) {
            ////Log.print(e);
        }
        return false;
    }

    public List<SiteUri> getAllRecordsWhereUuuidIsNull() {
        try {
            QueryBuilder<SiteUri, String> queryBuilder = mDao.queryBuilder();
            Where<SiteUri, String> where = queryBuilder.where();
            where.isNull(DatabaseConstants.UUID).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<SiteUri> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException e) {
            ////Log.print(e);
        }
        return null;
    }

    @Nullable
    public SiteUri getBySite(Site site) {
        try {
            QueryBuilder<SiteUri, String> queryBuilder = mDao.queryBuilder();
            queryBuilder.where().eq(DatabaseConstants.ACTIVE, true).and().eq(SiteUri.COLUMN_SITE_ID, site);
            return mDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return null;
    }

    public SiteUri getSiteUriBySiteId(String siteId) {
        try {
            QueryBuilder<SiteUri, String> queryBuilder = mDao.queryBuilder();
            Where<SiteUri, String> where = queryBuilder.where();
            where.eq(SiteUri.COLUMN_SITE_ID, siteId).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<SiteUri> preparedQuery = queryBuilder.prepare();
            List<SiteUri> mSiteUriList = mDao.query(preparedQuery);
            if (mSiteUriList.size() >= 1) {
                return mSiteUriList.get(0);
            }
        } catch (SQLException e) {
            ////Log.print(e);
        }
        return null;
    }
}
