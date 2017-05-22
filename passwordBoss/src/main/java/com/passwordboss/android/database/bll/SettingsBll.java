package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Settings;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.SQLException;
import java.util.List;


public class SettingsBll extends BaseBll<Settings, String> {

	public SettingsBll(DatabaseHelperSecure helperSecure) throws SQLException {
		super(helperSecure.getSettingsDao());
	}
	
	public Settings getSettingsByKey(String key) {
		try {
			QueryBuilder<Settings, String> queryBuilder = mDao.queryBuilder();
			Where<Settings, String> where = queryBuilder.where();
			where.eq(DatabaseConstants.KEY, key).and().eq(DatabaseConstants.ACTIVE, true);
			PreparedQuery<Settings> preparedQuery = queryBuilder.prepare();

			List<Settings> mSettingsList = mDao.query(preparedQuery);
			if (mSettingsList.size() > 0) {
				return mSettingsList.get(0);
			} 
		} catch (SQLException e) {
			////Log.print(e);
		}
		return null;
	}	
	
	public void insertOrUpdate(String key, String value) {
		try {
			Settings mSettings = this.getSettingsByKey(key);
			
			if (mSettings != null) {
				mSettings.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
				mSettings.setActive(true);
				mSettings.setValue(value);
				updateRow(mSettings);
			} else {
				mSettings = new Settings();
				mSettings.setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
				mSettings.setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
				mSettings.setKey(key);
				mSettings.setValue(value);
				mSettings.setActive(true);
				insertRow(mSettings);
			}
		} catch (Exception e) {
			////Log.print(e);
		}
	}

}