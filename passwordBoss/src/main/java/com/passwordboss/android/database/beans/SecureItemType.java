package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.SECURE_ITEM_TYPE_TABLE_NAME)
public class SecureItemType {
    @DatabaseField(columnName = DatabaseConstants.ACTIVE,
            dataType = DataType.BOOLEAN, defaultValue = "true")
    private boolean active;
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(
            columnName = DatabaseConstants.NAME,
            dataType = DataType.STRING, id = true)
    private String name;
    @DatabaseField(columnName = DatabaseConstants.ORDER,
            dataType = DataType.INTEGER)
    private int order;
    @DatabaseField(
            columnName = DatabaseConstants.SECURE_ITEM_TYPE_STORAGE_FORMAT,
            dataType = DataType.STRING)
    private String storage_format;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING)
    private String uuid;

    public SecureItemType() {

    }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.last_modified_date = lastModifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStorageFormat() {
        return storage_format;
    }

    public void setStorageFormat(String storageFormat) {
        this.storage_format = storageFormat;
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
