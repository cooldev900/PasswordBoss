package com.passwordboss.android.database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.google.common.base.Strings;
import com.j256.ormlite.android.cipher.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.passwordboss.android.activity.UnlockActivity;
import com.passwordboss.android.app.App;
import com.passwordboss.android.database.beans.AlertMessages;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.beans.Favorite;
import com.passwordboss.android.database.beans.Feature;
import com.passwordboss.android.database.beans.FeatureGroup;
import com.passwordboss.android.database.beans.FeatureGroupFeature;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.database.beans.SecureItemStats;
import com.passwordboss.android.database.beans.SecureItemType;
import com.passwordboss.android.database.beans.Settings;
import com.passwordboss.android.database.beans.Share;
import com.passwordboss.android.database.beans.Site;
import com.passwordboss.android.database.beans.SiteImage;
import com.passwordboss.android.database.beans.SiteImageSize;
import com.passwordboss.android.database.beans.SiteUri;
import com.passwordboss.android.database.beans.StorageRegion;
import com.passwordboss.android.database.beans.UserIdentity;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.beans.UserSubscription;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

import net.sqlcipher.database.SQLiteDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class DatabaseHelperSecure extends OrmLiteSqliteOpenHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelperSecure.class);
    private static int DB_VERSION = 17; // would be same as highest version of script at assets/dbSecure/Store_v*
    private static DatabaseHelperSecure sInstance = null;
    private static BehaviorSubject<DatabaseHelperSecure> sBehaviorSubject;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private DatabaseHelperSecure(Context context, String password) {
        super(context, DatabaseConstants.getSecuredDBPath(), null, DB_VERSION, password);
        mContext = context;
        File file = new File(DatabaseConstants.getSecuredDBPath());
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }
        mDatabase = SQLiteDatabase.openOrCreateDatabase(file, password, null);
    }

    public static void checkVersion(SQLiteDatabase database, Context context, int dbVersion) {
        if (database == null || !database.isOpen()) return;
        ArrayList<String> scripts = Utils.listAssetFiles("dbSecure", context);
        if (null == scripts) return;
        for (String script : scripts) {
            if (!script.startsWith("Store_v")) continue;
            String temp = script.replace(".txt", "");
            int dbVer = Integer.parseInt(temp.substring(7));
            if (dbVer <= dbVersion) continue;
            String sqlScript = Utils.ReadFromfile("dbSecure/" + script, context);
            String tables[] = sqlScript.split(";");
            for (String table : tables) {
                try {
                    database.execSQL(table);
                } catch (Exception e) {
                    LOGGER.error("Error during checking version:", e);
                }
            }
        }
    }

    /**
     * Starts LockActivity when password is empty.
     *
     * @return null if password is empty or null
     */
    @Nullable
    @Beta
    public static synchronized DatabaseHelperSecure getHelper(Context context, String password) {
        if (sInstance == null) {
            if (Strings.isNullOrEmpty(password)) {
                Intent intent = new Intent(context, UnlockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return null;
            }
            sInstance = new DatabaseHelperSecure(context.getApplicationContext(), password);
            if (null != sBehaviorSubject) {
                sBehaviorSubject.onNext(sInstance);
            }
        }
        return sInstance;
    }

    public static Observable<DatabaseHelperSecure> getObservable() {
        if (null == sBehaviorSubject) {
            sBehaviorSubject = BehaviorSubject.create(getHelper(App.get(), Pref.DATABASE_KEY));
        }
        return sBehaviorSubject
                .filter(db -> null != db) // not null
                .first(); // to call onComplete after first item
    }

    public static boolean instanceExists() {
        return null != sInstance;
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (mDatabase == null) throw new IllegalStateException("Database is null");
        mDatabase.rawExecSQL("PRAGMA key = '" + Pref.generatePassword(Pref.EMAIL, oldPassword) + "';");
        mDatabase.rawExecSQL("PRAGMA rekey = '" + Pref.generatePassword(Pref.EMAIL, newPassword) + "';");
        sInstance.close();
    }

    @Override
    public synchronized void close() {
        mDatabase.close();
        mDatabase = null;
        super.close();
        sInstance = null;
        mContext = null;
        if (sBehaviorSubject != null) {
            sBehaviorSubject.onNext(null);
        }
    }

    public Dao<AlertMessages, String> getAlertMessagesDao() throws SQLException {
        return getDao(AlertMessages.class);
    }

    public Dao<Folder, String> getCategoryDao() throws SQLException {
        return getDao(Folder.class);
    }

    public Dao<Device, String> getDeviceDao() throws SQLException {
        return getDao(Device.class);
    }

    public Dao<Favorite, String> getFavoriteDao() throws SQLException {
        return getDao(Favorite.class);
    }

    public Dao<Feature, String> getFeatureDao() throws SQLException {
        return getDao(Feature.class);
    }

    public Dao<FeatureGroup, String> getFeatureGroupDao() throws SQLException {
        return getDao(FeatureGroup.class);
    }

    public Dao<FeatureGroupFeature, String> getFeatureGroupFeatureDao() throws SQLException {
        return getDao(FeatureGroupFeature.class);
    }

    public Dao<SecureItem, String> getSecureItemDao() throws SQLException {
        return getDao(SecureItem.class);
    }

    public Dao<SecureItemData, Void> getSecureItemDataDao() throws SQLException {
        return getDao(SecureItemData.class);
    }

    public Dao<SecureItemStats, String> getSecureItemStatsDao() throws SQLException {
        return getDao(SecureItemStats.class);
    }

    public Dao<SecureItemType, String> getSecureItemTypeDao() throws SQLException {
        return getDao(SecureItemType.class);
    }

    public Dao<Settings, String> getSettingsDao() throws SQLException {
        return getDao(Settings.class);
    }

    public Dao<Share, String> getShareDao() throws SQLException {
        return getDao(Share.class);
    }

    public Dao<Site, String> getSiteDao() throws SQLException {
        return getDao(Site.class);
    }

    public Dao<SiteImage, String> getSiteImageDao() throws SQLException {
        return getDao(SiteImage.class);
    }

    public Dao<SiteImageSize, String> getSiteImageSizeDao() throws SQLException {
        return getDao(SiteImageSize.class);
    }

    public Dao<SiteUri, String> getSiteUriDao() throws SQLException {
        return getDao(SiteUri.class);
    }

    public Dao<StorageRegion, String> getStorageRegionDao() throws SQLException {
        return getDao(StorageRegion.class);
    }

    public Dao<UserIdentity, String> getUserIdentityDao() throws SQLException {
        return getDao(UserIdentity.class);
    }

    public Dao<UserInfo, String> getUserInfoDao() throws SQLException {
        return getDao(UserInfo.class);
    }

    public Dao<UserSubscription, String> getUserSubscriptionDao() throws SQLException {
        return getDao(UserSubscription.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        if (!database.isOpen()) return;
        ArrayList<String> scripts = Utils.listAssetFiles("dbSecure", mContext);
        if (null == scripts) return;
        for (String script : scripts) {
            String sqlScript = Utils.ReadFromfile("dbSecure/" + script, mContext);
            String tables[] = sqlScript.split(";");
            for (String table : tables) {
                database.execSQL(table);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        checkVersion(database, mContext, oldVersion);
    }

}
