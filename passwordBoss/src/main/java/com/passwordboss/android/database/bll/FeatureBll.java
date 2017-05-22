package com.passwordboss.android.database.bll;

import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Feature;

import java.sql.SQLException;


public class FeatureBll extends BaseBll<Feature, String> {

    public FeatureBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getFeatureDao());
    }

    public void deleteAll() {
        try {
            mDao.executeRawNoArgs("delete from feature");
        } catch (Exception e) {
            ////Log.print(e);
        }
    }

}