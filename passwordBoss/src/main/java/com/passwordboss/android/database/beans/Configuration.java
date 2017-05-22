package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.CONFIGURATION_TABLE_NAME)
public class Configuration extends Base {
    @DatabaseField(
            columnName = DatabaseConstants.ACCOUNT_EMAIL,
            dataType = DataType.STRING,
            uniqueCombo = true)
    private String account_e_mail;
    @DatabaseField(
            columnName = DatabaseConstants.KEY,
            dataType = DataType.STRING,
            uniqueCombo = true)
    private String key;
    @DatabaseField(
            columnName = DatabaseConstants.VALUE,
            dataType = DataType.STRING)
    private String value;

    public Configuration() {
    }

    public boolean valueEquals(String string) {
        return null == value && null == string
                || null != string && string.equals(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAccountEmail(String accountEmail) {
        this.account_e_mail = accountEmail;
    }
}
