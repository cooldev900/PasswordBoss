package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.UserIdentity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class UserIdentityBll extends BaseBll<UserIdentity, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserIdentityBll.class);

    public UserIdentityBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getUserIdentityDao());
    }

    public void delete(@NonNull UserIdentity userIdentity) {
        userIdentity.setActive(false);
        updateRow(userIdentity);
    }

    @Nullable
    public UserIdentity getById(String id) {
        try {
            QueryBuilder<UserIdentity, String> queryBuilder = mDao.queryBuilder();
            queryBuilder
                    .where()
                    .eq(DatabaseConstants.ID, id)
                    .and()
                    .eq(DatabaseConstants.ACTIVE, true);
            return mDao.queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            LOGGER.error("SQL: getById", e);
        }
        return null;
    }

    @Nullable
    public UserIdentity getDefault() {
        try {
            QueryBuilder<UserIdentity, String> queryBuilder = mDao.queryBuilder();
            Where<UserIdentity, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.DEFAULT, true).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<UserIdentity> preparedQuery = queryBuilder.prepare();
            List<UserIdentity> mUserIdentityList = mDao.query(preparedQuery);
            if (mUserIdentityList.size() > 0) {
                return mUserIdentityList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL: getById", e);
        }
        return null;
    }

    public List<UserIdentity> getIdentities() throws SQLException {
        QueryBuilder<UserIdentity, String> queryBuilder = mDao.queryBuilder();
        queryBuilder.where()
                .eq(DatabaseConstants.ACTIVE, true);
        return mDao.query(queryBuilder.prepare());
    }
}
