package com.passwordboss.android.database;

import android.content.Context;

import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.constants.HttpConstants;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.beans.Country;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.beans.Feature;
import com.passwordboss.android.database.beans.FeatureGroup;
import com.passwordboss.android.database.beans.Language;
import com.passwordboss.android.database.beans.MessageDefinitions;
import com.passwordboss.android.database.beans.MessageTranslations;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemType;
import com.passwordboss.android.database.beans.Settings;
import com.passwordboss.android.database.beans.Share;
import com.passwordboss.android.database.beans.Site;
import com.passwordboss.android.database.beans.SiteDomain;
import com.passwordboss.android.database.beans.SiteImage;
import com.passwordboss.android.database.beans.SiteImageSize;
import com.passwordboss.android.database.beans.SiteUri;
import com.passwordboss.android.database.beans.StorageRegion;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.beans.UserSubscription;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.CountryBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.database.bll.FeatureBll;
import com.passwordboss.android.database.bll.FeatureGroupBll;
import com.passwordboss.android.database.bll.FeatureGroupFeatureBll;
import com.passwordboss.android.database.bll.FolderBll;
import com.passwordboss.android.database.bll.LanguageBll;
import com.passwordboss.android.database.bll.MessageDefinitionsBll;
import com.passwordboss.android.database.bll.MessageTranslationsBll;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.database.bll.SecureItemTypeBll;
import com.passwordboss.android.database.bll.SettingsBll;
import com.passwordboss.android.database.bll.ShareBll;
import com.passwordboss.android.database.bll.SiteBll;
import com.passwordboss.android.database.bll.SiteDomainBll;
import com.passwordboss.android.database.bll.SiteImageBll;
import com.passwordboss.android.database.bll.SiteImageSizeBll;
import com.passwordboss.android.database.bll.SiteUriBll;
import com.passwordboss.android.database.bll.StorageRegionBll;
import com.passwordboss.android.database.bll.UserInfoBll;
import com.passwordboss.android.database.bll.UserSubscriptionBll;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.beans.AccountHttpBean;
import com.passwordboss.android.http.beans.BaseHttpBean;
import com.passwordboss.android.http.beans.CountryHttpBean;
import com.passwordboss.android.http.beans.DeviceHttpBean;
import com.passwordboss.android.http.beans.FeaturesHttpBean;
import com.passwordboss.android.http.beans.FileHttpBean;
import com.passwordboss.android.http.beans.LanguageHttpBean;
import com.passwordboss.android.http.beans.MessageDefinitionsHttpBean;
import com.passwordboss.android.http.beans.RecommendedSiteHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.http.beans.SharesHttpBean;
import com.passwordboss.android.http.beans.SiteDomainHttpBean;
import com.passwordboss.android.task.SyncTask;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.utils.Utils;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.http.util.ByteArrayBuffer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class DatabaseSync {
    public int progress = 0;
    private DatabaseHelperNonSecure dbNonSecure;
    private Context mContext;

    public DatabaseSync(Context context) {
        try {
            mContext = context;
            dbNonSecure = DatabaseHelperNonSecure.getHelper(context);
        } catch (Exception e) {
        }
    }


    public static int attachDatabase(Context context, String dbPath, String dbPass, String installationId) {
        int result = SyncTask.DB_STATUS_TO_UPLOAD;
        SQLiteDatabase db = null;
        SQLiteDatabase db2 = null;
        ArrayList<String> command = new ArrayList<>();
        command.add("ATTACH DATABASE '" + dbPath + "' AS merge KEY '" + dbPass + "';");
        command.add("insert or replace into storage_region select m.* from merge.storage_region m left join storage_region loc on m.uuid=loc.uuid where m.last_modified_date>loc.last_modified_date or loc.uuid is null;");
        command.add("insert or replace into feature select m.* from merge.feature m left join feature loc on m.uuid=loc.uuid where m.last_modified_date>loc.last_modified_date or loc.uuid is null;");
        command.add("insert or replace into feature_group select m.* from merge.feature_group m left join feature_group loc on m.uuid=loc.uuid where m.last_modified_date>loc.last_modified_date or loc.uuid is null;");
        command.add("insert or replace into feature_group_feature select m.* from merge.feature_group_feature m left join feature_group_feature loc on m.feature_uuid=loc.feature_uuid and m.feature_group_uuid=loc.feature_group_uuid where m.last_modified_date>loc.last_modified_date or loc.feature_group_uuid is null;");
        command.add("insert into user_info select m.* from merge.user_info m left join user_info loc on m.email=loc.email where loc.email is null and m.email is not null;");
        command.add("insert or replace into user_info select m.* from merge.user_info m join user_info loc on m.email=loc.email where m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date and m.email is not null;");
        command.add("insert or replace into device select m.* from merge.device m left join device loc on m.installation_id=loc.installation_id where m.installation_id<>'" + installationId + "' and (m.last_modified_date>loc.last_modified_date or loc.installation_id is null);");
        command.add("insert or replace into favorite select m.* from merge.favorite m left join favorite loc on m.url=loc.url where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.url is null;");
        command.add("insert or replace into secure_item_type select m.* from merge.secure_item_type m left join secure_item_type loc on m.name=loc.name where m.last_modified_date>loc.last_modified_date or loc.name is null;");
        command.add("insert or replace into folder select m.* from merge.folder m left join folder loc on m.id=loc.id where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.id is null;");
        command.add("insert or replace into folder_share select m.* from merge.folder_share m left join folder_share loc on m.id=loc.id where (m.hash<>loc.hash) or loc.id is null;");
        command.add("insert or replace into site select m.* from merge.site m left join site loc on m.id=loc.id where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.id is null;");
        command.add("insert or replace into site_image_size select m.* from merge.site_image_size m left join site_image_size loc on m.id=loc.id where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.id is null;");
        command.add("insert or replace into site_image select m.* from merge.site_image m left join site_image loc on m.id=loc.id where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.id is null;");
        command.add("insert or replace into site_uri select m.* from merge.site_uri m left join site_uri loc on m.uri=loc.uri and m.site_id=loc.site_id and m.type=loc.type where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.uri is null;");
        command.add("insert or replace into secure_item select m.* from merge.secure_item m left join secure_item loc on m.id=loc.id where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.id is null;");
        command.add("insert or replace into secure_item_data select m.* from merge.secure_item_data m left join secure_item_data loc on m.secure_item_id=loc.secure_item_id and m.identifier=loc.identifier where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.secure_item_id is null;");
        command.add("insert or replace into secure_item_share select m.* from merge.secure_item_share m left join secure_item_share loc on m.id=loc.id where (m.hash<>loc.hash);");
        command.add("insert or replace into secure_item_stats select m.* from merge.secure_item_stats m left join secure_item_stats loc on m.secure_item_id=loc.secure_item_id where m.last_modified_date>loc.last_modified_date or loc.secure_item_id is null;");
        command.add("insert or replace into share select m.* from merge.share m left join share loc on m.id=loc.id where m.last_modified_date>loc.last_modified_date or loc.id is null;");
        command.add("insert or replace into alert_messages select m.* from merge.alert_messages m left join alert_messages loc on m.uuid=loc.uuid where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.uuid is null;");
        command.add("insert or replace into user_identity select m.* from merge.user_identity m left join user_identity loc on m.id=loc.id where (m.hash<>loc.hash and m.last_modified_date>=loc.last_modified_date) or loc.id is null;");
        command.add("DETACH DATABASE merge;");

        try {
            File file = new File(DatabaseConstants.getSecuredDBPath());
            File file2 = new File(dbPath);
            if (file.exists()) {
                try {
                    db = SQLiteDatabase.openDatabase(
                            DatabaseConstants.getSecuredDBPath(), Pref.DATABASE_KEY, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
                } catch (Exception e) {
                    db = SQLiteDatabase.openDatabase(
                            DatabaseConstants.getSecuredDBPath(), Pref.DATABASE_KEY, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
                }
                if (file2.exists()) {
                    db2 = SQLiteDatabase.openDatabase(dbPath, dbPass, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
                }
                if (db2 != null) {
                    if (db2.getVersion() != db.getVersion()) {
                        if (db2.getVersion() > db.getVersion()) {
                            db.close();
                            db2.close();
                            return SyncTask.DB_STATUS_VERSION_ERROR;
                        } else {
                            DatabaseHelperSecure.checkVersion(db2, context, db2.getVersion());
                        }
                    }
                }
                while (command.size() > 0) {
                    if (!db.isDbLockedByCurrentThread()) {
                        db.execSQL(command.get(0));
                        command.remove(0);
                    }
                }
                db.close();
                if (db2 != null) {
                    db2.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (db != null) {
                db.close();
            }
            if (db2 != null) {
                db2.close();
            }
            result = SyncTask.DB_STATUS_NOTHING;
        }

        return result;
    }

    public static String calculateHash(String dbPath, String dbPass) {
        String hash = "";

        HashMap<String, String> tableHash = new HashMap<String, String>();
        ArrayList<String> hashs = new ArrayList<>();

        String sql[] = {
                "select hash from user_info order by hash asc;",
                "select hash from favorite order by hash asc;",
                "select hash from folder where uuid is null or uuid='' order by hash asc;",
                "select hash from site where uuid is null or uuid='' order by hash asc;",
                "select hash from secure_item order by hash asc;",
                "select hash from secure_item_data order by hash asc;"
        };

        String tables[] = {
                "user_info", "favorite", "folder", "site",
                "secure_item", "secure_item_data"
        };

        Cursor cursor = null;
        SQLiteDatabase db = null;

        try {
            try {
                db = SQLiteDatabase.openOrCreateDatabase(dbPath, dbPass, null);
            } catch (Exception ex) {
            }

            if (db.isOpen()) {
                for (int i = 0, max = sql.length; i < max; i++) {
                    cursor = db.rawQuery(sql[i], null);
                    String rawHash = "";
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            rawHash += cursor.getString(cursor.getColumnIndex("hash"));
                        } while (cursor.moveToNext());
                    }
                    String calcHash = Utils.getMD5(rawHash);
                    tableHash.put(tables[i], calcHash);
                    hashs.add(calcHash);
                    cursor.close();
                }
            }

            Collections.sort(hashs);
            String rawHash = "";
            for (int i = 0, max = hashs.size(); i < max; i++) {
                rawHash += hashs.get(i);
            }
            hash = Utils.getMD5(rawHash);
            if (db != null) {
                db.close();
            }
        } catch (Exception ex) {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return hash;
    }

    public int DownloadDatabase(String uri, String databaseName, boolean attach, String chacksum) {
        int bufferSize;
        int result = SyncTask.DB_STATUS_NOTHING;
        try {
            URL url = new URL(uri);
            URLConnection uconn = url.openConnection();
            InputStream is = uconn.getInputStream();
            bufferSize = uconn.getContentLength();
            BufferedInputStream bufferinstream = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(bufferSize);

            FileOutputStream fos = new FileOutputStream(new File(databaseName));
            int current;
            while ((current = bufferinstream.read()) != -1) {
                baf.append((byte) current);
            }

            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            File deleteDownloadFile = new File(DatabaseConstants.getSecuredDBPathBackUp());
            if (attach) {
                if (Utils.calculateMD5(deleteDownloadFile).equals(chacksum)) {
                    result = attachDatabaseDownloadDB();
                } else {
                    result = SyncTask.DB_STATUS_SKIP_UPLOAD;
                    if (deleteDownloadFile.exists()) {
                        deleteDownloadFile.delete();
                    }
                }
            } else {
                result = SyncTask.DB_STATUS_SKIP_UPLOAD;
                if (deleteDownloadFile.exists()) {
                    deleteDownloadFile.delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void GetSiteFromServer() {
        try {
            DatabaseHelperSecure databaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            SiteUriBll mSiteUriBll = new SiteUriBll(databaseHelperSecure);
            SecureItemBll mSecureItemBll = new SecureItemBll(databaseHelperSecure);
            SiteBll mSiteBll = new SiteBll(databaseHelperSecure);

            if (Utils.isOnline(mContext)) {
                ServerAPI api = new ServerAPI();
                List<SiteUri> mListSiteUri = mSiteUriBll.getAllRecordsWhereUuuidIsNull();
                DatabaseSync dbSync = new DatabaseSync(mContext);
                for (int i = 0, max = mListSiteUri.size(); i < max; i++) {
                    try {
                        ServerResponseHttpBean mServerResponseHttpBean =
                                api.getSiteEndpointGet(mListSiteUri.get(i).getUri());
                        if (mServerResponseHttpBean.getSites().length > 0) {
                            mSiteUriBll.deleteSiteUriByUriAndSiteId(mListSiteUri.get(i).getUri(), mListSiteUri.get(i).getSiteId().getId());
                            Site mSite = mSiteBll.getSiteById(mListSiteUri.get(i).getSiteId().getId());
                            Site mSiteFromServer = mSiteBll.getSiteByUuid(mServerResponseHttpBean.getSites()[0].getUuid());
                            List<SecureItem> secureItems = mSecureItemBll.getSecureItemBySiteId(
                                    mListSiteUri.get(i).getSiteId().getId());
                            if (mSiteFromServer == null) {
                                if (mSite != null) {
                                    File mListImage = new File(
                                            Constants.DIR_IMAGES_FAVICON_LIST + File.separator + mSite.getId() + ".png");

                                    if (mListImage.exists()) {
                                        mListImage.delete();
                                    }

                                    mSiteBll.deleteBySiteId(mSite.getId());
                                }

                                dbSync.SyncSitesAndRecommendedSites(mServerResponseHttpBean.getSites(), false);
                                SiteBll siteBll = new SiteBll(databaseHelperSecure);
                                for (int j = 0; j < secureItems.size(); j++) {
                                    SecureItem mSecureItem = secureItems.get(j);
                                    Site site = siteBll.getSiteById(mServerResponseHttpBean.getSites()[0].getUuid());
                                    mSecureItem.setSite(site);
                                    mSecureItemBll.updateRow(mSecureItem);
                                }
                            } else {
                                if (mSite != null) {
                                    mSiteBll.deleteBySiteId(mSite.getId());
                                    File mListImage = new File(
                                            Constants.DIR_IMAGES_FAVICON_LIST + File.separator + mSite.getId() + ".png");
                                    if (mListImage.exists()) {
                                        mListImage.delete();
                                    }
                                }
                                if (secureItems.size() > 0) {
                                    for (int j = 0, len = secureItems.size(); j < len; j++) {
                                        SecureItem mSecureItem = secureItems.get(j);
                                        mSecureItem.setSite(mSiteFromServer);
                                        mSecureItemBll.updateRow(mSecureItem);
                                    }
                                }
                                dbSync.SyncSitesAndRecommendedSites(mServerResponseHttpBean.getSites(), false);
                            }
                        }
                    } catch (Exception ex) {
                        Pref.setValue(mContext, Constants.LATEST_SYNC, false);
                    }
                }
            }
        } catch (Exception e) {
            Pref.setValue(mContext, Constants.LATEST_SYNC, false);
        }
    }

    public boolean SyncCountries(CountryHttpBean[] countries) {
        boolean result = true;
        try {
            CountryBll countryBll = new CountryBll(dbNonSecure);
            LanguageBll languageBll = new LanguageBll(dbNonSecure);
            List<Country> listItems = countryBll.getAllRecords();
            HashMap<String, Country> mapCountries = new HashMap<>();

            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapCountries.put(listItems.get(i).getCode(), listItems.get(i));
            }
            listItems.clear();

            if (countries != null && countries.length > 0) {
                for (int i = 0, max = countries.length; i < max; i++) {
                    Country c = mapCountries.get(countries[i].getCode());
                    if (c == null) {
                        Country newCountry = new Country();
                        newCountry.setActive(true);
                        newCountry.setOrder(countries[i].getOrder());
                        newCountry.setCode(countries[i].getCode());
                        newCountry.setCreatedDate(new DateTime(countries[i].getCreatedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newCountry.setLastModifiedDate(new DateTime(countries[i]
                                .getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newCountry.setCurrency(countries[i].getCurrency().getName());
                        newCountry.setLanguageCode(languageBll
                                .getLanguagesByUuid(
                                        countries[i].getLanguage().getUuid()));

                        newCountry.setName(countries[i].getName());
                        newCountry.setUuid(countries[i].getUuid());
                        countryBll.insertRow(newCountry);
                        mapCountries.put(newCountry.getCode(), newCountry);
                    } else if ((new DateTime(c.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                            countries[i].getLastModifiedDate().getMillis()) {
                        c.setActive(true);
                        c.setOrder(countries[i].getOrder());
                        c.setCode(countries[i].getCode());
                        c.setLastModifiedDate(countries[i].getLastModifiedDate()
                                .toDateTimeISO().toString());
                        c.setCurrency(countries[i].getCurrency().getName());
                        c.setLanguageCode(languageBll
                                .getLanguagesByUuid(
                                        countries[i].getLanguage().getUuid()));
                        c.setName(countries[i].getName());
                        c.setUuid(countries[i].getUuid());
                        countryBll.updateRow(c);
                    }
                }
            }
            mapCountries.clear();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean SyncDevices(DeviceHttpBean[] devices, DateTime latestSync) {
        boolean result = true;
        try {
            DeviceBll deviceBll = new DeviceBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<Device> listItems = deviceBll.getAllRecords();
            HashMap<String, Device> mapDevices = new HashMap<>();
            HashMap<String, DeviceHttpBean> mapDevicesSync = new HashMap<>();
            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapDevices.put(listItems.get(i).getInstallationId(), listItems.get(i));
            }

            listItems.clear();

            for (int i = 0, max = devices.length; i < max; i++) {
                DeviceHttpBean deviceHttpBean = null;
                if (devices[i].getInstallation() != null) {
                    deviceHttpBean = mapDevicesSync.get(devices[i].getInstallation().getUuid());
                    if (deviceHttpBean == null) {
                        mapDevicesSync.put(devices[i].getInstallation().getUuid(), devices[i]);
                    } else {
                        if (devices[i].getLastModifiedDate().getMillis() >
                                deviceHttpBean.getLastModifiedDate().getMillis()) {
                            mapDevicesSync.remove(devices[i].getUuid());
                            mapDevicesSync.put(devices[i].getInstallation().getUuid(), devices[i]);
                        }
                    }
                }
            }

            for (String key : mapDevicesSync.keySet()) {
                Device existDevice = mapDevices.get(key);

                DeviceHttpBean device = mapDevicesSync.get(key);

                boolean isUpdate = true;

                if (existDevice == null) {
                    isUpdate = false;
                    existDevice = new Device();
                }

                existDevice.setActive(device.isActive());
                existDevice.setCreatedDate(new DateTime(device.getCreatedDate(), DateTimeZone.UTC)
                        .toDateTimeISO().toString());
                existDevice.setDeviceCategory(
                        device.getInstallation().getDeviceCategory().getName());
                existDevice.setInstallationId(device.getInstallation().getUuid());
                existDevice.setLastModifiedDate(new DateTime(device.getLastModifiedDate(), DateTimeZone.UTC)
                        .toDateTimeISO().toString());
                if (device.getLastSyncDate() != null) {
                    existDevice.setLatestSync(new DateTime(device.getLastSyncDate(),
                            DateTimeZone.UTC)
                            .toDateTimeISO().toString());
                }
                existDevice.setNickname(device.getNickname());
                existDevice.setOs(device.getInstallation().getOs().getName());
                existDevice.setUuid(device.getUuid());

                if (Pref.INSTALLATION_UUID.equals(existDevice.getInstallationId())) {
                    existDevice.setLatestSync(new DateTime(latestSync, DateTimeZone.UTC)
                            .toDateTimeISO().toString());
                }

                if (isUpdate) {
                    deviceBll.updateRow(existDevice);
                } else {
                    deviceBll.insertRow(existDevice);
                }

            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean SyncFeatures(FeaturesHttpBean features) {
        boolean result = true;
        try {
            FeatureBll featureBll = new FeatureBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            FeatureGroupBll featureGroupBll = new FeatureGroupBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            FeatureGroupFeatureBll featureGroupFeatureBll =
                    new FeatureGroupFeatureBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));

            featureBll.deleteAll();
            featureGroupBll.deleteAll();
            featureGroupFeatureBll.deleteAll();

            FeatureGroup featureGroup = new FeatureGroup();
            featureGroup.setActive(true);
            featureGroup.setCreatedDate(
                    new DateTime(features.getCreatedDate(), DateTimeZone.UTC)
                            .toDateTimeISO().toString());
            featureGroup.setLastModifiedDate(
                    new DateTime(features.getLastModifiedDate(), DateTimeZone.UTC)
                            .toDateTimeISO().toString());
            featureGroup.setName(features.getName());
            featureGroup.setUuid(
                    features.getUuid());
            featureGroupBll.insertOrUpdateRow(featureGroup);
            for (int i = 0, max = features.getFeatures().length; i < max; i++) {
                Feature feature = new Feature();
                feature.setActive(true);
                feature.setCreatedDate(new DateTime(features.getFeatures()[i]
                        .getCreatedDate(), DateTimeZone.UTC)
                        .toDateTimeISO().toString());
                feature.setLastModifiedDate(new DateTime(features.getFeatures()[i]
                        .getLastModifiedDate(), DateTimeZone.UTC)
                        .toDateTimeISO().toString());
                feature.setIdentifier(features.getFeatures()[i].getIdentifier());
                feature.setName(features.getFeatures()[i].getName());
                feature.setUuid(features.getFeatures()[i].getUuid());
                featureBll.insertOrUpdateRow(feature);
            }

        } catch (Exception ex) {
            result = false;
        }

        return result;
    }

    public boolean SyncLanguages(LanguageHttpBean[] languages) {
        boolean result = true;
        try {
            LanguageBll languageBll = new LanguageBll(dbNonSecure);
            List<Language> listItems = languageBll.getAllRecords();
            HashMap<String, Language> mapLanguages = new HashMap<>();

            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapLanguages.put(listItems.get(i).getCode(), listItems.get(i));
            }
            listItems.clear();

            if (languages != null && languages.length > 0) {
                for (int i = 0, max = languages.length; i < max; i++) {
                    Language l = mapLanguages.get(languages[i].getCode());
                    if (l == null) {
                        Language newLang = new Language();
                        newLang.setActive(languages[i].isActive());
                        newLang.setOrder(languages[i].getOrder());
                        newLang.setCode(languages[i].getCode());
                        newLang.setCreatedDate(new DateTime(languages[i].getCreatedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newLang.setLastModifiedDate(new DateTime(languages[i]
                                .getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newLang.setName(languages[i].getName());
                        newLang.setTranslated(languages[i].getTranslated());
                        newLang.setTranslatedName(languages[i].getTranslatedName());
                        newLang.setUuid(languages[i].getUuid());
                        languageBll.insertRow(newLang);
                        mapLanguages.put(newLang.getCode(), newLang);
                    } else if ((new DateTime(l.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                            languages[i].getLastModifiedDate().getMillis()) {
                        l.setActive(languages[i].isActive());
                        l.setOrder(languages[i].getOrder());
                        l.setCode(languages[i].getCode());
                        l.setLastModifiedDate(new DateTime(languages[i].getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        l.setName(languages[i].getName());
                        l.setTranslated(languages[i].getTranslated());
                        l.setTranslatedName(languages[i].getTranslatedName());
                        l.setUuid(languages[i].getUuid());
                        languageBll.updateRow(l);
                    }
                }
            }
            mapLanguages.clear();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean SyncMessageDefinitions(MessageDefinitionsHttpBean[] messageDefinition) {
        boolean result = true;
        try {
            MessageDefinitionsBll mMessageDefinitionsBll = new MessageDefinitionsBll(dbNonSecure);
            MessageTranslationsBll mMessageTranslationsBll = new MessageTranslationsBll(dbNonSecure);

            List<MessageDefinitions> listItems = mMessageDefinitionsBll.getAllRecords();
            HashMap<String, MessageDefinitions> mapMessageDefinitions = new HashMap<>();
            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapMessageDefinitions.put(listItems.get(i).getMsgId(), listItems.get(i));
            }
            listItems.clear();

            List<MessageTranslations> listItemsTranslations = mMessageTranslationsBll.getAllRecords();
            HashMap<String, MessageTranslations> mapMessageTranslations = new HashMap<>();
            for (int i = 0, max = listItemsTranslations.size(); i < max; i++) {
                mapMessageTranslations.put(listItemsTranslations.get(i).getMsgId().getMsgId(), listItemsTranslations.get(i));
            }
            listItemsTranslations.clear();

            if (messageDefinition != null && messageDefinition.length > 0) {
                for (int i = 0, max = messageDefinition.length; i < max; i++) {
                    MessageDefinitions mMessageDefinitions = mapMessageDefinitions.get(messageDefinition[i].getIdentifier());

                    if (mMessageDefinitions == null) {
                        mMessageDefinitions = new MessageDefinitions();
                        mMessageDefinitions.setMsgId(messageDefinition[i].getIdentifier());
                        if (messageDefinition[i].getType() != null) {
                            mMessageDefinitions.setMsgType(messageDefinition[i].getType().getName());
                        }
                        mMessageDefinitions.setTheme(messageDefinition[i].getTemplate());
                        mMessageDefinitions.setIconType(messageDefinition[i].getIconType());
                        if (messageDefinition[i].getConfirmActionId() != null) {
                            mMessageDefinitions.setConfirmAction(messageDefinition[i].getConfirmActionId().getIdentifier());
                        }
                        if (messageDefinition[i].getCancelActionId() != null) {
                            if (messageDefinition[i].getCancelActionId().getIdentifier() != null &&
                                    !messageDefinition[i].getCancelActionId().getIdentifier().equals("")) {
                                mMessageDefinitions.setCancelAction(messageDefinition[i].getCancelActionId().getIdentifier());
                            } else {
                                mMessageDefinitions.setCancelAction(messageDefinition[i].getCancelActionId().getUuid());
                            }
                        }

                        mMessageDefinitions.setShowReminder(messageDefinition[i].isShowReminder());
                        mMessageDefinitions.setAccountStatus(messageDefinition[i].isAccountStatus());
                        mMessageDefinitions.setSubscriptionLevel(messageDefinition[i].getSubscriptionType());
                        if (messageDefinition[i].getTimer() != null) {
                            mMessageDefinitions.setStartTimerOn(messageDefinition[i].getTimer().getName());
                        }
                        if (messageDefinition[i].getInitialDelay() != null) {
                            mMessageDefinitions.setStartDelayMinutes(messageDefinition[i].getInitialDelay().getMinutes());
                        }
                        if (messageDefinition[i].getDisplaySubsequent() != null) {
                            mMessageDefinitions.setRepeatDelayMinutes(messageDefinition[i].getDisplaySubsequent().getMinutes());
                        }
                        if (messageDefinition[i].getSkipShowIfLate() != null) {
                            mMessageDefinitions.setSkipShowIfLateMinutes(messageDefinition[i].getSkipShowIfLate().getMinutes());
                        }
                        mMessageDefinitions.setMaxRepCnt(messageDefinition[i].getMaxDisplayCount());
                        mMessageDefinitions.setShowOnNoEvent(false);
                        mMessageDefinitions.setShowOnNoEventDelayMinutes(0);
                        mMessageDefinitions.setCreatedDate(new DateTime(messageDefinition[i].getCreatedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        mMessageDefinitions.setLastModifiedDate(new DateTime(messageDefinition[i].getLastModifiedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        mMessageDefinitions.setActive(messageDefinition[i].isActive());
                        mMessageDefinitionsBll.insertRow(mMessageDefinitions);
                    } else if ((new DateTime(mMessageDefinitions.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                            messageDefinition[i].getLastModifiedDate().getMillis()) {
                        mMessageDefinitions.setMsgId(messageDefinition[i].getIdentifier());
                        if (messageDefinition[i].getType() != null) {
                            mMessageDefinitions.setMsgType(messageDefinition[i].getType().getName());
                        }
                        mMessageDefinitions.setTheme(messageDefinition[i].getTemplate());
                        mMessageDefinitions.setIconType(messageDefinition[i].getIconType());
                        if (messageDefinition[i].getConfirmActionId() != null) {
                            mMessageDefinitions.setConfirmAction(messageDefinition[i].getConfirmActionId().getIdentifier());
                        }
                        if (messageDefinition[i].getCancelActionId() != null) {
                            if (messageDefinition[i].getCancelActionId().getIdentifier() != null &&
                                    !messageDefinition[i].getCancelActionId().getIdentifier().equals("")) {
                                mMessageDefinitions.setCancelAction(messageDefinition[i].getCancelActionId().getIdentifier());
                            } else {
                                mMessageDefinitions.setCancelAction(messageDefinition[i].getCancelActionId().getUuid());
                            }
                        }

                        mMessageDefinitions.setShowReminder(messageDefinition[i].isShowReminder());
                        mMessageDefinitions.setAccountStatus(messageDefinition[i].isAccountStatus());
                        mMessageDefinitions.setSubscriptionLevel(messageDefinition[i].getSubscriptionType());
                        if (messageDefinition[i].getTimer() != null) {
                            mMessageDefinitions.setStartTimerOn(messageDefinition[i].getTimer().getName());
                        }
                        if (messageDefinition[i].getInitialDelay() != null) {
                            mMessageDefinitions.setStartDelayMinutes(messageDefinition[i].getInitialDelay().getMinutes());
                        }
                        if (messageDefinition[i].getDisplaySubsequent() != null) {
                            mMessageDefinitions.setRepeatDelayMinutes(messageDefinition[i].getDisplaySubsequent().getMinutes());
                        }
                        mMessageDefinitions.setSkipShowIfLateMinutes(messageDefinition[i].getSkipShowIfLate().getMinutes());
                        mMessageDefinitions.setMaxRepCnt(messageDefinition[i].getMaxDisplayCount());
                        mMessageDefinitions.setShowOnNoEvent(false);
                        mMessageDefinitions.setShowOnNoEventDelayMinutes(0);
                        mMessageDefinitions.setCreatedDate(new DateTime(messageDefinition[i].getCreatedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        mMessageDefinitions.setLastModifiedDate(new DateTime(messageDefinition[i].getLastModifiedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        mMessageDefinitions.setActive(messageDefinition[i].isActive());
                        mMessageDefinitionsBll.updateRow(mMessageDefinitions);
                    }


                    MessageTranslations mMessageTranslations = mapMessageTranslations.get(mMessageDefinitions.getMsgId());

                    if (mMessageTranslations == null) {
                        mMessageTranslations = new MessageTranslations();
                        mMessageTranslations.setMsgId(mMessageDefinitions);

                        mMessageTranslations.setLang(HttpConstants.LANGUAGE);
                        mMessageTranslations.setTitle(messageDefinition[i].getTitle1());
                        mMessageTranslations.setTitle1(messageDefinition[i].getTitle2());
                        mMessageTranslations.setTitle2(messageDefinition[i].getTitle3());
                        mMessageTranslations.setBody(messageDefinition[i].getBody1());
                        mMessageTranslations.setBody1(messageDefinition[i].getBody2());
                        mMessageTranslations.setBody2(messageDefinition[i].getBody3());
                        mMessageTranslations.setCreatedDate(new DateTime(messageDefinition[i].getCreatedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        mMessageTranslations.setLastModifiedDate(new DateTime(messageDefinition[i].getCreatedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        if (messageDefinition[i].getCancelActionText() != null) {
                            mMessageTranslations.setCancelActionText(messageDefinition[i].getCancelActionText().getText());
                        }
                        if (messageDefinition[i].getConfirmActionText() != null) {
                            mMessageTranslations.setConfirmActionText(messageDefinition[i].getConfirmActionText().getText());
                        }
                        mMessageTranslationsBll.insertRow(mMessageTranslations);
                    } else if ((new DateTime(mMessageTranslations.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                            messageDefinition[i].getLastModifiedDate().getMillis()) {
                        mMessageTranslations.setMsgId(mMessageDefinitions);
                        mMessageTranslations.setLang(HttpConstants.LANGUAGE);
                        mMessageTranslations.setTitle(messageDefinition[i].getTitle1());
                        mMessageTranslations.setTitle1(messageDefinition[i].getTitle2());
                        mMessageTranslations.setTitle2(messageDefinition[i].getTitle3());
                        mMessageTranslations.setBody(messageDefinition[i].getBody1());
                        mMessageTranslations.setBody1(messageDefinition[i].getBody2());
                        mMessageTranslations.setBody2(messageDefinition[i].getBody3());
                        mMessageTranslations.setCreatedDate(new DateTime(messageDefinition[i].getCreatedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        mMessageTranslations.setLastModifiedDate(new DateTime(messageDefinition[i].getCreatedDate(),
                                DateTimeZone.UTC).toDateTimeISO().toString());
                        if (messageDefinition[i].getCancelActionText() != null) {
                            mMessageTranslations.setCancelActionText(messageDefinition[i].getCancelActionText().getText());
                        }
                        if (messageDefinition[i].getConfirmActionText() != null) {
                            mMessageTranslations.setConfirmActionText(messageDefinition[i].getConfirmActionText().getText());
                        }
                        mMessageTranslationsBll.updateObject(mMessageTranslations);
                    }
                }
            }


        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean SyncSecureItemTypes(BaseHttpBean[] secureItemTypes) {
        boolean result = true;
        try {
            SecureItemTypeBll secureItemTypeBll = new SecureItemTypeBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<SecureItemType> listItems = secureItemTypeBll.getAllRecords();
            HashMap<String, SecureItemType> mapSecureItemTypes = new HashMap<>();

            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapSecureItemTypes.put(listItems.get(i).getUuid(), listItems.get(i));
            }
            listItems.clear();

            if (secureItemTypes != null && secureItemTypes.length > 0) {
                for (int i = 0, max = secureItemTypes.length; i < max; i++) {
                    SecureItemType sit = mapSecureItemTypes.get(secureItemTypes[i].getUuid());
                    if (sit == null) {
                        SecureItemType newItem = new SecureItemType();
                        newItem.setActive(secureItemTypes[i].isActive());
                        newItem.setCreatedDate(new DateTime(secureItemTypes[i]
                                .getCreatedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newItem.setLastModifiedDate(new DateTime(secureItemTypes[i]
                                .getLastModifiedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                        newItem.setName(secureItemTypes[i].getName());
                        newItem.setOrder(secureItemTypes[i].getOrder());
                        newItem.setUuid(secureItemTypes[i].getUuid());
                        secureItemTypeBll.insertRow(newItem);
                        mapSecureItemTypes.put(newItem.getUuid(), newItem);
                    } else if ((new DateTime(sit.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                            secureItemTypes[i].getLastModifiedDate().getMillis()) {
                        sit.setActive(secureItemTypes[i].isActive());
                        sit.setLastModifiedDate(new DateTime(secureItemTypes[i]
                                .getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        sit.setName(secureItemTypes[i].getName());
                        sit.setOrder(secureItemTypes[i].getOrder());
                        sit.setUuid(secureItemTypes[i].getUuid());
                        secureItemTypeBll.updateRow(sit);
                    }
                }
            }
            mapSecureItemTypes.clear();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean SyncShare(SharesHttpBean shares) {
        boolean result = true;
        try {
            ShareBll shareBll = new ShareBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            SecureItemBll mSecureItemBll = new SecureItemBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<Share> listItems = shareBll.getAllRecords();
            HashMap<String, Share> mapShare = new HashMap<>();

            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapShare.put(listItems.get(i).getId(), listItems.get(i));
            }

            if (shares != null) {
                if (shares.getSent() != null && shares.getSent().length > 0) {
                    for (int i = 0, max = shares.getSent().length; i < max; i++) {

                        Share shareItem = mapShare.get(shares.getSent()[i].getUuid());
                        if (shareItem == null) {
                            Share newItem = new Share();
                            newItem.setCreatedDate(new DateTime(shares.getSent()[i]
                                    .getCreatedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            newItem.setLastModifiedDate(new DateTime(shares.getSent()[i]
                                    .getLastModifiedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            newItem.setNickname(shares.getSent()[i].getNickname());
                            newItem.setData(shares.getSent()[i].getData().toString());
                            newItem.setExpirationDate(new DateTime(shares.getSent()[i]
                                    .getExpirationDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            newItem.setId(shares.getSent()[i].getUuid());
                            newItem.setUuid(shares.getSent()[i].getUuid());
                            newItem.setReceiver(shares.getSent()[i].getReceiver());
                            if (shares.getSent()[i].getSenderAccount() != null) {
                                newItem.setSender(shares.getSent()[i].getSenderAccount().getEmail());
                            }
                            if (shares.getSent()[i].getSecureItemType() != null) {
                                newItem.setSecureItemTypeName(shares.getSent()[i].getSecureItemType().getName());
                            }

                            newItem.setStatus(shares.getSent()[i].getStatus());
                            newItem.setReceiverPk(shares.getSent()[i].getPublicKey());
                            newItem.setActive(shares.getSent()[i].isActive());
                            newItem.setVisible(shares.getSent()[i].isVisible());
                            if (DatabaseConstants.REVOKED.equals(shares.getSent()[i].getStatus()) ||
                                    DatabaseConstants.CANCELED.equals(shares.getSent()[i].getStatus()) ||
                                    DatabaseConstants.REMOVED.equals(shares.getSent()[i].getStatus())) {
                                if (shares.getSent()[i].getReceiver().equals(Pref.EMAIL)) {
                                    newItem.setActive(false);
                                }
                            }

                            newItem.setMessage(shares.getSent()[i].getMessage());
                            shareBll.insertRow(newItem);
/* // TODO: 5/13/2016 refactor
                            if (DatabaseConstants.UPDATED.equals(shares.getSent()[i].getStatus())) {
								new ShareItemResponse(mContext, newItem, DatabaseConstants.PENDING, true)
									.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
							} else if (DatabaseConstants.WAITING_DATA.equals(shares.getSent()[i].getStatus())){
								new ShareItem(mContext, newItem, DatabaseConstants.PENDING).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);
							}
*/
                            mapShare.put(newItem.getId(), newItem);
                        } else if ((new DateTime(shareItem.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                                shares.getSent()[i].getLastModifiedDate().getMillis()) {
                            shareItem.setLastModifiedDate(new DateTime(shares.getSent()[i].getLastModifiedDate(),
                                    DateTimeZone.UTC).toDateTimeISO().toString());
                            shareItem.setData(shares.getSent()[i].getData().toString());
                            shareItem.setExpirationDate(new DateTime(shares.getSent()[i]
                                    .getExpirationDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            shareItem.setId(shares.getSent()[i].getUuid());
                            shareItem.setNickname(shares.getSent()[i].getNickname());
                            shareItem.setUuid(shares.getSent()[i].getUuid());
                            shareItem.setReceiver(shares.getSent()[i].getReceiver());
                            shareItem.setReceiverPk(shares.getSent()[i].getPublicKey());
                            shareItem.setActive(shares.getSent()[i].isActive());
                            shareItem.setVisible(shares.getSent()[i].isVisible());
                            if (!shares.getSent()[i].getStatus().equals(shareItem.getStatus())) {
                                shareItem.setLastAlert(null);
                            }
                            if (DatabaseConstants.REVOKED.equals(shares.getSent()[i].getStatus()) ||
                                    DatabaseConstants.CANCELED.equals(shares.getSent()[i].getStatus()) ||
                                    DatabaseConstants.REMOVED.equals(shares.getSent()[i].getStatus())) {
                                shareItem.setActive(false);
                                if (shares.getSent()[i].getReceiver().equals(Pref.EMAIL)) {
                                    if (shareItem.getSecureItemId() != null) {
                                        mSecureItemBll.deleteItemByID(shareItem.getSecureItemId().getId());
                                    }
                                }
                            }

                            if (DatabaseConstants.REJECTED.equals(shares.getSent()[i].getStatus())) {
                                AnalyticsHelperSegment.logSharing(mContext, AnalyticsHelperSegment.ACTION_REJECTED);
                            } else if (DatabaseConstants.SHARED.equals(shares.getSent()[i].getStatus())) {
                                AnalyticsHelperSegment.logSharing(mContext, AnalyticsHelperSegment.ACTION_ACCEPTED);
                            }

                            if (shares.getSent()[i].getSecureItemType() != null) {
                                shareItem.setSecureItemTypeName(shares.getSent()[i].getSecureItemType().getName());
                            }
                            if (shares.getSent()[i].getSenderAccount() != null) {
                                shareItem.setSender(shares.getSent()[i].getSenderAccount().getEmail());
                            }
                            shareItem.setMessage(shares.getSent()[i].getMessage());
                            shareItem.setStatus(shares.getSent()[i].getStatus());
                            shareBll.updateRow(shareItem);
/* TODO: 5/13/2016 refactor
							if (DatabaseConstants.UPDATED.equals(shares.getSent()[i].getStatus())) {
								if (shareItem.getSecureItemId() != null) {
									new ShareItemResponse(mContext, shareItem, DatabaseConstants.SHARED, true)
											.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
								} else {
									new ShareItemResponse(mContext, shareItem, DatabaseConstants.PENDING, true)
										.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
								}
							} else if (DatabaseConstants.WAITING_DATA.equals(shares.getSent()[i].getStatus())){
								new ShareItem(mContext, shareItem, DatabaseConstants.PENDING).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);
							}
*/
                        }
                    }
                }
                if (shares.getReceived() != null && shares.getReceived().length > 0) {
                    for (int i = 0, max = shares.getReceived().length; i < max; i++) {
                        Share shareItem = mapShare.get(shares.getReceived()[i].getUuid());
                        if (shareItem == null) {
                            Share newItem = new Share();
                            newItem.setActive(shares.getReceived()[i].isActive());
                            if (DatabaseConstants.EXPIRED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.REVOKED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.REJECTED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.CANCELED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.REMOVED.equals(shares.getReceived()[i].getStatus())) {
                                newItem.setActive(false);
                            }
                            newItem.setNickname(shares.getReceived()[i].getNickname());
                            newItem.setCreatedDate(new DateTime(shares.getReceived()[i]
                                    .getCreatedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            newItem.setLastModifiedDate(new DateTime(shares.getReceived()[i]
                                    .getLastModifiedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            newItem.setData(shares.getReceived()[i].getData().toString());
                            if (shares.getReceived()[i].getExpirationDate() != null) {
                                newItem.setExpirationDate(new DateTime(shares.getReceived()[i]
                                        .getExpirationDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            }
                            newItem.setId(shares.getReceived()[i].getUuid());
                            newItem.setUuid(shares.getReceived()[i].getUuid());
                            newItem.setReceiver(shares.getReceived()[i].getReceiver());
                            newItem.setVisible(shares.getReceived()[i].isVisible());
                            newItem.setReceiverPk(shares.getReceived()[i].getPublicKey());
                            if (shares.getReceived()[i].getSenderAccount() != null) {
                                newItem.setSender(shares.getReceived()[i].getSenderAccount().getEmail());
                            }
                            if (shares.getReceived()[i].getSecureItemType() != null) {
                                newItem.setSecureItemTypeName(shares.getReceived()[i].getSecureItemType().getName());
                            }
                            newItem.setMessage(shares.getReceived()[i].getMessage());
                            newItem.setStatus(shares.getReceived()[i].getStatus());
/* TODO: 5/13/2016 refactor
							if (DatabaseConstants.UPDATED.equals(shares.getReceived()[i].getStatus())) {
								new ShareItemResponse(mContext, newItem, DatabaseConstants.PENDING, true)
										.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);

							} else if (DatabaseConstants.WAITING_DATA.equals(shares.getReceived()[i].getStatus())){
								new ShareItem(mContext, newItem, DatabaseConstants.PENDING).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);
							}
*/
                            shareBll.insertRow(newItem);
                        } else if ((new DateTime(shareItem.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                                shares.getReceived()[i].getLastModifiedDate().getMillis()) {
                            shareItem.setActive(shares.getReceived()[i].isActive());
                            if (DatabaseConstants.EXPIRED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.REVOKED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.REJECTED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.CANCELED.equals(shares.getReceived()[i].getStatus()) ||
                                    DatabaseConstants.REMOVED.equals(shares.getReceived()[i].getStatus())) {
                                shareItem.setActive(false);
                                if (shareItem.getSecureItemId() != null) {
                                    mSecureItemBll.deleteItemByID(shareItem.getSecureItemId().getId());
                                }
                            }
                            shareItem.setNickname(shares.getReceived()[i].getNickname());
                            shareItem.setVisible(shares.getReceived()[i].isVisible());
                            shareItem.setLastModifiedDate(new DateTime(shares.getReceived()[i]
                                    .getLastModifiedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            shareItem.setData(shares.getReceived()[i].getData().toString());
                            shareItem.setExpirationDate(new DateTime(shares.getReceived()[i].
                                    getExpirationDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            shareItem.setId(shares.getReceived()[i].getUuid());
                            shareItem.setUuid(shares.getReceived()[i].getUuid());
                            shareItem.setReceiverPk(shares.getReceived()[i].getPublicKey());
                            shareItem.setReceiver(shares.getReceived()[i].getReceiver());
                            if (shares.getReceived()[i].getSenderAccount() != null) {
                                shareItem.setSender(shares.getReceived()[i].getSenderAccount().getEmail());
                            }
                            if (shares.getReceived()[i].getSecureItemType() != null) {
                                shareItem.setSecureItemTypeName(shares.getReceived()[i].getSecureItemType().getName());
                            }
                            shareItem.setMessage(shares.getReceived()[i].getMessage());
                            shareItem.setStatus(shares.getReceived()[i].getStatus());
                            if (!shares.getReceived()[i].getStatus().equals(shareItem.getStatus())) {
                                shareItem.setLastAlert(null);
                            }
/* TODO: 5/13/2016 refactor
							if (DatabaseConstants.UPDATED.equals(shares.getReceived()[i].getStatus())) {
								if (shareItem.getSecureItemId() != null) {
									shareItem.setStatus(DatabaseConstants.SHARED);
									new ShareItemResponse(mContext, shareItem, DatabaseConstants.SHARED, true)
											.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
								} else {
									shareItem.setStatus(DatabaseConstants.PENDING);
									new ShareItemResponse(mContext, shareItem, DatabaseConstants.PENDING)
											.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
								}
							} else if (DatabaseConstants.WAITING_DATA.equals(shares.getReceived()[i].getStatus())){
								new ShareItem(mContext, shareItem, DatabaseConstants.PENDING).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);
							}
*/
                            shareBll.updateRow(shareItem);
                        }
                    }
                }
            }
            mapShare.clear();
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    public boolean SyncSiteDomains(SiteDomainHttpBean[] domains) {
        boolean result = false;
        try {
            SiteDomainBll mSiteDomainBll = new SiteDomainBll(dbNonSecure);
            for (SiteDomainHttpBean siteDomain : domains) {
                SiteDomain mSiteDomain = new SiteDomain();
                mSiteDomain.setActive(siteDomain.isActive());
                mSiteDomain.setCreatedDate(new DateTime(siteDomain.getCreatedDate(),
                        DateTimeZone.UTC).toDateTimeISO().toString());
                mSiteDomain.setLastModifiedDate(new DateTime(siteDomain.getLastModifiedDate(),
                        DateTimeZone.UTC).toDateTimeISO().toString());
                mSiteDomain.setDomain(siteDomain.getDomain());
                mSiteDomain.setUuid(siteDomain.getUuid());
                mSiteDomainBll.insertOrUpdateRow(mSiteDomain);
            }
            result = true;
        } catch (Exception e) {
        }
        return result;
    }

    public boolean SyncSitesAndRecommendedSites(RecommendedSiteHttpBean[] recommendedSite, boolean recommended) {
        boolean result = true;
        try {
            DatabaseHelperSecure helperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
            SiteBll siteBll = new SiteBll(helperSecure);
            SiteImageBll siteImageBll = new SiteImageBll(helperSecure);
            SiteImageSizeBll siteImageSizeBll = new SiteImageSizeBll(helperSecure);
            SiteUriBll siteUriBll = new SiteUriBll(helperSecure);
            FolderBll folderBll = new FolderBll(helperSecure);

            HashMap<String, Site> mapSite = new HashMap<>();
            HashMap<String, SiteImage> mapSiteImage = new HashMap<>();
            HashMap<String, SiteImageSize> mapSiteImageSize = new HashMap<>();
            HashMap<String, SiteUri> mapSiteUri = new HashMap<>();

            List<Site> listSite = siteBll.getAllRecords();
            for (int i = 0, max = listSite.size(); i < max; i++) {
                mapSite.put(listSite.get(i).getId(), listSite.get(i));
            }
            listSite.clear();

            List<SiteUri> listSiteUri = siteUriBll.getAllRecords();
            for (int i = 0, max = listSiteUri.size(); i < max; i++) {
                mapSiteUri.put(listSiteUri.get(i).getUuid(), listSiteUri.get(i));
            }
            listSiteUri.clear();
            List<SiteImage> listSiteImage = siteImageBll.getAllRecords();
            for (int i = 0, max = listSiteImage.size(); i < max; i++) {
                mapSiteImage.put(listSiteImage.get(i).getId(), listSiteImage.get(i));
            }
            listSiteImage.clear();
            List<SiteImageSize> listSiteImageSize = siteImageSizeBll.getAllRecords();
            for (int i = 0, max = listSiteImageSize.size(); i < max; i++) {
                mapSiteImageSize.put(listSiteImageSize.get(i).getUuid(), listSiteImageSize.get(i));
            }
            listSiteImageSize.clear();

            if (recommendedSite != null && recommendedSite.length > 0) {
                for (int i = 0, max = recommendedSite.length; i < max; i++) {
                    Site site = mapSite.get(recommendedSite[i].getUuid());
                    if (site == null) {
                        site = new Site();
                        site.setActive(true);
                        site.setCreatedDate(new DateTime(recommendedSite[i].getCreatedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        site.setLastModifiedDate(new DateTime(recommendedSite[i]
                                .getLastModifiedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                        site.setId(recommendedSite[i].getUuid());
                        site.setName(recommendedSite[i].getName());
                        site.setOrder(recommendedSite[i].getOrder());
                        site.setValidated(recommendedSite[i].isValidated());
                        site.setRecommended(recommended);
                        site.setFriendlyName(recommendedSite[i].getFriendlyName());
                        site.setUuid(recommendedSite[i].getUuid());
                        site.calculateHash();
                        siteBll.insertRow(site);
                        mapSite.put(site.getId(), site);
                    } else {

                        if ((new DateTime(site.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                                recommendedSite[i].getLastModifiedDate().getMillis()) {
                            site.setActive(true);
                            site.setCreatedDate(new DateTime(recommendedSite[i]
                                    .getCreatedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            site.setLastModifiedDate(new DateTime(recommendedSite[i]
                                    .getLastModifiedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            site.setId(recommendedSite[i].getUuid());
                            site.setName(recommendedSite[i].getName());
                            site.setOrder(recommendedSite[i].getOrder());
                            site.setValidated(recommendedSite[i].isValidated());
                            site.setFriendlyName(recommendedSite[i].getFriendlyName());
                            site.setRecommended(recommended);
                            site.setUuid(recommendedSite[i].getUuid());
                            site.calculateHash();
                            siteBll.updateRow(site);
                        }
                    }

                    for (int j = 0, len = recommendedSite[i].getUris().length; j < len; j++) {
                        SiteUri siteUri = mapSiteUri.get(recommendedSite[i].getUris()[j].getUuid());
                        if (siteUri == null) {
                            siteUri = new SiteUri();
                            siteUri.setActive(true);
                            siteUri.setBehavior(recommendedSite[i].getUris()[j].getBehavior());
                            siteUri.setCreatedDate(new DateTime(recommendedSite[i].getUris()[j]
                                    .getCreatedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                            siteUri.setLastModifiedDate(new DateTime(recommendedSite[i].getUris()[j]
                                    .getLastModifiedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            siteUri.setIsTrivial(recommendedSite[i].getUris()[j].isTrivial());
                            siteUri.setType(recommendedSite[i].getUris()[j].getType());
                            siteUri.setHost(recommendedSite[i].getUris()[j].getHost());
                            siteUri.setLookupMethod(recommendedSite[i].getUris()[j].getLookupMethod());
                            siteUri.setUri(recommendedSite[i].getUris()[j].getUri());
                            siteUri.setUuid(recommendedSite[i].getUris()[j].getUuid());
                            siteUri.setVerifiedCount(recommendedSite[i].getUris()[j].getVerifiedCount());
                            siteUri.setSiteId(site);
                            siteUri.calculateHash();
                            siteUriBll.insertRow(siteUri);
                            mapSiteUri.put(siteUri.getUuid(), siteUri);
                        } else {
                            if ((new DateTime(siteUri.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                                    recommendedSite[i].getUris()[j].getLastModifiedDate().getMillis()) {
                                siteUri.setActive(true);
                                siteUri.setBehavior(recommendedSite[i].getUris()[j].getBehavior());
                                siteUri.setLastModifiedDate(new DateTime(recommendedSite[i].getUris()[j]
                                        .getLastModifiedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                                siteUri.setIsTrivial(recommendedSite[i].getUris()[j].isTrivial());
                                siteUri.setType(recommendedSite[i].getUris()[j].getType());
                                siteUri.setHost(recommendedSite[i].getUris()[j].getHost());
                                siteUri.setLookupMethod(recommendedSite[i].getUris()[j].getLookupMethod());
                                siteUri.setUri(recommendedSite[i].getUris()[j].getUri());
                                siteUri.setUuid(recommendedSite[i].getUris()[j].getUuid());
                                siteUri.setVerifiedCount(recommendedSite[i].getUris()[j].getVerifiedCount());
                                siteUri.setSiteId(site);
                                siteUri.calculateHash();
                                siteUriBll.updateRow(siteUri);
                            }
                        }
                    }

/* /* // TODO: 5/13/2016 refactor
					if (recommendedSite[i].getImages().length == 0) {
						try {
							new CheckImageExist(mContext, recommendedSite[i].getUris()[0].getUri(), site.getId()).execute();
						} catch (Exception e) {
						}
					}
*/

                    for (int j = 0, len = recommendedSite[i].getImages().length; j < len; j++) {
                        SiteImage siteImage = mapSiteImage.get(recommendedSite[i].getImages()[j].getUuid());
                        SiteImageSize siteImageSize = mapSiteImageSize.get(recommendedSite[i].getImages()[j]
                                .getSize().getUuid());
                        if (siteImageSize == null) {
                            siteImageSize = new SiteImageSize();
                            siteImageSize.setActive(recommendedSite[i].getImages()[j].getSize().isActive());
                            siteImageSize.setCreatedDate(new DateTime(recommendedSite[i]
                                    .getImages()[j].getSize().getCreatedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            siteImageSize.setLastModifiedDate(new DateTime(recommendedSite[i]
                                    .getImages()[j].getSize().getLastModifiedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            siteImageSize.setHeight(recommendedSite[i].getImages()[j].getSize().getHeight());
                            siteImageSize.setWidth(recommendedSite[i].getImages()[j].getSize().getWidth());
                            siteImageSize.setUuid(recommendedSite[i].getImages()[j].getSize().getUuid());
                            siteImageSize.setName(recommendedSite[i].getImages()[j].getSize().getName());
                            siteImageSize.setId(recommendedSite[i].getImages()[j].getSize().getUuid());
                            siteImageSize.calculateHash();
                            siteImageSizeBll.insertRow(siteImageSize);
                            mapSiteImageSize.put(siteImageSize.getUuid(), siteImageSize);
                        } else {
                            if ((new DateTime(siteImageSize.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                                    recommendedSite[i].getImages()[j].getSize().getLastModifiedDate().getMillis()) {
                                siteImageSize.setActive(recommendedSite[i].getImages()[j].getSize().isActive());
                                siteImageSize.setCreatedDate(new DateTime(recommendedSite[i]
                                        .getImages()[j].getSize().getCreatedDate(), DateTimeZone.UTC)
                                        .toDateTimeISO().toString());
                                siteImageSize.setLastModifiedDate(new DateTime(recommendedSite[i]
                                        .getImages()[j].getSize().getLastModifiedDate(), DateTimeZone.UTC)
                                        .toDateTimeISO().toString());
                                siteImageSize.setHeight(recommendedSite[i].getImages()[j].getSize().getHeight());
                                siteImageSize.setWidth(recommendedSite[i].getImages()[j].getSize().getWidth());
                                siteImageSize.setUuid(recommendedSite[i].getImages()[j].getSize().getUuid());
                                siteImageSize.setName(recommendedSite[i].getImages()[j].getSize().getName());
                                siteImageSize.setId(recommendedSite[i].getImages()[j].getSize().getUuid());
                                siteImageSize.calculateHash();
                                siteImageSizeBll.updateRow(siteImageSize);
                            }
                        }

                        if (siteImage == null) {
                            siteImage = new SiteImage();
                            siteImage.setActive(recommendedSite[i].getImages()[j].isActive());
                            siteImage.setCreatedDate(new DateTime(recommendedSite[i]
                                    .getImages()[j].getCreatedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            siteImage.setLastModifiedDate(new DateTime(recommendedSite[i]
                                    .getImages()[j].getLastModifiedDate(), DateTimeZone.UTC)
                                    .toDateTimeISO().toString());
                            siteImage.setId(recommendedSite[i].getImages()[j].getUuid());
                            siteImage.setSiteId(site);
                            siteImage.setUrl(recommendedSite[i].getImages()[j].getUrl());
                            siteImage.setUuid(recommendedSite[i].getImages()[j].getUuid());
                            siteImage.calculateHash();
                            siteImage.setSizeId(siteImageSize);
                            siteImageBll.insertRow(siteImage);
/* TODO: 5/13/2016 refactor, candidate for replacement is library: Glide
							try {
								new DownloadFileImage(mContext, site.getId(), true,siteImage.getUrl()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							} catch (Exception e) {
							}
*/
                            mapSiteImage.put(siteImage.getId(), siteImage);
                        } else {
                            if ((new DateTime(siteImage.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                                    recommendedSite[i].getImages()[j].getLastModifiedDate().getMillis()) {
                                siteImage.setActive(recommendedSite[i].getImages()[j].isActive());
                                siteImage.setCreatedDate(new DateTime(recommendedSite[i]
                                        .getImages()[j].getCreatedDate(), DateTimeZone.UTC)
                                        .toDateTimeISO().toString());
                                siteImage.setLastModifiedDate(new DateTime(recommendedSite[i]
                                        .getImages()[j].getLastModifiedDate(), DateTimeZone.UTC)
                                        .toDateTimeISO().toString());
                                siteImage.setId(recommendedSite[i].getImages()[j].getUuid());
                                siteImage.setSiteId(site);
                                siteImage.setUrl(recommendedSite[i].getImages()[j].getUrl());
                                siteImage.setUuid(recommendedSite[i].getImages()[j].getUuid());
                                siteImage.calculateHash();
                                siteImage.setSizeId(siteImageSize);
                                siteImageBll.updateRow(siteImage);
/* TODO: 5/13/2016 refactor, candidate for replacement is library: Glide
								try {
									new DownloadFileImage(mContext, site.getId(), true,siteImage.getUrl()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
								} catch (Exception e) {
								}
*/
                            }
                        }
                    }
                }
            }
            mapSite.clear();
            mapSiteUri.clear();
            mapSiteImage.clear();
            mapSiteImageSize.clear();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean SyncStorageBackup(boolean isEnable) {
        try {
            SettingsBll mSettingsBll = new SettingsBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            mSettingsBll.insertOrUpdate(DatabaseConstants.CLOUD_STORAGE_ENABLE, isEnable + "");
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean SyncStorageRegions(BaseHttpBean[] regions) {
        boolean result = true;
        try {
            StorageRegionBll storageRegionBll = new StorageRegionBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<StorageRegion> listItems = storageRegionBll.getAllRecords();
            HashMap<String, StorageRegion> mapRegions = new HashMap<>();

            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapRegions.put(listItems.get(i).getUuid(), listItems.get(i));
            }
            listItems.clear();

            if (regions != null && regions.length > 0) {
                for (int i = 0, max = regions.length; i < max; i++) {
                    StorageRegion sr = mapRegions.get(regions[i].getUuid());

                    if (sr == null) {
                        StorageRegion newRegion = new StorageRegion();
                        newRegion.setActive(regions[i].isActive());
                        newRegion.setCreatedDate(new DateTime(regions[i].getCreatedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newRegion.setLastModifiedDate(new DateTime(regions[i]
                                .getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        newRegion.setName(regions[i].getName());
                        newRegion.setUuid(regions[i].getUuid());
                        storageRegionBll.insertRow(newRegion);
                        mapRegions.put(newRegion.getUuid(), newRegion);
                    } else if ((new DateTime(sr.getLastModifiedDate(), DateTimeZone.UTC)).getMillis() <
                            regions[i].getLastModifiedDate().getMillis()) {
                        sr.setActive(regions[i].isActive());
                        sr.setLastModifiedDate(new DateTime(regions[i]
                                .getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        sr.setName(regions[i].getName());
                        storageRegionBll.updateRow(sr);
                    }
                }
            }
            mapRegions.clear();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean SyncUserInfo(AccountHttpBean accountHttpBean) {
        boolean result = true;
        try {
            SettingsBll mSettingsBll = new SettingsBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            UserInfoBll userInfoBll = new UserInfoBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            StorageRegionBll storageRegionBll = new StorageRegionBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            UserSubscriptionBll userSubscriptionBll = new UserSubscriptionBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<UserInfo> listItems = userInfoBll.getAllRecords();
            List<UserSubscription> listUserSubscription = userSubscriptionBll.getAllRecords();

            HashMap<String, UserInfo> mapAccounts = new HashMap<>();
            HashMap<String, UserSubscription> mapUserSubscription = new HashMap<>();

            for (int i = 0, max = listItems.size(); i < max; i++) {
                mapAccounts.put(listItems.get(i).getEmail(), listItems.get(i));
            }
            for (int i = 0, max = listUserSubscription.size(); i < max; i++) {
                mapUserSubscription.put(listUserSubscription.get(i).getEmail(), listUserSubscription.get(i));
            }

            listUserSubscription.clear();
            listItems.clear();

            if (accountHttpBean != null) {
                UserInfo userInfo = mapAccounts.get(accountHttpBean.getEmail());
                if (userInfo != null) {
                    DateTime lastModification = new DateTime(userInfo.getLastModifiedDate(), DateTimeZone.UTC);
                    if (lastModification.getMillis() < accountHttpBean.getLastModifiedDate().getMillis()) {
                        userInfo.setFirstName(accountHttpBean.getFirstName());
                        userInfo.setLastName(accountHttpBean.getLastName());
                        userInfo.setEmail(accountHttpBean.getEmail());
                        userInfo.setPhone(accountHttpBean.getPhone());
                        userInfo.setLastModifiedDate(new DateTime(accountHttpBean
                                .getLastModifiedDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        if (accountHttpBean != null
                                && accountHttpBean.getStorageRegion() != null
                                && accountHttpBean.getStorageRegion().getUuid() != null) {
                            userInfo.setStorageRegionUuid(storageRegionBll
                                    .getStorageRegionByUuid(accountHttpBean.getStorageRegion().getUuid()));
                        }
                        userInfo.setSubscription(accountHttpBean
                                .getSubscription().getSubscriptionLevel().getDescription());
                        userInfo.calculateHash();
                        userInfoBll.updateRow(userInfo);
                    }
                }
            }

            mapAccounts.clear();

            if (accountHttpBean != null && accountHttpBean.getSubscription() != null) {
                UserSubscription userSubscription = mapUserSubscription.get(Pref.EMAIL);
                if (userSubscription == null) {
                    userSubscription = new UserSubscription();
                    userSubscription.setCreatedDate(new DateTime(accountHttpBean.getSubscription().getCreatedDate(), DateTimeZone.UTC)
                            .toDateTimeISO().toString());
                    userSubscription.setLastModifiedDate(new DateTime(
                            accountHttpBean.getSubscription().getLastModifiedDate(),
                            DateTimeZone.UTC).toDateTimeISO().toString());
                    userSubscription.setDescription(accountHttpBean.getSubscription().getSubscriptionLevel().getDescription());
                    userSubscription.setDuration(accountHttpBean.getSubscription().getSubscriptionLevel().getDuration());
                    userSubscription.setEmail(Pref.EMAIL);
                    userSubscription.setExpirationDate(new DateTime(
                            accountHttpBean.getSubscription().getExpirationDate(), DateTimeZone.UTC)
                            .toDateTimeISO().toString());
                    userSubscription.setName(accountHttpBean.getSubscription().getSubscriptionLevel().getSubscriptionType());
                    userSubscription.setMultiuser(accountHttpBean.getSubscription().getSubscriptionLevel().isMultiuser());
                    userSubscription.setNextValidationDate(new DateTime(
                            accountHttpBean.getSubscription().getNextValidationDate(), DateTimeZone.UTC)
                            .toDateTimeISO().toString());
                    userSubscriptionBll.insertRow(userSubscription);
                    mapUserSubscription.put(userSubscription.getEmail(), userSubscription);
                } else {
                    DateTime d = new DateTime(userSubscription.getLastModifiedDate(), DateTimeZone.UTC);
                    if (d.getMillis() < accountHttpBean.getSubscription().getLastModifiedDate().getMillis()) {
                        try {
                            if (!Constants.PAID.equals(userSubscription.getName()) && Constants.PAID.equals(accountHttpBean.getSubscription().getSubscriptionLevel().getSubscriptionType())) {
                                String provider = "";

                                if (accountHttpBean.getSubscription().getProvider() != null && accountHttpBean.getSubscription().getProvider().getName() != null) {
                                    provider = accountHttpBean.getSubscription().getProvider().getName();
                                }

                                AnalyticsHelperSegment.logFreeToPaid(mContext,
                                        accountHttpBean.getSubscription().getSubscriptionLevel().getSubscriptionType(),
                                        accountHttpBean.getSubscription().getSubscriptionLevel().getInternalName(),
                                        provider,
                                        accountHttpBean.getSubscription().getSource(),
                                        accountHttpBean.getSubscription().getMedium(),
                                        accountHttpBean.getSubscription().getCampaign());
                            }
                        } catch (Exception e) {
                        }

                        userSubscription.setLastModifiedDate(new DateTime(
                                accountHttpBean.getSubscription().getLastModifiedDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                        userSubscription.setDescription(accountHttpBean.getSubscription().getSubscriptionLevel().getDescription());
                        userSubscription.setDuration(accountHttpBean.getSubscription().getSubscriptionLevel().getDuration());
                        userSubscription.setEmail(Pref.EMAIL);
                        userSubscription.setExpirationDate(new DateTime(
                                accountHttpBean.getSubscription().getExpirationDate(), DateTimeZone.UTC).toDateTimeISO().toString());
                        userSubscription.setName(accountHttpBean.getSubscription().getSubscriptionLevel().getSubscriptionType());
                        userSubscription.setMultiuser(accountHttpBean.getSubscription().getSubscriptionLevel().isMultiuser());
                        userSubscription.setNextValidationDate(new DateTime(
                                accountHttpBean.getSubscription().getNextValidationDate(), DateTimeZone.UTC)
                                .toDateTimeISO().toString());
                        userSubscriptionBll.updateRow(userSubscription);
                    }
                }
            }

            if (accountHttpBean != null) {
                Settings mSettings = mSettingsBll.getSettingsByKey(DatabaseConstants.ITEM_SETTINGS_COUNTRY);
                if (mSettings == null) {
                    mSettingsBll.insertOrUpdate(DatabaseConstants.ITEM_SETTINGS_COUNTRY, accountHttpBean.getCountry().getCode());
                }
                if (accountHttpBean.getInstallation() != null && accountHttpBean.getInstallation().getChannel() != null) {
                    String searchProviderUrl = accountHttpBean.getInstallation().getChannel().getSearch_provider_url();
                    String startPageUrl = accountHttpBean.getInstallation().getChannel().getStartPageUrl();
                    mSettingsBll.insertOrUpdate(DatabaseConstants.SEARCH_PROVIDER_URL, searchProviderUrl);
                    mSettingsBll.insertOrUpdate(DatabaseConstants.START_PAGE_URL, startPageUrl);
                }
            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean UploadDatabase(FileHttpBean fileHttpBean, boolean masterPasswordChange) {
        boolean result = true;
        try {
            String tableHash = DatabaseSync.calculateHash(DatabaseConstants.getSecuredDBPath(), Pref.DATABASE_KEY);
            File secureDatabase = new File(DatabaseConstants.getSecuredDBPath());
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext));
            String dbHash = null;
            int masterPasswordVersion = 1;
            if (fileHttpBean != null) {
                masterPasswordVersion = fileHttpBean.getMasterPasswordVersion();
                dbHash = fileHttpBean.getHash();
            } else {
                Configuration mConfigurationMasterPasswordVersion =
                        configurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_VERSION);
                if (mConfigurationMasterPasswordVersion != null) {
                    masterPasswordVersion = Integer.parseInt(mConfigurationMasterPasswordVersion.getValue());
                }
                Configuration mConfigurationHash = configurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, Constants.CONFIGURATION_LAST_SYNC_HASH);
                if (mConfigurationHash != null) {
                    dbHash = mConfigurationHash.getValue();
                }
            }

            if ((!tableHash.equals(dbHash) || masterPasswordChange) && secureDatabase.exists()) {
                if (masterPasswordChange) {
                    masterPasswordVersion++;
                }
                if (Utils.isOnline(mContext)) {
                    if (tableHash != null && !tableHash.equals("")) {
                        if (uploadFile(new ServerAPI().getSecureFile(), tableHash, masterPasswordVersion) == 200) {
                            try {
                                configurationBll.updateItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_CHANGE, "0");
                                configurationBll.updateItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_VERSION, masterPasswordVersion + "");
                                configurationBll.updateOrInsertItem(Pref.EMAIL, Constants.CONFIGURATION_LAST_SYNC_HASH, tableHash);
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        result = false;
                    }
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public int attachDatabaseDownloadDB() {
        return attachDatabaseDownloadDB(Pref.DATABASE_KEY);
    }

    public int attachDatabaseDownloadDB(String password) {
        int result = attachDatabase(mContext, DatabaseConstants.getSecuredDBPathBackUp(), password, Pref.INSTALLATION_UUID);
        if (result != 0) {
            File deleteDownloadFile = new File(DatabaseConstants.getSecuredDBPathBackUp());
            deleteDownloadFile.delete();
        }
        return result;
    }

    public void deleteExpiredShareItem() {
        try {
            ShareBll mShareBll = new ShareBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));

            ArrayList<Share> mListExpiredShareItem = mShareBll.getExpiredShareItem();

/*TODO: 5/13/2016 refactor
			new ShareItem(mContext, mListExpiredShareItem, DatabaseConstants.EXPIRED)
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);
*/

        } catch (Exception e) {
            Pref.setValue(mContext, Constants.LATEST_SYNC, false);
        }
    }

    public void deleteShareItemByStatus() {
        try {
            ShareBll mShareBll = new ShareBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<Share> mListShareItem = mShareBll.getAllShareItemActive();
            for (Share item : mListShareItem) {
                if (
                        item.getStatus().equalsIgnoreCase(DatabaseConstants.CANCELED) ||
                                item.getStatus().equalsIgnoreCase(DatabaseConstants.REVOKED) ||
                                item.getStatus().equalsIgnoreCase(DatabaseConstants.REMOVED)) {
                    item.setActive(false);
                }
                mShareBll.updateRow(item);
            }
        } catch (Exception e) {

        }
    }

    public void revokeDeletedSecureItem() {
/* TODO: 5/13/2016 refactor
		try {
			ShareBll mShareBll = new ShareBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));

			ArrayList<Share> mListExpiredShareItem = mShareBll.getShareItemForRevoke(
					Pref.getValue(mContext, Constants.EMAIL, null));
				new ShareItem(mContext, mListExpiredShareItem, DatabaseConstants.REVOKED)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);


			mListExpiredShareItem = mShareBll.getShareItemForCancel(
					Pref.getValue(mContext, Constants.EMAIL, null));

			new ShareItem(mContext, mListExpiredShareItem, DatabaseConstants.CANCELED)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);


		} catch (Exception e) {
			Pref.setValue(mContext, Constants.LATEST_SYNC, false);
		}
*/
    }

    public void showInfo() {
        throw new UnsupportedOperationException(); // TODO: 5/13/2016 refactor, see old implementation
    }

    public void updateStatusShareItem() {
        try {
            ShareBll mShareBll = new ShareBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
            List<Share> mListUpdateStatusShareItem = mShareBll.getShareByStatus(DatabaseConstants.NOSENT);
/* TODO: 5/13/2016 refactor
			for (Share item : mListUpdateStatusShareItem) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					new ShareItem(mContext, item, DatabaseConstants.UPDATED)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void[])null);
				} else {
						new ShareItem(mContext, item, DatabaseConstants.UPDATED)
							.execute((Void[])null);
				}
			}
*/
        } catch (Exception e) {
            Pref.setValue(mContext, Constants.LATEST_SYNC, false);
        }
    }

    public int uploadFile(String server, String hash, int masterPasswordVersion) {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        File sourceFile = new File(DatabaseConstants.getSecuredDBPath());
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String uuid = UUID.randomUUID().toString();
        String boundary = "Boundary-" + uuid;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        int serverResponseCode = 0;

        if (!sourceFile.isFile()) {
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile.getAbsolutePath());
                URL url = new URL(server);

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty(HttpConstants.AUTHORIZATION, Pref.EMAIL + "|" + Pref.DEVICE_UUID);
                conn.setRequestProperty(HttpConstants.ACCEPT, HttpConstants.PASSWORDBOSS_VERSION);
                conn.setRequestProperty(HttpConstants.ACCEPT_LANGUAGE, HttpConstants.LANGUAGE);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes(
                        twoHyphens + boundary + lineEnd +
                                "Content-Disposition: form-data; name=\"hash\"" +
                                lineEnd + lineEnd + hash);


                dos.writeBytes(
                        twoHyphens + boundary + lineEnd +
                                "Content-Disposition: form-data; name=\"checksum\"" +
                                lineEnd + lineEnd + Utils.calculateMD5(sourceFile));

                dos.writeBytes(
                        twoHyphens + boundary + lineEnd +
                                "Content-Disposition: form-data; name=\"master_password_version\"" +
                                lineEnd + lineEnd + masterPasswordVersion);

                dos.writeBytes(
                        twoHyphens + boundary + lineEnd +
                                "Content-Disposition: form-data; name=\"file\"; filename=\"file\";" +
                                lineEnd + "Content-Type:application/octet-stream" +
                                lineEnd + lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);

                dos.flush();

                serverResponseCode = conn.getResponseCode();
                fileInputStream.close();
                dos.close();

            } catch (MalformedURLException e) {
            } catch (Exception e) {
            }
            return serverResponseCode;
        }
    }

}
