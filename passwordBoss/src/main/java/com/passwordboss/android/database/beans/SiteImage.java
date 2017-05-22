package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = "site_image")
public class SiteImage {

    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_IMAGE_SIZE = "size_id";
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
    @DatabaseField(columnName = DatabaseConstants.ID,
            dataType = DataType.STRING, id = true)
    private String id;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(
            columnName = SiteImage.COLUMN_SITE_ID,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private Site site_id;
    @DatabaseField(
            columnName = COLUMN_IMAGE_SIZE,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SiteImageSize size_id;
    @DatabaseField(
            columnName = DatabaseConstants.URL,
            dataType = DataType.STRING)
    private String url;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING)
    private String uuid;

    public SiteImage() {

    }

    public void calculateHash() {
        String data = "";
        String siteId = null;
        String sizeId = null;
        if (getSiteId() != null && getSiteId().getId() != null) {
            siteId = getSiteId().getId();
        }
        if (getSizeId() != null && getSizeId().getId() != null) {
            sizeId = getSizeId().getId();
        }
        data += getUuid() + siteId + sizeId + getUrl() + isActive();
        setHash(Utils.getMD5(data));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.last_modified_date = lastModifiedDate;
    }

    public Site getSiteId() {
        return site_id;
    }

    public void setSiteId(Site siteId) {
        this.site_id = siteId;
    }

    public SiteImageSize getSizeId() {
        return size_id;
    }

    public void setSizeId(SiteImageSize sizeId) {
        this.size_id = sizeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
