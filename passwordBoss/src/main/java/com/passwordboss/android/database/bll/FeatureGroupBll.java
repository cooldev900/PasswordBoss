package com.passwordboss.android.database.bll;

import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.FeatureGroup;

import java.sql.SQLException;


public class FeatureGroupBll extends BaseBll<FeatureGroup, String> {
    public FeatureGroupBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getFeatureGroupDao());
    }

    public void deleteAll() {
        try {
            mDao.executeRawNoArgs("delete from feature_group");
        } catch (Exception e) {
            ////Log.print(e);
        }
    }
}