package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Share;
import com.passwordboss.android.http.beans.SentHttpBean;
import com.passwordboss.android.utils.Pref;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShareBll extends BaseBll<Share, String> {

    public ShareBll(DatabaseHelperSecure helperSecure) throws SQLException {
        super(helperSecure.getShareDao());
    }

    public List<Share> getAllReceiverItemByType(String type) {
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true).and()
                    .eq(Share.COLUMN_SECURE_ITEM_TYPE_NAME, type).and()
                    .eq(Share.COLUMN_RECEIVER, Pref.EMAIL);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException ignored) {
        }
        return null;
    }

    public List<Share> getAllSendsItemByType(String type) {
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true).and()
                    .eq(Share.COLUMN_SECURE_ITEM_TYPE_NAME, type).and()
                    .eq(Share.COLUMN_SENDER, Pref.EMAIL);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException ignored) {
        }
        return null;
    }

    public List<Share> getAllShareItemActive() {
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException ignored) {
        }
        return null;
    }

    public ArrayList<Share> getExpiredShareItem() {
        List<Share> mShareList;
        ArrayList<Share> resultList;
        resultList = new ArrayList<>();
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            mShareList = mDao.query(preparedQuery);
            for (Share item : mShareList) {
                DateTime itemExpired = new DateTime(item.getExpirationDate());
                DateTime now = DateTime.now();

                if (now.getMillis() >= itemExpired.getMillis()) {
                    if (!item.getStatus().equals(DatabaseConstants.EXPIRED)) {
                        resultList.add(item);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return resultList;
    }

    public List<Share> getSecureItemArrivedFromShareAll() {
        List<Share> mShareList = null;
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            mShareList = mDao.query(preparedQuery);

        } catch (Exception ignored) {
        }
        return mShareList;
    }

    public Share getShareByID(String id) {
        List<Share> mShareList;
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ID, id).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            mShareList = mDao.query(preparedQuery);
            if (mShareList.size() > 0) {
                return mShareList.get(0);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    public List<Share> getShareBySecureItemId(String id) {
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.SECURE_ITEM_ID, id).and().eq(DatabaseConstants.ACTIVE, true)
                    .and().eq(Share.COLUMN_SENDER, Pref.EMAIL);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException ignored) {
        }
        return null;
    }

    public List<Share> getShareBySecureItemId(String id, String email) {
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.SECURE_ITEM_ID, id)
                    .and().eq(DatabaseConstants.ACTIVE, true)
                    .and().eq(Share.COLUMN_RECEIVER, email);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException ignored) {
        }
        return null;
    }

    public List<Share> getShareByStatus(String status) {
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(Share.COLUMN_STATUS, status).and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            return mDao.query(preparedQuery);
        } catch (SQLException ignored) {
        }
        return null;
    }

    public ArrayList<Share> getShareItemForCancel(String email) {
        List<Share> mShareList;
        ArrayList<Share> resultList = new ArrayList<>();
        try {
            mShareList = getAllRecords();
            for (Share item : mShareList) {

                if (item.getReceiver().equals(email) && !item.isActive()) {
                    if (DatabaseConstants.PENDING.equals(item.getStatus()) ||
                            DatabaseConstants.WAITING.equals(item.getStatus()) ||
                            DatabaseConstants.WAITING_DATA.equals(item.getStatus())) {
                        resultList.add(item);
                    }
                }
            }

        } catch (Exception ignored) {
        }
        return resultList;
    }

    public ArrayList<Share> getShareItemForRevoke(String email) {
        List<Share> mShareList;
        ArrayList<Share> resultList = new ArrayList<>();
        try {
            mShareList = getAllRecords();
            for (Share item : mShareList) {
                if (DatabaseConstants.SHARED.equals(item.getStatus()) && item.getReceiver().equals(email) && !item.isActive()) {
                    resultList.add(item);
                }
            }
        } catch (Exception ignored) {
        }
        return resultList;
    }

    public boolean isSecureItemArrivedFromShare(String uuid) {
        boolean secureItemArrivedFromShare = false;
        List<Share> mShareList;
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.SECURE_ITEM_ID, uuid)
                    .and().eq(DatabaseConstants.ACTIVE, true)
                    .and().ne(Share.COLUMN_SENDER, Pref.EMAIL);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            mShareList = mDao.query(preparedQuery);
            if (mShareList.size() > 0) {
                secureItemArrivedFromShare = true;
            }
        } catch (Exception ignored) {
        }
        return secureItemArrivedFromShare;
    }

    public boolean isShareAble(String uuid, String email) {
        boolean result = true;
        List<Share> mShareList;
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true).and().eq(Share.COLUMN_RECEIVER, email).and().eq(DatabaseConstants.SECURE_ITEM_ID, uuid);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            mShareList = mDao.query(preparedQuery);
            if (mShareList.size() > 0) {
                result = false;
            }
        } catch (SQLException ignored) {
        }
        return result;
    }

    public boolean limitShare() {
        boolean result = false;
        try {
            QueryBuilder<Share, String> queryBuilder = mDao.queryBuilder();
            Where<Share, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Share> preparedQuery = queryBuilder.prepare();
            List<Share> listItem = mDao.query(preparedQuery);
            int sumShareItems = 0;
            int sumShareSender = 0;
            int sumShareReceiver = 0;

            for (Share sh : listItem) {
                if (sh.getStatus().equals(DatabaseConstants.PENDING) ||
                        sh.getStatus().equals(DatabaseConstants.SHARED)) {
                    sumShareItems++;
                    if (sh.getReceiver().equals(Pref.EMAIL)) {
                        sumShareReceiver++;
                    }
                    if (sh.getSender().equals(Pref.EMAIL)) {
                        sumShareSender++;
                    }
                }
            }

            if (sumShareItems >= 10 || sumShareReceiver >= 5 || sumShareSender >= 5) {
                result = true;
            }

        } catch (Exception ignored) {
        }
        return result;
    }

    public void updateOrInsertShare(SentHttpBean mSentHttpBean) {
        try {
            boolean isUpdate = false;
            Share mShare = getShareByID(mSentHttpBean.getUuid());
            if (mShare == null) {
                mShare = new Share();
            } else {
                isUpdate = true;
            }
            mShare.setActive(true);
            mShare.setCreatedDate(new DateTime(mSentHttpBean.getCreatedDate(),
                    DateTimeZone.UTC).toDateTimeISO().toString());
            mShare.setLastModifiedDate(new DateTime(mSentHttpBean.getLastModifiedDate(),
                    DateTimeZone.UTC).toDateTimeISO().toString());
            mShare.setExpirationDate(new DateTime(mSentHttpBean.getExpirationDate(),
                    DateTimeZone.UTC).toDateTimeISO().toString());
            mShare.setData(mSentHttpBean.getData());
            mShare.setMessage(mSentHttpBean.getMessage());
            mShare.setReceiver(mSentHttpBean.getReceiver());
            mShare.setStatus(mSentHttpBean.getStatus());
            mShare.setUuid(mSentHttpBean.getUuid());
            mShare.setId(mSentHttpBean.getUuid());
            mShare.setVisible(mSentHttpBean.isVisible());
            if (
                    mSentHttpBean.getStatus().equalsIgnoreCase(DatabaseConstants.CANCELED) ||
                            mSentHttpBean.getStatus().equalsIgnoreCase(DatabaseConstants.REVOKED) ||
                            mSentHttpBean.getStatus().equalsIgnoreCase(DatabaseConstants.REMOVED)) {
                mShare.setActive(false);
            }
            if (mSentHttpBean.getSenderAccount() != null) {
                mShare.setSender(mSentHttpBean.getSenderAccount().getEmail());
            }
            if (mSentHttpBean.getSecureItemType() != null) {
                mShare.setSecureItemTypeName(mSentHttpBean.getSecureItemType().getName());
            }
            if (isUpdate) {
                updateRow(mShare);
            } else {
                insertRow(mShare);
            }
        } catch (Exception ignored) {
        }
    }
}
