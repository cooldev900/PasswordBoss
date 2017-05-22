package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.DEVICES_TABLE_NAME)
public class Device {

    public static final String OS_ANDROID = "Android";
    public static final String OS_IOS = "iOS";
    public static final String OS_WINDOWS = "Windows";

    @DatabaseField(columnName = DatabaseConstants.ACTIVE,
            dataType = DataType.BOOLEAN, defaultValue = "true")
    private boolean active;
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(columnName = DatabaseConstants.DEVICES_CATEGORY,
            dataType = DataType.STRING)
    private String device_category;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(columnName = DatabaseConstants.INSTALLATION,
            dataType = DataType.STRING, id = true)
    private String installation_id;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(columnName = DatabaseConstants.LATEST_SYNC,
            dataType = DataType.STRING)
    private String latest_sync;
    @DatabaseField(columnName = DatabaseConstants.MOBILENO,
            dataType = DataType.INTEGER)
    private int mobile_no;
    @DatabaseField(columnName = DatabaseConstants.NICKNAME,
            dataType = DataType.STRING)
    private String nickname;
    @DatabaseField(columnName = DatabaseConstants.DEVICES_OS,
            dataType = DataType.STRING)
    private String os;
    @DatabaseField(columnName = DatabaseConstants.PIN_NUMBER,
            dataType = DataType.INTEGER)
    private int pin_number;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING)
    private String uuid;


    public Device() {

    }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }

    public String getDeviceCategory() {
        return device_category;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.device_category = deviceCategory;
    }

    public String getInstallationId() {
        return installation_id;
    }

    public void setInstallationId(String installationId) {
        this.installation_id = installationId;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.last_modified_date = lastModifiedDate;
    }

    public String getLatestSync() {
        return latest_sync;
    }

    public void setLatestSync(String latestSync) {
        this.latest_sync = latestSync;
    }

    public int getMobileNo() {
        return mobile_no;
    }

    public void setMobileNo(int mobileNo) {
        this.mobile_no = mobileNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getPinNumber() {
        return pin_number;
    }

    public void setPinNumber(int pinNumber) {
        this.pin_number = pinNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
