package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.FEATURE_GROUP_FEATURE_TABLE_NAME)
public class FeatureGroupFeature {

    @DatabaseField(columnName = DatabaseConstants.ACTIVE,
            dataType = DataType.BOOLEAN, defaultValue = "true")
    private boolean active;
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(
            columnName = DatabaseConstants.FEATURE_GROUP_FEATURE_FEATURE_GROUP,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.UUID)
    private FeatureGroup feature_group_uuid;
    @DatabaseField(
            columnName = DatabaseConstants.FEATURE_GROUP_FEATURE_FEATURE,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.UUID)
    private Feature feature_uuid;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING, id = true)
    private String uuid;

    public FeatureGroupFeature() {

    }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }

    public FeatureGroup getFeatureGroupUuid() {
        return feature_group_uuid;
    }

    public void setFeatureGroupUuid(FeatureGroup featureGroupUuid) {
        this.feature_group_uuid = featureGroupUuid;
    }

    public Feature getFeatureUuid() {
        return feature_uuid;
    }

    public void setFeatureUuid(Feature featureUuid) {
        this.feature_uuid = featureUuid;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.last_modified_date = lastModifiedDate;
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
