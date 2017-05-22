package com.passwordboss.android.database.bll;

import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.analytics.DailyAnalyticsHelper;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.logback.AppSqlError;
import com.passwordboss.android.model.ItemType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.exceptions.Exceptions;


public class SecureItemBll extends BaseBll<SecureItem, String> {

    private DatabaseHelperSecure mDatabaseHelper;

    public SecureItemBll(DatabaseHelperSecure helperSecure)
            throws SQLException {
        super(helperSecure.getSecureItemDao());
        mDatabaseHelper = helperSecure;
    }

    public static Observable<SecureItemBll> getObservable() {
        return Observable.defer(() -> DatabaseHelperSecure.getObservable().map(helperSecure -> {
            try {
                return new SecureItemBll(helperSecure);
            } catch (SQLException e) {
                throw Exceptions.propagate(e);
            }
        }));
    }

    public boolean deleteItemByID(String uuid) {
        try {
            SecureItem secureItem = findSecureItemById(uuid);
            if (secureItem != null) {
                secureItem.setActive(false);
                secureItem.setLastModifiedDate(DateTime.now(DateTimeZone.UTC)
                        .toDateTimeISO().toString());
                secureItem.calculateHash();
                mDao.update(secureItem);

                SecureItemDataBll mSecureItemDataBll = new SecureItemDataBll(
                        mDatabaseHelper);
                mSecureItemDataBll.deleteById(uuid);

                return true;
            }
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return false;
    }

    @Nullable
    public SecureItem findSecureItemById(String id) throws SQLException {
        if (Strings.isNullOrEmpty(id)) return null;
        QueryBuilder<SecureItem, String> queryBuilder = mDao.queryBuilder();
        queryBuilder.where()
                .eq(DatabaseConstants.ID, id)
                .and()
                .eq(DatabaseConstants.ACTIVE, true);
        return mDao.queryForFirst(queryBuilder.prepare());
    }

    public DailyAnalyticsHelper getAnalysis() {
        DailyAnalyticsHelper dailyAnalyticsHelper = new DailyAnalyticsHelper();
        String query1 = "select (select count(*) from secure_item where active=1 and secure_item_type_name='PV') as pv_cnt, (select count(*) from secure_item where active=1 and secure_item_type_name='DW') as dw_cnt, (select count(*) from secure_item where active=1 and secure_item_type_name='PI') as pi_cnt;";
        String query2 = "select ifnull((select sum(access_count) from secure_item where secure_item_type_name='PV' and access_count>=0),0) as total_logins;";
        String query3 = "select (select count(*) from secure_item si join secure_item_stats st on si.id=st.secure_item_id where si.active=1) as total_pwd_cnt, (select count(*) from secure_item si join secure_item_stats st on si.id=st.secure_item_id where si.active=1 and st.is_weak=1) as weak_pwds_cnt, (select count(*) from secure_item si join secure_item_stats st on si.id=st.secure_item_id where si.active=1 and st.has_duplicate=1) as duplicate_pwds_cnt, (select count(*) from secure_item si join secure_item_stats st on si.id=st.secure_item_id where si.active=1 and (julianday('now') - julianday(st.last_password_change))>=365) as old_pwds_cnt, 0.00 as dup_pwd_score,0.00 as old_pwd_score,0.00 as weak_pwd_score,0.00 as total_pwd_score;";
        try {
            GenericRawResults<String[]> rawResults1;
            rawResults1 = mDao.queryRaw(query1);
            ArrayList<String[]> mList1 = (ArrayList<String[]>) rawResults1
                    .getResults();
            for (String[] strings : mList1) {
                dailyAnalyticsHelper.setPv_cnt(Long.parseLong(strings[0]));
                dailyAnalyticsHelper.setDw_cnt(Long.parseLong(strings[1]));
                dailyAnalyticsHelper.setPi_cnt(Long.parseLong(strings[2]));
            }

            GenericRawResults<String[]> rawResults2;
            rawResults2 = mDao.queryRaw(query2);
            ArrayList<String[]> mList2 = (ArrayList<String[]>) rawResults2
                    .getResults();
            for (String[] strings : mList2) {
                dailyAnalyticsHelper
                        .setTotal_logins(Long.parseLong(strings[0]));
            }

            GenericRawResults<String[]> rawResults3;
            rawResults3 = mDao.queryRaw(query3);
            ArrayList<String[]> mList3 = (ArrayList<String[]>) rawResults3
                    .getResults();
            for (String[] strings : mList3) {
                dailyAnalyticsHelper.setTotal_pwd_cnt(
                        Integer.parseInt(strings[0]));
                dailyAnalyticsHelper.setWeak_pwds_cnt(Long
                        .parseLong(strings[1]));
                dailyAnalyticsHelper.setDuplicate_pwds_cnt(Long
                        .parseLong(strings[2]));
                dailyAnalyticsHelper
                        .setOld_pwds_cnt(Long.parseLong(strings[3]));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyAnalyticsHelper;

    }

    public long getCountOfSecureItemsByType(ItemType type) throws SQLException {
        QueryBuilder<SecureItem, String> builder = mDao.queryBuilder();
        Where<SecureItem, String> where = builder.where().eq(DatabaseConstants.ACTIVE, true);
        if (null != type) {
            if (type.hasChildren()) {
                where.and().eq(SecureItem.COLUMN_SECURE_ITEM_TYPE_NAME, ItemType.toSecureItemType(type));
            } else {
                where.and().eq(SecureItem.COLUMN_TYPE, ItemType.toType(type));
            }
        }
        builder.orderBy(DatabaseConstants.NAME, true);
        builder.setCountOf(true);
        return builder.countOf();
    }

    public List<SecureItem> getSecureItemBySiteId(String siteId) {
        try {
            QueryBuilder<SecureItem, String> queryBuilder = mDao
                    .queryBuilder();
            Where<SecureItem, String> where = queryBuilder.where();
            where.eq(SecureItem.COLUMN_SITE_ID, siteId).and()
                    .eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<SecureItem> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Observable<SecureItem> getSecureItemsByType(ItemType type) {
        return Observable.defer(() -> {
            try {
                QueryBuilder<SecureItem, String> builder = mDao.queryBuilder();
                Where<SecureItem, String> where = builder.where().eq(DatabaseConstants.ACTIVE, true);
                if (null != type) {
                    if (type.hasChildren()) {
                        where.and().eq(SecureItem.COLUMN_SECURE_ITEM_TYPE_NAME, ItemType.toSecureItemType(type));
                    } else {
                        where.and().eq(SecureItem.COLUMN_TYPE, ItemType.toType(type));
                    }
                }
                builder.orderBy(DatabaseConstants.NAME, true);
                return Observable.from(builder.query());
            } catch (SQLException e) {
                return Observable.error(e);
            }
        });

    }

}
