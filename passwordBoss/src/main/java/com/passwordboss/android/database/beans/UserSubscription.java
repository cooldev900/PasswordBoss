package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.USER_SUBSCRIPTION_TABLE_NAME)
public class UserSubscription {
    @DatabaseField(
            columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(
            columnName = DatabaseConstants.DESCRIPTION,
            dataType = DataType.STRING)
    private String description;
    @DatabaseField(
            columnName = DatabaseConstants.DURATION,
            dataType = DataType.INTEGER)
    private int duration;
    @DatabaseField(
            columnName = DatabaseConstants.EMAIL,
            dataType = DataType.STRING, id = true)
    private String email;
    @DatabaseField(
            columnName = DatabaseConstants.EXPIRATION_DATE,
            dataType = DataType.STRING)
    private String expiration_date;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(
            columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(
            columnName = DatabaseConstants.MULTIUSER,
            dataType = DataType.BOOLEAN)
    private boolean multiuser;

    @DatabaseField(
            columnName = DatabaseConstants.NAME,
            dataType = DataType.STRING)
    private String name;
    @DatabaseField(
            columnName = DatabaseConstants.NEXT_VALIDATION_DATE,
            dataType = DataType.STRING)
    private String next_validation_date;
    @DatabaseField(
            columnName = DatabaseConstants.SUBSCRIPTION_DATE,
            dataType = DataType.STRING)
    private String subscription_date;


    public UserSubscription() {
        super();
    }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String created_date) {
        this.created_date = created_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpirationDate() {
        return expiration_date;
    }

    public void setExpirationDate(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextValidationDate() {
        return next_validation_date;
    }

    public void setNextValidationDate(String next_validation_date) {
        this.next_validation_date = next_validation_date;
    }

    public String getSubscription_date() {
        return subscription_date;
    }

    public boolean isMultiuser() {
        return multiuser;
    }

    public void setMultiuser(boolean multiuser) {
        this.multiuser = multiuser;
    }

    public void setSubscriptionDate(String subscription_date) {
        this.subscription_date = subscription_date;
    }
}
