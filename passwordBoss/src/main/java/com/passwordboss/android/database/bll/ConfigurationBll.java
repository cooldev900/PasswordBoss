package com.passwordboss.android.database.bll;

import android.text.TextUtils;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Configuration;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class ConfigurationBll extends BaseBll<Configuration, Integer> {

    public static final int DEFAULT_AUTO_LOCK_TIME = 1000 * 30;

    public ConfigurationBll(DatabaseHelperNonSecure helperNonSecure) throws SQLException {
        super(helperNonSecure.getConfigurationDao());
    }

    public long getAutoLockTime(String email) {
        switch (getAutoLockValue(email)) {
            case 0:
            default:
                return DEFAULT_AUTO_LOCK_TIME;
            case 1:
                return 1000 * 60;
            case 2:
                return 1000 * 60 * 2;
            case 3:
                return 1000 * 60 * 3;
            case 4:
                return 1000 * 60 * 5;
            case 5:
                return 1000 * 60 * 10;
        }
    }

    public int getAutoLockValue(String email) {
        Configuration configuration = getConfigurationByEmailAndKey(email, DatabaseConstants.AUTOLOCK);
        if (null != configuration) {
            try {
                return Integer.parseInt(configuration.getValue());
            } catch (NumberFormatException ignored) {
            }
        }
        return 3;
    }

    public Configuration getConfigurationByEmailAndKey(String email, String key) {
        try {
            QueryBuilder<Configuration, Integer> queryBuilder = mDao.queryBuilder();
            Where<Configuration, Integer> where = queryBuilder.where();
            where.eq(DatabaseConstants.KEY, key)
                    .and().eq(DatabaseConstants.ACTIVE, true)
                    .and().eq(DatabaseConstants.ACCOUNT_EMAIL, email);

            PreparedQuery<Configuration> preparedQuery = queryBuilder.prepare();

            List<Configuration> configurations = mDao.query(preparedQuery);
            if (configurations.size() > 0) {
                return configurations.get(0);
            }
        } catch (SQLException e) {
            ////Log.print(e);
        }
        return null;
    }

    public Configuration getConfigurationValueByKey(String key) {
        try {
            QueryBuilder<Configuration, Integer> queryBuilder = mDao.queryBuilder();
            Where<Configuration, Integer> where = queryBuilder.where();
            where.eq(DatabaseConstants.KEY, key).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Configuration> preparedQuery = queryBuilder.prepare();
            List<Configuration> configurations = mDao.query(preparedQuery);
            if (configurations.size() > 0) {
                return configurations.get(0);
            }
        } catch (SQLException e) {
            ////Log.print(e);
        }
        return null;
    }

    public void insertItem(String email, String key, String value) {
        Configuration mConfiguration = new Configuration();
        mConfiguration.setAccountEmail(email);
        mConfiguration.setActive(true);
        mConfiguration.setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
        mConfiguration.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
        mConfiguration.setKey(key);
        mConfiguration.setUuid(UUID.randomUUID().toString());
        mConfiguration.setValue(value);
        mConfiguration.setOrder(0);
        insertRow(mConfiguration);
    }

    public void replaceNoEmailForLanguageKey(String email) throws SQLException {
        if (TextUtils.isEmpty(email)) return;
        String UPDATE_QUERY = "UPDATE `configuration` SET account_e_mail=? WHERE account_e_mail=? and key=?";
        mDao.executeRaw(UPDATE_QUERY, email, Constants.NO_EMAIL, DatabaseConstants.LANGUAGE);
    }

    public void updateItem(String email, String key, String value) {
        try {
            String UPDATE_QUERY = "UPDATE `configuration` SET value=?, " +
                    "last_modified_date=? WHERE account_e_mail=? and key=?";

            mDao.executeRaw(UPDATE_QUERY, value,
                    DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString(),
                    email, key);
        } catch (SQLException e) {
            ////Log.print(e);
        }
    }

    public void updateOrInsertItem(String email, String key, String value) {
        try {
            if (email != null) {
                Configuration mConfiguration = getConfigurationByEmailAndKey(email, key);
                if (mConfiguration != null) {
                    updateItem(email, key, value);
                } else {
                    insertItem(email, key, value);
                }
            }
        } catch (Exception e) {
            ////Log.print(e);
        }
    }

    /**
     * Search and compare value by provided search criteria
     *
     * @param email search criteria
     * @param key   search criteria
     * @param value value to compare
     * @return false - no value was found by provided search criteria, true - value was found and it equals provided one
     */
    public boolean valueEquals(String email, String key, String value) {
        Configuration configuration = getConfigurationByEmailAndKey(email, key);
        return null != configuration && configuration.valueEquals(value);
    }
}
