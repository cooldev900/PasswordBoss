package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.passwordboss.android.database.DatabaseConstants;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;

public class Base implements Serializable {
    @DatabaseField(columnName = DatabaseConstants.ACTIVE,
            dataType = DataType.BOOLEAN, defaultValue = "true")
    private boolean active;
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;

    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(columnName = DatabaseConstants.ORDER,
            dataType = DataType.INTEGER)
    private int order;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING)
    private String uuid;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCreatedDateNow() {
        setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
    }

    public void setLastModifiedDateNow() {
        setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
    }
}
