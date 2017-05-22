package com.passwordboss.android.database.bll;

import android.support.annotation.Nullable;

import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SiteImageSize;
import com.passwordboss.android.logback.AppSqlError;

import java.sql.SQLException;
import java.util.List;


public class SiteImageSizeBll extends BaseBll<SiteImageSize, String> {

    public SiteImageSizeBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getSiteImageSizeDao());
    }

    @Nullable
    public SiteImageSize getMaxImageSize() {
        try {
            List<SiteImageSize> sizes = mDao.queryForAll();// TODO: 6/3/2016   queryForEq(DatabaseConstants.ACTIVE, true);, see PBA-907
            SiteImageSize maxSize = null;
            for (SiteImageSize size : sizes) {
                if (null == maxSize) {
                    maxSize = size;
                    continue;
                }
                if (size.getHeight() > maxSize.getHeight() || size.getWidth() > maxSize.getWidth()) {
                    maxSize = size;
                }
            }
            return maxSize;
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return null;
    }


}
