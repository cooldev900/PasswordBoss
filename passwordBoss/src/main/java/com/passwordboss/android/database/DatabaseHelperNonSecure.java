package com.passwordboss.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.database.beans.Action;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.Currency;
import com.passwordboss.android.database.beans.Event;
import com.passwordboss.android.database.beans.EventDefinition;
import com.passwordboss.android.database.beans.Language;
import com.passwordboss.android.database.beans.MessageData;
import com.passwordboss.android.database.beans.MessageDefinitions;
import com.passwordboss.android.database.beans.MessageHistory;
import com.passwordboss.android.database.beans.MessageTranslations;
import com.passwordboss.android.database.beans.SiteDomain;
import com.passwordboss.android.utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseHelperNonSecure extends OrmLiteSqliteOpenHelper {

	public static SQLiteDatabase mDatabase = null;

	public static DatabaseHelperNonSecure databaseHelperNonSecure = null;
	private Context mContext = null;
	private static String databasePath =
			Constants.APP_DATABASE + File.separator +
			DatabaseConstants.NON_SECURED_DB_NAME;

	private static final AtomicInteger usageCounter = new AtomicInteger(0);
    private static int DB_VERSION = 6; // would be same as highest version of script at assets/dbController/Controller_v2*

	public DatabaseHelperNonSecure(Context context) {
		super(context, databasePath, null, DB_VERSION);
		mContext = context;
		openDataBase();
	}

    public static boolean exists() {
        return new File(databasePath).exists();
    }

    @Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		if (!database.isOpen()) return;
		ArrayList<String> scripts = Utils.listAssetFiles("dbController", mContext);
		if(null == scripts)return;
		for (String script : scripts) {
            String sqlScript = Utils.ReadFromfile("dbController/" + script, mContext);
			String tables[] = sqlScript.split(";");
			for (String table : tables) {
                database.execSQL(table);
            }
		}
	}

	public static void checkVersion(SQLiteDatabase database, Context context, int dbVersion) {
		if (database == null || !database.isOpen()) return;
		ArrayList<String> scripts = Utils.listAssetFiles("dbController", context);
		if(null == scripts)return;
		for (String script : scripts) {
            if (script.startsWith("Controller_v")) {
                String temp = script.replace(".txt", "");
                int dbVer = Integer.parseInt(temp.substring(12));
                if (dbVer > dbVersion) {
                    String sqlScript = Utils.ReadFromfile("dbController/" + script, context);
					String tables[] = sqlScript.split(";");
					for (String table : tables) {
                        database.execSQL(table);
                    }
				}
            }
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int arg2, int arg3) {
		checkVersion(database, mContext, arg2);
	}

	/**
	 * Get the helper, possibly constructing it if necessary. For each call to
	 * this method, there should be 1 and only 1 call to {@link #close()}.
	 */
	public static synchronized DatabaseHelperNonSecure getHelper(Context context) {
		if (databaseHelperNonSecure == null) {
			databaseHelperNonSecure = new DatabaseHelperNonSecure(context.getApplicationContext());
		}
		usageCounter.incrementAndGet();
		return databaseHelperNonSecure;
	}

	/*
	 * Open database
	 */
	public SQLiteDatabase openDataBase() {
		if (mDatabase == null) {
				File mFile = new File(Constants.APP_DATABASE);
				File mFileDatabase = new File(databasePath);
				if (!mFile.exists()) {
					//noinspection ResultOfMethodCallIgnored
					mFile.mkdir();
				}
				if (!mFileDatabase.exists()) {
					getReadableDatabase();
				}
				mDatabase =	SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}
		return mDatabase;
	}

    public Dao<Action, Integer> getActionDao() throws SQLException {
		return getDao(Action.class);
	}

	public Dao<Configuration, Integer> getConfigurationDao() throws SQLException {
		return getDao(Configuration.class);
	}

	public Dao<Country, Integer> getCountryDao() throws SQLException {
		return getDao(Country.class);
	}

	public Dao<Language, Integer> getLanguageDao() throws SQLException {
		return getDao(Language.class);
	}

	public Dao<MessageDefinitions, Integer> getMessageDefinitionsDao() throws SQLException {
		return getDao(MessageDefinitions.class);
	}

	public Dao<MessageTranslations, Integer> getMessageTranslationsDao() throws SQLException {
		return getDao(MessageTranslations.class);
	}
	public Dao<MessageData, Integer> getMessageDataDao() throws SQLException {
		return getDao(MessageData.class);
	}
	public Dao<Event, Integer> getEventDao() throws SQLException {
		return getDao(Event.class);
	}
	public Dao<EventDefinition, Integer> getEventDefinitionDao() throws SQLException {
		return getDao(EventDefinition.class);
	}
	public Dao<SiteDomain, Integer> getSiteDomainDao() throws SQLException {
		return getDao(SiteDomain.class);
	}
	public Dao<Currency, Integer> getCurrencyDao() throws SQLException {
		return getDao(Currency.class);
	}

	public Dao<MessageHistory, Integer> getMessageHistoryDao() throws SQLException {
		return getDao(MessageHistory.class);
	}

	@Override
	public void close() {
		if (usageCounter.decrementAndGet() == 0) {
			super.close();
			if(mDatabase != null) {
				mDatabase.close();
				mDatabase = null;
			}
			databaseHelperNonSecure = null;
		}
	}
}
