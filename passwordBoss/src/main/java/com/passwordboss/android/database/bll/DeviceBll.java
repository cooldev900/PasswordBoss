package com.passwordboss.android.database.bll;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Device;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.exceptions.Exceptions;

public class DeviceBll extends BaseBll<Device, String> {

    public DeviceBll(DatabaseHelperSecure databaseHelper) throws SQLException {
        super(databaseHelper.getDeviceDao());
    }

    public static Observable<DeviceBll> getObservable() {
        return Observable.defer(() -> DatabaseHelperSecure.getObservable().map(helperSecure -> {
            try {
                return new DeviceBll(helperSecure);
            } catch (SQLException e) {
                throw Exceptions.propagate(e);
            }
        }));
    }

    public void deleteDevice(Device device) {
        device.setActive(false);
        updateRow(device);
    }

    public List<Device> getActiveDevices() throws SQLException {
        QueryBuilder<Device, String> queryBuilder = mDao.queryBuilder();
        Where<Device, String> where = queryBuilder.where();
        where.eq(DatabaseConstants.ACTIVE, true);
        PreparedQuery<Device> preparedQuery = queryBuilder.prepare();
        return mDao.query(preparedQuery);
    }

    /*
     * Return category by uuid
     * null in case of SQLException
    */
    public Device getDeviceByInstallationUuid(String uuid) {
        try {
            QueryBuilder<Device, String> queryBuilder = mDao.queryBuilder();
            Where<Device, String> where = queryBuilder.where();
            where.eq(DatabaseConstants.INSTALLATION, uuid)
                    .and().eq(DatabaseConstants.ACTIVE, true);
            PreparedQuery<Device> preparedQuery = queryBuilder.prepare();
            List<Device> devices = mDao.query(preparedQuery);
            if (devices.size() > 0) {
                return devices.get(0);
            }
        } catch (SQLException e) {
            ////Log.print(e);
        }
        return null;
    }
}