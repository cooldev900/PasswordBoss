package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Site;
import com.passwordboss.android.logback.AppSqlError;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SiteBll extends BaseBll<Site, String> {

    public SiteBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getSiteDao());
    }

    public boolean deleteBySiteId(String id) {
        try {
            DeleteBuilder<Site, String> deleteBuilder = mDao.deleteBuilder();
            deleteBuilder.where().eq(DatabaseConstants.ID, id);
            mDao.delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return false;
    }

    @NonNull
    public List<Site> getRecommendedSites() {
        try {
            QueryBuilder<Site, String> queryBuilder = mDao.queryBuilder();
            Where<Site, String> where = queryBuilder.where();
            where.eq(Site.COLUMN_RECOMMENDED, true).and().eq(DatabaseConstants.ACTIVE, true);
            queryBuilder.orderBy(DatabaseConstants.ORDER, true);
            PreparedQuery<Site> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return Collections.emptyList();

    }

    public Site getSiteById(String id) {
        try {
            QueryBuilder<Site, String> queryBuilder = mDao.queryBuilder();
            Where<Site, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ID, id).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Site> preparedQuery = queryBuilder.prepare();

            List<Site> mSiteList = mDao.query(preparedQuery);
            if (mSiteList.size() > 0) {
                return mSiteList.get(0);
            }
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return null;
    }

    public Site getSiteByUuid(String uuid) {
        try {
            QueryBuilder<Site, String> queryBuilder = mDao.queryBuilder();
            Where<Site, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.UUID, uuid).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Site> preparedQuery = queryBuilder.prepare();

            List<Site> mSiteList = mDao.query(preparedQuery);
            if (mSiteList.size() > 0) {
                return mSiteList.get(0);
            }
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return null;
    }
}
