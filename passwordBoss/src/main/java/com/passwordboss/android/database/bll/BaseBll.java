package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BaseBll<T, K> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseBll.class);

    protected final Dao<T, K> mDao;

    public BaseBll(Dao<T, K> dao) {
        mDao = dao;
    }

    @NonNull
    public List<T> getAllRecords() {
        try {
            return mDao.queryForAll();
        } catch (SQLException e) {
            LOGGER.error("SQL: getAllRecords", e);
        }
        return Collections.emptyList();
    }


    public boolean insertOrUpdateRow(T entity) {
        try {
            CreateOrUpdateStatus change = mDao.createOrUpdate(entity);
            return change.isCreated() || change.isUpdated();
        } catch (SQLException e) {
            LOGGER.error("SQL: insertOrUpdateRow", e);
        }
        return false;
    }

    public boolean insertRow(T entity) {
        try {
            return mDao.create(entity) == 1;

        } catch (SQLException e) {
            LOGGER.error("SQL: insertRow", e);
        }

        return false;
    }

    public boolean updateRow(T entity) {
        try {
            return mDao.update(entity) == 1;
        } catch (SQLException e) {
            LOGGER.error("SQL: updateRow", e);
        }
        return false;
    }
}
