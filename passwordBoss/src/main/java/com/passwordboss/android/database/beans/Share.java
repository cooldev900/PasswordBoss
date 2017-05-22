package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = "share")
public class Share {
    public static final String COLUMN_SECURE_ITEM_TYPE_NAME = "secure_item_type_name";
    public static final String COLUMN_RECEIVER = "receiver";
    public static final String COLUMN_SENDER = "sender";
    public static final String COLUMN_STATUS = "status";
    @DatabaseField(columnName = DatabaseConstants.ACTIVE,
            dataType = DataType.BOOLEAN,
            defaultValue = "true")
    private boolean active;
    @DatabaseField(columnName = DatabaseConstants.CREATED_DATE,
            dataType = DataType.STRING)
    private String created_date;
    @DatabaseField(
            columnName = "data",
            dataType = DataType.STRING)
    private String data;
    @DatabaseField(
            columnName = "expiration_date",
            dataType = DataType.STRING)
    private String expiration_date;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(
            columnName = DatabaseConstants.ID,
            dataType = DataType.STRING, id = true)
    private String id;
    @DatabaseField(columnName = "last_alert",
            dataType = DataType.STRING)
    private String last_alert;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(
            columnName = "message",
            dataType = DataType.STRING)
    private String message;
    @DatabaseField(columnName = "nickname",
            dataType = DataType.STRING)
    private String nickname;
    @DatabaseField(columnName = "permission",
            dataType = DataType.STRING)
    private String permission;
    @DatabaseField(
            columnName = Share.COLUMN_RECEIVER,
            dataType = DataType.STRING)
    private String receiver;
    @DatabaseField(columnName = "receiver_pk",
            dataType = DataType.STRING, canBeNull = true)
    private String receiver_pk;
    @DatabaseField(
            columnName = COLUMN_SECURE_ITEM_TYPE_NAME,
            dataType = DataType.STRING)
    private String secure_item_type_name;
    @DatabaseField(
            columnName = Share.COLUMN_SENDER,
            dataType = DataType.STRING)
    private String sender;
    @DatabaseField(
            columnName = "share_items",
            dataType = DataType.STRING)
    private String share_items; // from Store_v17.txt: individual, folder, full
    @DatabaseField(
            columnName = "share_type",
            dataType = DataType.STRING)
    private String share_type; // from Store_v17.txt: standard, emergency_access
    @DatabaseField(
            columnName = COLUMN_STATUS,
            dataType = DataType.STRING)
    private String status;
    @DatabaseField(columnName = DatabaseConstants.UUID,
            dataType = DataType.STRING)
    private String uuid;
    @DatabaseField(columnName = "visible",
            dataType = DataType.BOOLEAN,
            defaultValue = "true")
    private boolean visible;
    @DatabaseField(columnName = "waiting_period",
            dataType = DataType.INTEGER)
    private int waiting_period;


    public Share() {

    }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExpirationDate() {
        return expiration_date;
    }

    public void setExpirationDate(String expirationDate) {
        this.expiration_date = expirationDate;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPk() {
        return receiver_pk;
    }

    public void setReceiverPk(String receiver_pk) {
        this.receiver_pk = receiver_pk;
    }

    public SecureItem getSecureItemId() {
        throw new UnsupportedOperationException();
    }

    public void setSecureItemId(SecureItem secureItemId) {
        throw new UnsupportedOperationException();
    }

    public String getSecureItemTypeName() {
        return secure_item_type_name;
    }

    public void setSecureItemTypeName(String secure_item_type_name) {
        this.secure_item_type_name = secure_item_type_name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setLastAlert(String last_alert) {
        this.last_alert = last_alert;
    }


}
