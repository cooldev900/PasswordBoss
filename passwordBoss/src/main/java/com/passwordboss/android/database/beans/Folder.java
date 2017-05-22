package com.passwordboss.android.database.beans;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = "folder")
public class Folder extends Base implements Comparable<Folder> {

    @DatabaseField(
            columnName = DatabaseConstants.HASH,
            dataType = DataType.STRING)
    private String hash;
    @DatabaseField(
            columnName = DatabaseConstants.ID,
            dataType = DataType.STRING, id = true)
    private String id;
    @DatabaseField(
            columnName = DatabaseConstants.NAME,
            dataType = DataType.STRING)
    private String name;
    @DatabaseField(columnName = "parent",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private Folder parent;

    public Folder() {
    }

    public void calculateHash() {
        String data = "";
        data += getName() + getOrder() + getActive();
        setHash(Utils.getMD5(data));
    }

    public int compareTo(@NonNull Folder other) {
        return this.name.compareTo(other.name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equal(hash, folder.hash) &&
                Objects.equal(id, folder.id) &&
                Objects.equal(name, folder.name) &&
                Objects.equal(parent, folder.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash, id, name, parent);
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

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public boolean isNew() {
        return Strings.isNullOrEmpty(id);
    }
}
