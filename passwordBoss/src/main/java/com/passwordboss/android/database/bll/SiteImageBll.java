package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.stmt.QueryBuilder;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Site;
import com.passwordboss.android.database.beans.SiteImage;
import com.passwordboss.android.database.beans.SiteImageSize;
import com.passwordboss.android.logback.AppSqlError;

import java.sql.SQLException;


public class SiteImageBll extends BaseBll<SiteImage, String> {

    public SiteImageBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getSiteImageDao());
    }

    @Nullable
    public SiteImage getBySize(@NonNull Site site, @Nullable SiteImageSize siteImageSize) {
        if (null == siteImageSize) return null;
        try {
            QueryBuilder<SiteImage, String> queryBuilder = mDao.queryBuilder();
            queryBuilder
                    .where()
                    .eq(DatabaseConstants.ACTIVE, true)
                    .and()
                    .eq(SiteImage.COLUMN_IMAGE_SIZE, siteImageSize)
                    .and()
                    .eq(SiteImage.COLUMN_SITE_ID, site);
            return mDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return null;

    }
}
