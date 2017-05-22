package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = "secure_item_stats")
public class SecureItemStats {

    public static final String COLUMN_SECURE_ITEM_ID = "secure_item_id";
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(
            columnName = "do_not_show",
            dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean do_not_show;
    @DatabaseField(
            columnName = "has_duplicate",
            dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean has_duplicate;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(
            columnName = "is_weak",
            dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean is_weak;
    @DatabaseField(
            columnName = "last_alert",
            dataType = DataType.STRING)
    private String last_alert;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(
            columnName = "last_password_change",
            dataType = DataType.STRING)
    private String last_password_change;
    @DatabaseField(
            columnName = COLUMN_SECURE_ITEM_ID,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem secure_item_id;

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }

    public void setLastAlert(String last_alert) {
        this.last_alert = last_alert;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.last_modified_date = lastModifiedDate;
    }

    public String getLastPasswordChange() {
        return last_password_change;
    }

    public void setLastPasswordChange(String last_password_change) {
        this.last_password_change = last_password_change;
    }

    public SecureItem getSecureItemId() {
        return secure_item_id;
    }

    public void setSecureItemId(SecureItem secure_item_id) {
        this.secure_item_id = secure_item_id;
    }

    public void setDoNotShow(boolean do_not_show) {
        this.do_not_show = do_not_show;
    }

    public void setHasDuplicate(boolean has_duplicate) {
        this.has_duplicate = has_duplicate;
    }

    public boolean isIsWeak() {
        return is_weak;
    }

    public void setIsWeak(boolean is_weak) {
        this.is_weak = is_weak;
    }
}
