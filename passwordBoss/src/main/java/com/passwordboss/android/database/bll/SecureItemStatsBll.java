package com.passwordboss.android.database.bll;

import android.app.Activity;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.R;
import com.passwordboss.android.beans.NotificationBean;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.SecureItemStats;
import com.passwordboss.android.utils.Pref;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecureItemStatsBll extends BaseBll<SecureItemStats, String> {
	public SecureItemStatsBll(DatabaseHelperSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getSecureItemStatsDao());
	}
	
	public SecureItemStats getSecureItemStatsByID(String id) {
		try {
			QueryBuilder<SecureItemStats, String> queryBuilder = mDao.queryBuilder();
			Where<SecureItemStats, String> where = queryBuilder.where();
			where.eq(SecureItemStats.COLUMN_SECURE_ITEM_ID, id);
			PreparedQuery<SecureItemStats> preparedQuery = queryBuilder.prepare();
			List<SecureItemStats> mSecureItemStatusList = mDao.query(preparedQuery);
			if (mSecureItemStatusList.size() > 0) {
				return mSecureItemStatusList.get(0);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public void updateItem(String id, String lastPasswordChange, boolean isWeek, String lastModifiedDate) {
		try {
			String UPDATE_QUERY = "UPDATE `secure_item_stats` SET last_password_change=?, is_weak=?, last_modified_date=?,  last_alert=null WHERE secure_item_id=?";
			mDao.executeRaw(UPDATE_QUERY, lastPasswordChange, isWeek? "1" : "0", lastModifiedDate, id);
		} catch (SQLException e) {
		}
	}
	
	public void CheckForDuplicatePasswords() {
		try {
			String dateTimeNow = new DateTime(DateTimeZone.UTC).toDateTimeISO().toString();
			ArrayList<String> queries;
			queries = new ArrayList<>();
			queries.add("update `secure_item_stats` set has_duplicate=0,last_modified_date=? where has_duplicate=1;");
			queries.add("update `secure_item_stats` set has_duplicate=1,last_modified_date=?, "+ 
            " last_alert=case when (julianday('now') - julianday(last_alert))>30 then null end"+ 
            " where secure_item_id in " + 
            "(select sd.secure_item_id from secure_item_data sd join secure_item si on si.id=sd.secure_item_id join " +  
            "(select sd.value from secure_item_data sd join secure_item si on si.id=sd.secure_item_id " + 
            " where si.secure_item_type_name='PV' and sd.identifier='password' and si.active=1 and sd.value is not null and length(sd.value)>0 " +
            " group by sd.value having count(sd.value)>1) dup on sd.value=dup.value)"+ 
            " and has_duplicate=0;");
			for (int i = 0; i < queries.size(); i++) {
				 mDao.executeRaw(queries.get(i), dateTimeNow);
			}
		} catch (Exception e) {
		}
	}
	
	
	public ArrayList<NotificationBean> GetAlertNotifications(Activity mContext) {
		ArrayList<NotificationBean> listNotification = new ArrayList<>();
		try {
			String select_query = "select *,'share' as item_type from share order by last_modified_date asc;";
			
			GenericRawResults<String[]> rawResults = mDao.queryRaw(select_query);
			ArrayList<String[]> mList = (ArrayList<String[]>) rawResults.getResults();
			if (mList.size() > 0) {
				 for (String[] s : mList) {
					 String message = s[2];
					 if (Pref.EMAIL.equals(s[6])) {			
						 // SENDER
						 if (DatabaseConstants.REJECTED.equals(s[3])) {
							 NotificationBean nb = new NotificationBean(s[7],s[15], s[10],s[11], s[17], s[5], message, s[0]);
							 nb.setTitle(mContext.getResources().getString(R.string.SharingNotification_Rejected));
							 nb.setSubtitle(s[5]);
							 nb.setDescription(s[16]);
							 listNotification.add(nb); 
						 } else if (DatabaseConstants.EXPIRED.equals(s[3])) {
							 NotificationBean nb = new NotificationBean(s[7],s[15], s[10],s[11], s[17], s[5], message, s[0]);
							 nb.setTitle(mContext.getResources().getString(R.string.SharingNotification_Expired));
							 nb.setSubtitle(s[5]);
							 nb.setDescription(s[16]);
							 listNotification.add(nb); 
						 } else if (DatabaseConstants.SHARED.equals(s[3])) {
							 NotificationBean nb = new NotificationBean(s[7],s[15], s[10],s[11], s[17], s[5], message, s[0]);
							 nb.setTitle(mContext.getResources().getString(R.string.SharingNotification_Shared));
							 nb.setSubtitle(s[5]);
							 nb.setDescription(s[16]);
							 listNotification.add(nb); 
						 }
					 } else {
//						 RECIVER
						 if (DatabaseConstants.PENDING.equals(s[3])) {
							 NotificationBean nb = new NotificationBean(s[7],s[15], s[10],s[11], s[17], s[5], message, s[0]);
							 nb.setTitle(mContext.getResources().getString(R.string.SharingNotification_Pending));
							 nb.setSubtitle(s[5]);
							 nb.setDescription(s[16]);
							 listNotification.add(nb); 
						 } else if (DatabaseConstants.EXPIRED.equals(s[3])) {
							 NotificationBean nb = new NotificationBean(s[7],s[15], s[10],s[11], s[17], s[5], message, s[0]);
							 nb.setTitle(mContext.getResources().getString(R.string.SharingNotification_Expired));
							 nb.setSubtitle(s[5]);
							 nb.setDescription(s[16]);
							 listNotification.add(nb);
						 }
					 }
				 }
			}
			
			String select_query2 = "select st.*,si.last_access,si.access_count,'sec_alert' as item_type," +  
                    "s.id as site_id,s.name as site_name,s.id as image_id from secure_item_stats st " + 
                    " join secure_item si on st.secure_item_id=si.id join site s on s.id=si.site_id " + 
                    " where st.do_not_show=0 and si.active=1 and ((julianday('now') - julianday(st.last_password_change))>=365 or st.has_duplicate=1 or st.is_weak=1)" +
                    " order by st.last_alert asc,si.access_count desc,si.last_access desc;";
			GenericRawResults<String[]> rawResults2 = mDao.queryRaw(select_query2);
			ArrayList<String[]> mList2 = (ArrayList<String[]>) rawResults2.getResults();
			if (mList2.size() > 0) {
				 for (String[] s : mList2) {
					 String imgUrl = Constants.DIR_IMAGES_FAVICON_LIST + File.separator +  s[13] + ".png";
					 boolean isWeak = s[3].equals("1");
					 boolean hasDouble = s[4].equals("1");
					 NotificationBean nb = new NotificationBean(s[0], s[1], s[2], isWeak, hasDouble, 
							 s[5], s[6], s[7], s[8], s[9], s[10], s[11], s[12], s[13], imgUrl);
					 listNotification.add(nb);
				 }
			}
		} catch (Exception e) {
		}
		return listNotification;
	}


	public void AlertNotificationSeen(String type, String id) {
		try {
			String dateTimeNow = new DateTime(DateTimeZone.UTC).toDateTimeISO().toString();
			if (Constants.SECURE_ITEM_NOTIFICATION_SHARE.equals(type)) {
				String query_share = "update share set last_alert=?, last_modified_date=? where id=?;";
				mDao.executeRaw(query_share, dateTimeNow, dateTimeNow, id);
			} else if (Constants.SECURE_ITEM_NOTIFICATION_SEC_ALERT.equals(type)) {
				String query_alert = "update secure_item_stats set last_alert=?, last_modified_date=? where secure_item_id=?;";  
				mDao.executeRaw(query_alert, dateTimeNow, dateTimeNow, id);
			}
		} catch (Exception e) {
		}
	}
}
