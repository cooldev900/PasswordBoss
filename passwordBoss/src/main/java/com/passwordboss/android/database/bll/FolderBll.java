package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Folder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class FolderBll extends BaseBll<Folder, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderBll.class);

    public FolderBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getCategoryDao());
    }

    @NonNull
    public List<Folder> getAllActive() {
        try {
            QueryBuilder<Folder, String> queryBuilder = mDao.queryBuilder();
            Where<Folder, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true);
            queryBuilder.orderBy(DatabaseConstants.NAME, true);
            PreparedQuery<Folder> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException e) {
            LOGGER.error("SQL: getAllActive", e);
        }
        return Collections.emptyList();
    }

    @Nullable
    public Folder getById(String id) {
        try {
            QueryBuilder<Folder, String> queryBuilder = mDao.queryBuilder();
            Where<Folder, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ID, id).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Folder> preparedQuery = queryBuilder.prepare();
            List<Folder> mFolderList = mDao.query(preparedQuery);
            if (mFolderList.size() > 0) {
                return mFolderList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL: getById", e);
        }
        return null;
    }

}