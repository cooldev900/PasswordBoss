package com.passwordboss.android.database.bll;

import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.FeatureGroupFeature;

import java.sql.SQLException;


public class FeatureGroupFeatureBll extends BaseBll<FeatureGroupFeature, String> {
    public FeatureGroupFeatureBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getFeatureGroupFeatureDao());
    }

    public void deleteAll() {
        try {
            mDao.executeRawNoArgs("delete from feature_group_feature");
        } catch (Exception e) {
            ////Log.print(e);
        }
    }
}

