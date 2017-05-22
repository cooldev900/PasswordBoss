package com.passwordboss.android.task;

import android.content.Context;
import android.util.Base64;

import com.passwordboss.android.actionqueue.ActionQueue;
import com.passwordboss.android.analytics.AnalyticsHelperSegment;
import com.passwordboss.android.analytics.DailyAnalyticsHelper;
import com.passwordboss.android.app.ResetApp;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.crypto.CryptoHelper;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.DatabaseSync;
import com.passwordboss.android.database.beans.Configuration;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.beans.UserInfo;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.database.bll.SecureItemStatsBll;
import com.passwordboss.android.database.bll.UserInfoBll;
import com.passwordboss.android.event.SyncProgressEvent;
import com.passwordboss.android.event.SyncResultEvent;
import com.passwordboss.android.http.NullResponseError;
import com.passwordboss.android.http.ServerAPI;
import com.passwordboss.android.http.beans.AccountPatchHttpBean;
import com.passwordboss.android.http.beans.FileHttpBean;
import com.passwordboss.android.http.beans.ServerResponseHttpBean;
import com.passwordboss.android.subscription.SubscriptionValidator;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.crypto.utils.pem.CryptoPemHelper;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.security.KeyPair;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SyncTask extends Thread {

    public static final int DB_STATUS_VERSION_ERROR = 2;
    public static final int DB_STATUS_TO_UPLOAD = 1;
    public static final int DB_STATUS_NOTHING = 0;
    public static final int DB_STATUS_SKIP_UPLOAD = 3;
    public static final int DB_STATUS_ERROR_401 = 4;
    private static final AtomicBoolean SYNCING = new AtomicBoolean(false);
    private final DatabaseSync mDatabaseSync;
    private final Context mContext;
    private ServerAPI mApi = new ServerAPI();
    private FileHttpBean mFileHttpBean;
    private boolean mMasterPasswordWasChanged;
    private ServerResponseHttpBean mResponse;

    public SyncTask(Context context) {
        mContext = context;
        mDatabaseSync = new DatabaseSync(mContext);
    }

    void blockingRun() throws Exception {
        try {
            while (SYNCING.get()) {
                Thread.sleep(1000);
            }
            SYNCING.set(true);
            runImpl();
        } finally {
            SYNCING.set(false);
        }
    }

    private void callSyncConfiguration() throws SQLException {
        ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext));
        Configuration configuration = configurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, Constants.CONFIGURATION_STORE_DATABASE_HASH);
        if (null == configuration) {
            configurationBll.insertItem(Pref.EMAIL,
                    Constants.CONFIGURATION_STORE_DATABASE_HASH,
                    DatabaseSync.calculateHash(DatabaseConstants.getSecuredDBPath(), Pref.DATABASE_KEY));
        } else {
            configurationBll.updateItem(Pref.EMAIL,
                    Constants.CONFIGURATION_STORE_DATABASE_HASH,
                    DatabaseSync.calculateHash(DatabaseConstants.getSecuredDBPath(), Pref.DATABASE_KEY));
        }
        configuration = configurationBll.getConfigurationByEmailAndKey(
                Pref.EMAIL,
                Constants.CONFIGURATION_MASTER_PASSWORD_CHANGE);
        if (null == configuration) {
            configurationBll.insertItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_CHANGE, "0");
        } else {
            configurationBll.updateItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_CHANGE, "0");
        }
        configuration = configurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_VERSION);
        if (configuration == null) {
            configurationBll.insertItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_VERSION, "1");
        } else {
            if (mFileHttpBean == null) {
                configurationBll.updateItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_VERSION, configuration.getValue());
            } else {
                configurationBll.updateItem(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_VERSION, String.valueOf(mFileHttpBean.getMasterPasswordVersion()));
            }
        }
        if (null != mResponse
                && null != mResponse.getCon()
                && !"".equals(mResponse.getCon())
                && mResponse.getCon().length() > 2) {
            configurationBll.updateOrInsertItem(Constants.NO_EMAIL, Constants.EXT_APP_PARAMS, mResponse.getCon());
        }
        mDatabaseSync.deleteExpiredShareItem();
        mDatabaseSync.revokeDeletedSecureItem();
        mDatabaseSync.updateStatusShareItem();
        mDatabaseSync.deleteShareItemByStatus();
        boolean successSync = Pref.getValue(mContext, Constants.LATEST_SYNC, false);
        if (successSync && mResponse != null) {
            mDatabaseSync.SyncDevices(mResponse.getDevices(), mResponse.getNow());
        }

        Pref.SYNC_DEVICE = false;
        if (mResponse != null) {
            ActionQueue.addActionSync(mContext, mResponse.getNow(), mResponse.getNextMobileSync());
        }
    }

    public void checkRsa() {
        try {
            if (Pref.EMAIL == null) {
                String email = Pref.getValue(mContext, Constants.EMAIL, "UNKNOWN");
                if (!email.equals("UNKNOWN")) {
                    Pref.EMAIL = email;
                }
            }

            DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure
                    .getHelper(mContext, Pref.DATABASE_KEY);
            UserInfoBll mUserInfoBll = new UserInfoBll(mDatabaseHelperSecure);
            UserInfo mUserInfo = mUserInfoBll.getUserInfoByEmail(Pref.EMAIL);
            if (mUserInfo != null) {
                ServerAPI api = new ServerAPI();
                if (!CryptoHelper.isRSAkeySizeV2(mUserInfo.getPublicKey())) {
                    ServerResponseHttpBean response = api.getAccountEndpointGet();
                    if (response != null && response.getError() == null) {
                        String userPublicKey = response.getAccount().getPublicKey();
                        if (!CryptoHelper.isRSAkeySizeV2(userPublicKey.getBytes())) {
                            KeyPair key = CryptoHelper.createKeyPair();
                            String publicKey = CryptoPemHelper.getPEMPublicKey(key);
                            String privateKey = CryptoPemHelper.getPEMPrivateKey(key);

                            AccountPatchHttpBean accountPatch = new AccountPatchHttpBean(
                                    Base64.encodeToString(CryptoPemHelper.getPEMPublicKey(key).getBytes(), Base64.DEFAULT));
                            ServerResponseHttpBean responseAccountPatch = api.accountPatch(accountPatch);
                            if (responseAccountPatch != null && responseAccountPatch.getError() == null) {
                                mUserInfo.setPublicKey(publicKey.getBytes());
                                mUserInfo.setPrivateKey(privateKey.getBytes());
                                mUserInfoBll.updateRow(mUserInfo);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dailyAnalytics() throws SQLException {
        // TODO: 4/15/2016 to refactor
        boolean sendData = false;
        String storedDate = Pref.getValue(mContext, Pref.DAILY_ANALYTICS_UPDATE_DATE, "");
        if (storedDate.length() > 0) {
            DateTime d1 = DateTime.now();
            DateTime d2 = DateTime.parse(storedDate,
                    DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"));
            long diff = d1.getMillis() - d2.getMillis();
            if (diff > 86400000) {
                sendData = true;
            }
        } else {
            sendData = true;
        }
        DatabaseHelperSecure mDatabaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
        SecureItemStatsBll mSecureItemStatsBll = new SecureItemStatsBll(mDatabaseHelperSecure);
        mSecureItemStatsBll.CheckForDuplicatePasswords();
        if (!sendData) return;
        DatabaseHelperSecure databaseHelperSecure = DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY);
        SecureItemBll secureItemBll = new SecureItemBll(databaseHelperSecure);
        DailyAnalyticsHelper dAH = secureItemBll.getAnalysis();
        if (dAH.getTotal_pwd_cnt() > 0) {
            double pwdScoreCo = (1 - ((double) dAH.getWeak_pwds_cnt() / (double) dAH.getTotal_pwd_cnt()));
            dAH.setWeak_pwd_score(Math.round(dAH.getWeak_pwd_score() * pwdScoreCo));
            double dupScoreCo = (1 - ((double) dAH.getDuplicate_pwds_cnt() / (double) dAH.getTotal_pwd_cnt()));
            dAH.setDup_pwd_score(Math.round(dAH.getDup_pwd_score() * dupScoreCo));
            double oldScoreCo = (1 - ((double) dAH.getOld_pwds_cnt() / (double) dAH.getTotal_pwd_cnt()));
            dAH.setOld_pwd_score(Math.round(dAH.getOld_pwd_score() * oldScoreCo));
            dAH.setTotal_pwd_score(dAH.getWeak_pwd_score() + dAH.getDup_pwd_score() + dAH.getOld_pwd_score());
        } else {
            dAH.setTotal_pwd_score(100);
        }
        AnalyticsHelperSegment.logAnalytics(mContext, ((int) dAH.getTotal_pwd_score()) + "",
                dAH.getDuplicate_pwds_cnt() + "",
                dAH.getWeak_pwds_cnt() + "",
                dAH.getOld_pwds_cnt() + "",
                dAH.getTotal_pwd_cnt() + "");
        ServerResponseHttpBean serverResponseHttpBean = mApi.dailyAnalyticsPost(dAH);
        if (serverResponseHttpBean.getError() == null) {
            Pref.setValue(mContext, Pref.DAILY_ANALYTICS_UPDATE_DATE, DateTime.now().toString("MM/dd/yyyy HH:mm:ss"));
        }
    }

    private void postProgress(int progress) {
        EventBus.getDefault().post(new SyncProgressEvent(progress));
    }

    @Override
    public void run() {
        if (SYNCING.getAndSet(true)) return;
        try {
            runImpl();
            Pref.get()
                    .edit()
                    .putLong(Pref.LAST_SYNC_TIME, System.currentTimeMillis())
                    .apply();
            EventBus.getDefault().postSticky(new SyncResultEvent());
        } catch (Exception e) {
            Pref.setValue(mContext, Constants.LATEST_SYNC, false);
            EventBus.getDefault().postSticky(new SyncResultEvent(e));
        } finally {
            SYNCING.set(false);
        }
    }

    private void runImpl() throws Exception {
        int status = syncDatabase();
        switch (status) {
            case SyncTask.DB_STATUS_TO_UPLOAD:
                uploadDatabase();
                callSyncConfiguration();
                break;
            case SyncTask.DB_STATUS_VERSION_ERROR:
                Pref.setValue(mContext, Constants.LATEST_SYNC, false);
                mDatabaseSync.showInfo();
                callSyncConfiguration();
                break;
            case SyncTask.DB_STATUS_SKIP_UPLOAD:
                Pref.setValue(mContext, Constants.LATEST_SYNC, false);
                callSyncConfiguration();
                break;
            case SyncTask.DB_STATUS_ERROR_401:
                new ResetApp().execute(mContext);
                break;
        }

    }

    private int syncDatabase() throws Exception {
        int status = SyncTask.DB_STATUS_TO_UPLOAD;
        Pref.setValue(mContext, Constants.LATEST_SYNC, false);
        DatabaseHelperNonSecure databaseHelperNonSecure = DatabaseHelperNonSecure.getHelper(mContext);
        ConfigurationBll configurationBll = new ConfigurationBll(databaseHelperNonSecure);
        Configuration configuration = configurationBll.getConfigurationByEmailAndKey(Pref.EMAIL, Constants.CONFIGURATION_MASTER_PASSWORD_CHANGE);
        mMasterPasswordWasChanged = null != configuration && "1".equals(configuration.getValue());
        DeviceBll deviceBll = new DeviceBll(DatabaseHelperSecure.getHelper(mContext, Pref.DATABASE_KEY));
        Device deviceForSync = deviceBll.getDeviceByInstallationUuid(Pref.INSTALLATION_UUID);
        if (deviceForSync != null && !mMasterPasswordWasChanged && deviceForSync.getLatestSync() != null) {
            mResponse = mApi.getSync(deviceForSync.getLatestSync(), false);
        } else {
            Configuration seedSync = configurationBll.getConfigurationByEmailAndKey(Constants.NO_EMAIL, Constants.SEED_SINCE);
            if (seedSync != null) {
                mResponse = mApi.getSync(seedSync.getValue(), true);
            } else {
                mResponse = mApi.getSync(null, false);
            }
        }
        if (null == mResponse) throw new NullResponseError(mContext);
        if (null == mResponse.getError()) {
            if (null != mResponse.getSecureFiles() && mResponse.getSecureFiles().length > 0) {
                for (int k = 0, len = mResponse.getSecureFiles().length; k < len; k++) {
                    if (k == 0) {
                        mFileHttpBean = mResponse.getSecureFiles()[k];
                    } else {
                        if (mResponse.getSecureFiles()[k].getLastModifiedDate()
                                .getMillis() > mFileHttpBean.getLastModifiedDate()
                                .getMillis()) {
                            mFileHttpBean = mResponse.getSecureFiles()[k];
                        }
                    }
                }

                ServerResponseHttpBean fileLink = mApi.getDownloadDBLink(mFileHttpBean.getDownloadUrl());
                if (fileLink != null && fileLink.getError() == null) {
                    if (!mMasterPasswordWasChanged) {
                        status = mDatabaseSync.DownloadDatabase(fileLink.getSecureFile().getDownloadUrl(),
                                DatabaseConstants.getSecuredDBPathBackUp(), true, fileLink.getSecureFile().getChecksum());
                    } else {
                        status = DB_STATUS_TO_UPLOAD;
                    }
                }
            }
            mDatabaseSync.SyncFeatures(mResponse.getFeatures());
            SubscriptionValidator.featureList = null;
            mDatabaseSync.SyncLanguages(mResponse.getLanguages());
            mDatabaseSync.SyncSecureItemTypes(mResponse.getSecureItemTypes());
            mDatabaseSync.SyncSitesAndRecommendedSites(mResponse.getRecommendedSites(), true);
            mDatabaseSync.SyncSitesAndRecommendedSites(mResponse.getSites(), false);
            mDatabaseSync.SyncShare(mResponse.getShares());
            postProgress(51); // requirement to change label text at UpdateActivity
            mDatabaseSync.SyncStorageRegions(mResponse.getStorageRegions());
            if (null != mResponse.getAccount()) {
                mDatabaseSync.SyncStorageBackup(mResponse.getAccount().isSynchronize());
            }
            if (null != mResponse.getSiteDomains()) {
                mDatabaseSync.SyncSiteDomains(mResponse.getSiteDomains());
            }
            mDatabaseSync.SyncUserInfo(mResponse.getAccount());
            if (status != DB_STATUS_NOTHING) {
                checkRsa();
            }
        } else {
            if (mResponse.getError().getCode() == 401) {
                status = DB_STATUS_ERROR_401;
            }
        }
        dailyAnalytics();
        return status;
    }

    private void uploadDatabase() {
        mDatabaseSync.GetSiteFromServer();
        if (mResponse == null || mResponse.getAccount() == null || !mResponse.getAccount().isSynchronize()) {
            return;
        }
        if (SubscriptionValidator.isFeatureValid(SubscriptionValidator.FEATURE_SYNCHRONIZE_DATA_ACROS_DEVICES, mContext, false)) {
            boolean result = mDatabaseSync.UploadDatabase(mFileHttpBean, mMasterPasswordWasChanged);
            Pref.setValue(mContext, Constants.LATEST_SYNC, result);
        }
    }
}
