package com.passwordboss.android.pin;

import android.content.Context;

import com.passwordboss.android.app.Installation;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.bll.ConfigurationBll;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.logback.AppSqlError;
import com.passwordboss.android.utils.Pref;

import java.sql.SQLException;

public class PinStoreOld implements PinStore {

    private Context mContext;
    private DeviceBll mDeviceBll;

    public PinStoreOld(Context context, DeviceBll deviceBll) {
        mDeviceBll = deviceBll;
        mContext = context;
    }

    @Override
    public void clear() {
        set("-1");
    }

    @Override
    public void disable() {
        try {
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext));
            configurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ENABLE_PIN, "0");
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
    }

    @Override
    public void enable() {
        try {
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext));
            configurationBll.updateOrInsertItem(Pref.EMAIL, DatabaseConstants.ENABLE_PIN, "1");
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
    }

    @Override
    public boolean isEnabled() {
        try {
            ConfigurationBll configurationBll = new ConfigurationBll(DatabaseHelperNonSecure.getHelper(mContext));
            return configurationBll.valueEquals(Pref.EMAIL, DatabaseConstants.ENABLE_PIN, "1");
        } catch (SQLException e) {
            new AppSqlError(e).log(getClass());
        }
        return false;
    }

    @Override
    public boolean isValid(String pin) {
        Device device = mDeviceBll.getDeviceByInstallationUuid(new Installation().getUuid());
        int pinNumber = null == device ? -1 : device.getPinNumber();
        return pinNumber == Integer.parseInt(pin);
    }

    @Override
    public void set(String pin) {
        Device device = mDeviceBll.getDeviceByInstallationUuid(new Installation().getUuid());
        if (device != null) {
            device.setPinNumber(Integer.parseInt(pin));
            mDeviceBll.updateRow(device);
        }
    }
}
