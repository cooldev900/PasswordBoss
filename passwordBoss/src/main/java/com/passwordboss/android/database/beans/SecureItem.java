package com.passwordboss.android.database.beans;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.logback.AppError;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = "secure_item")
public class SecureItem extends Base {

    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_SECURE_ITEM_TYPE_NAME = "secure_item_type_name";
    public static final String COLUMN_TYPE = "type";


    @DatabaseField(
            columnName = "access_count",
            dataType = DataType.INTEGER)
    private int access_count;
    @DatabaseField(
            columnName = "color",
            dataType = DataType.STRING)
    private String color;
    @DatabaseField(
            columnName = "data",
            dataType = DataType.STRING)
    private String data;
    @DatabaseField(columnName = "favorite",
            dataType = DataType.BOOLEAN,
            defaultValue = "false")
    private boolean favorite;
    @DatabaseField(
            columnName = "folder_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private Folder folder;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(
            columnName = DatabaseConstants.ID,
            dataType = DataType.STRING, id = true)
    private String id;
    @DatabaseField(columnName = "last_access",
            dataType = DataType.STRING)
    private String last_access;
    @DatabaseField(
            columnName = "login_url",
            dataType = DataType.STRING)
    private String login_url;
    @DatabaseField(
            columnName = DatabaseConstants.NAME,
            dataType = DataType.STRING)
    private String name;
    // field was excluded form hashCode and equals methods
    // they are used to determine was entity changed or not during edit
    @ForeignCollectionField(eager = true)
    private ForeignCollection<SecureItemData> secureItemData;
    @DatabaseField(
            columnName = COLUMN_SECURE_ITEM_TYPE_NAME,
            dataType = DataType.STRING)
    private String secure_item_type;
    @DatabaseField(columnName = DatabaseConstants.SHARE,
            dataType = DataType.BOOLEAN,
            defaultValue = "false")
    private boolean share;
    @DatabaseField(
            columnName = COLUMN_SITE_ID,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private Site site;
    @DatabaseField(
            columnName = COLUMN_TYPE,
            dataType = DataType.STRING)
    private String type;
    @DatabaseField(columnName = "verified",
            dataType = DataType.BOOLEAN,
            defaultValue = "false")
    private boolean verified;

    public SecureItem() {

    }

    public void calculateHash() {
        String data = "";
        data += getData() + getName() +
                getSecureItemTypeName() + getSiteId() + getUuid() +
                isFavorite() + getColor() + getType() + isShare() +
                getOrder() + getActive() + isVerified() + getLoginUrl() + getAccessCount();
        setHash(Utils.getMD5(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecureItem that = (SecureItem) o;
        return access_count == that.access_count &&
                favorite == that.favorite &&
                share == that.share &&
                verified == that.verified &&
                Objects.equal(color, that.color) &&
                Objects.equal(data, that.data) &&
                Objects.equal(folder, that.folder) &&
                Objects.equal(hash, that.hash) &&
                Objects.equal(id, that.id) &&
                Objects.equal(last_access, that.last_access) &&
                Objects.equal(login_url, that.login_url) &&
                Objects.equal(name, that.name) &&
                Objects.equal(secure_item_type, that.secure_item_type) &&
                Objects.equal(site, that.site) &&
                Objects.equal(type, that.type);
    }

    public int getAccessCount() {
        return access_count;
    }

    @ColorInt
    @Nullable
    public Integer getColor() {
        //noinspection ResourceAsColor
        if (null == color) return null;
        try {
            return Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            new AppError(e).log(getClass());
            return null;
        }
    }

    public void setColor(Integer color) {
        this.color = null == color ? null : String.format("#%06X", (0xFFFFFF & color)); // to store at format #RRGGBB
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folderId) {
        this.folder = folderId;
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

    public String getLoginUrl() {
        return login_url;
    }

    public void setLoginUrl(String login_url) {
        this.login_url = login_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignCollection<SecureItemData> getSecureItemData() {
        return secureItemData;
    }

    public String getSecureItemTypeName() {
        return secure_item_type;
    }

    @Nullable
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Nullable
    public String getSiteId() {
        return null == site ? null : site.getId();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(access_count, color, data, favorite, folder, hash, id, last_access, login_url, name, secure_item_type, share, site, type, verified);
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isNew() {
        return Strings.isNullOrEmpty(id);
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setItemType(@Nullable ItemType itemType) {
        secure_item_type = ItemType.toSecureItemType(itemType);
        type = ItemType.toType(itemType);
    }

}
