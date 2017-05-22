package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = "site_uri")
public class SiteUri {

    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_URI = "uri";
    @DatabaseField(columnName = DatabaseConstants.ACTIVE,
            dataType = DataType.BOOLEAN, defaultValue = "true")
    private boolean active;
    @DatabaseField(
            columnName = "behavior",
            dataType = DataType.STRING)
    private String behavior;
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(columnName = "host",
            dataType = DataType.STRING)
    private String host;
    @DatabaseField(columnName = "is_trivial",
            dataType = DataType.BOOLEAN)
    private boolean is_trivial;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(columnName = "lookup_method",
            dataType = DataType.STRING)
    private String lookup_method;
    @DatabaseField(
            columnName = SiteUri.COLUMN_SITE_ID,
            foreign = true, foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private Site site_id;
    @DatabaseField(
            columnName = "type",
            dataType = DataType.STRING)
    private String type;
    @DatabaseField(
            columnName = SiteUri.COLUMN_URI,
            dataType = DataType.STRING)
    private String uri;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING, id = true)
    private String uuid;
    @DatabaseField(
            columnName = "verified_count",
            dataType = DataType.INTEGER)
    private int verified_count;


    public SiteUri() {

    }

    public void calculateHash() {
        String data = "";
        String siteId = null;

        if (getSiteId() != null && getSiteId().getId() != null) {
            siteId = getSiteId().getId();
        }

        data += getUri() + getBehavior() + getVerifiedCount() + isIsTrivial()
                + getHost() + getLookupMethod() + getType() + siteId + getUuid() + isActive();
        setHash(Utils.getMD5(data));
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.last_modified_date = lastModifiedDate;
    }

    public String getLookupMethod() {
        return lookup_method;
    }

    public void setLookupMethod(String lookup_method) {
        this.lookup_method = lookup_method;
    }

    public Site getSiteId() {
        return site_id;
    }

    public void setSiteId(Site siteId) {
        this.site_id = siteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getVerifiedCount() {
        return verified_count;
    }

    public void setVerifiedCount(int verifiedCount) {
        this.verified_count = verifiedCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isIsTrivial() {
        return is_trivial;
    }

    public void setIsTrivial(boolean isTrivial) {
        this.is_trivial = isTrivial;
    }

}
