package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = "site")
public class Site extends Base {
    private static final String BLACKLIST_PAGE = "page";
    public static final String COLUMN_RECOMMENDED = "recommended";
    @DatabaseField(columnName = "apc_script_id",
            dataType = DataType.STRING)
    private String apc_script_id;
    @DatabaseField(columnName = "apc_type",
            dataType = DataType.STRING)
    private String apc_type;
    @DatabaseField(columnName = "blacklist",
            dataType = DataType.STRING)
    private String blacklist;
    @DatabaseField(
            columnName = "friendly_name",
            dataType = DataType.STRING)
    private String friendly_name;
    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(
            columnName = DatabaseConstants.ID,
            dataType = DataType.STRING,
            id = true)
    private String id;
    @DatabaseField(
            columnName = "multi_step_login",
            dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean multi_step_login;
    @DatabaseField(
            columnName = DatabaseConstants.NAME,
            dataType = DataType.STRING)
    private String name;
    @DatabaseField(
            columnName = "realm",
            dataType = DataType.STRING)
    private String realm;
    @DatabaseField(
            columnName = COLUMN_RECOMMENDED,
            dataType = DataType.BOOLEAN)
    private boolean recommended;
    @DatabaseField(
            columnName = "sent_status",
            dataType = DataType.STRING, defaultValue = "pending_send")
    private String sent_status;
    @DatabaseField(
            columnName = "validated",
            dataType = DataType.BOOLEAN)
    private boolean validated;


    public Site() {

    }

    public void calculateHash() {
        String data = "";
        data += getUuid();
        data += getName() + getFriendlyName() + isValidated() + isRecommended() + getOrder() + getActive() +
                getRealm() + getSentStatus();
        setHash(Utils.getMD5(data));
    }

    public String getFriendlyName() {
        return friendly_name;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendly_name = friendlyName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealm() {
        return realm;
    }

    public String getSentStatus() {
        return sent_status;
    }

    public boolean isBlacklistPage() {
        return BLACKLIST_PAGE.equals(blacklist);
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setBlacklistPage() {
        blacklist = BLACKLIST_PAGE;
    }

}
