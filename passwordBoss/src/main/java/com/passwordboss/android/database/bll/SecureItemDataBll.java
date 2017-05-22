package com.passwordboss.android.database.bll;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.logback.AppSqlError;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.SQLException;
import java.util.List;


public class SecureItemDataBll extends BaseBll<SecureItemData, Void> {

    public SecureItemDataBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getSecureItemDataDao());
    }

    public int deleteAll(SecureItem secureItem) throws SQLException {
        DeleteBuilder<SecureItemData, Void> builder = mDao
                .deleteBuilder();
        builder.where().eq(DatabaseConstants.SECURE_ITEM_ID, secureItem.getId());
        return mDao.delete(builder.prepare());
    }

    public boolean deleteById(String id) {
        try {
            QueryBuilder<SecureItemData, Void> queryBuilder = mDao.queryBuilder();
            Where<SecureItemData, Void> where = queryBuilder.where();
            where.eq(DatabaseConstants.SECURE_ITEM_ID, id).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<SecureItemData> preparedQuery = queryBuilder.prepare();
            List<SecureItemData> mSecureItemDataList = mDao.query(preparedQuery);
            for (SecureItemData secureItem : mSecureItemDataList) {
                secureItem.setActive(false);
                secureItem.setLastModifiedDate(new DateTime(DateTimeZone.UTC).toDateTimeISO().toString());
                secureItem.calculateHash();
                deleteSecureItemData(id, secureItem.getHash());
            }
            return true;
        } catch (Exception e) {
            ////Log.print(e);
        }
        return false;

    }

    public void deleteSecureItemData(String secureItemId, String hash) {
        try {
            String UPDATE_QUERY = "UPDATE `secure_item_data` SET active=?, " +
                    "last_modified_date=?, hash=? WHERE secure_item_id=?";

            mDao.executeRaw(UPDATE_QUERY, "0",
                    DateTime.now(DateTimeZone.UTC).toDateTimeISO().getMillis() + "",
                    hash, secureItemId);
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
    }

    @Nullable
    public SecureItemData findSecureItemData(String secureItemId, String identifier) throws SQLException {
        QueryBuilder<SecureItemData, Void> queryBuilder = mDao.queryBuilder();
        queryBuilder.where()
                .eq(DatabaseConstants.SECURE_ITEM_ID, secureItemId)
                .and()
                .eq(DatabaseConstants.ACTIVE, true)
                .and()
                .eq(SecureItemData.COLUMN_IDENTIFIER, identifier);
        return mDao.queryForFirst(queryBuilder.prepare());
    }

    @NonNull
    public List<SecureItemData> getSecureItemDataList(String id) throws SQLException {
        QueryBuilder<SecureItemData, Void> queryBuilder = mDao.queryBuilder();
        queryBuilder.where()
                .eq(DatabaseConstants.SECURE_ITEM_ID, id)
                .and()
                .eq(DatabaseConstants.ACTIVE, true);
        return mDao.query(queryBuilder.prepare());
    }
}
