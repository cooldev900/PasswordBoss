package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = "secure_item_share")
public class SecureItemShare {
    @DatabaseField(columnName = "hash",
            dataType = DataType.STRING)
    private String mHash;
    @DatabaseField(columnName = "id",
            dataType = DataType.STRING,
            id = true)
    private String mId;
    @DatabaseField(
            columnName = "secure_item_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mSecureItem;
    @DatabaseField(
            columnName = "share_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private Share mShare;

}
