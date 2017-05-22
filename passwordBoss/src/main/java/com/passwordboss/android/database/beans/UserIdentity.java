package com.passwordboss.android.database.beans;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

@DatabaseTable(tableName = "user_identity")
public class UserIdentity extends Base {
    public static final String COLUMN_DEFAULT = "default";
    @DatabaseField(columnName = "address_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mAddress;
    @DatabaseField(columnName = "avatar",
            dataType = DataType.STRING)
    private String mAvatar;
    @DatabaseField(columnName = "bank_account_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mBankAccount;
    @DatabaseField(columnName = "company_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mCompany;
    @DatabaseField(columnName = "credit_card_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mCreditCard;
    @DatabaseField(columnName = COLUMN_DEFAULT,
            dataType = DataType.BOOLEAN)
    private boolean mDefault;
    @DatabaseField(columnName = "email_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mEmail;
    @DatabaseField(columnName = "hash",
            dataType = DataType.STRING)
    private String mHash;
    @DatabaseField(columnName = DatabaseConstants.ID,
            dataType = DataType.STRING,
            id = true)
    private String mId;
    @DatabaseField(columnName = "identity_name",
            dataType = DataType.STRING)
    private String mIdentityName;
    @DatabaseField(columnName = "name_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mName;
    @DatabaseField(columnName = "phone_number_id",
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem mPhoneNumber;

    public UserIdentity() {
    }

    public void calculateHash() {
        String data = "";
        data += getId() + getOrder() + getActive();
        setHash(Utils.getMD5(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdentity that = (UserIdentity) o;
        return mDefault == that.mDefault &&
                Objects.equal(mAddress, that.mAddress) &&
                Objects.equal(mAvatar, that.mAvatar) &&
                Objects.equal(mBankAccount, that.mBankAccount) &&
                Objects.equal(mCompany, that.mCompany) &&
                Objects.equal(mCreditCard, that.mCreditCard) &&
                Objects.equal(mEmail, that.mEmail) &&
                Objects.equal(mHash, that.mHash) &&
                Objects.equal(mId, that.mId) &&
                Objects.equal(mIdentityName, that.mIdentityName) &&
                Objects.equal(mName, that.mName) &&
                Objects.equal(mPhoneNumber, that.mPhoneNumber);
    }

    public SecureItem getAddress() {
        return mAddress;
    }

    public void setAddress(SecureItem mAddress) {
        this.mAddress = mAddress;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public SecureItem getBankAccount() {
        return mBankAccount;
    }

    public void setBankAccount(SecureItem mBankAccount) {
        this.mBankAccount = mBankAccount;
    }

    public SecureItem getCompany() {
        return mCompany;
    }

    public void setCompany(SecureItem mCompany) {
        this.mCompany = mCompany;
    }

    public SecureItem getCreditCard() {
        return mCreditCard;
    }

    public void setCreditCard(SecureItem mCreditCard) {
        this.mCreditCard = mCreditCard;
    }

    public SecureItem getEmail() {
        return mEmail;
    }

    public void setEmail(SecureItem mEmail) {
        this.mEmail = mEmail;
    }

    public String getHash() {
        return mHash;
    }

    public void setHash(String mHash) {
        this.mHash = mHash;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getIdentityName() {
        return mIdentityName;
    }

    public void setIdentityName(String mIdentityName) {
        this.mIdentityName = mIdentityName;
    }

    public int getItemsCount() {
        int result = 0;
        if (null != mName) result++;
        if (null != mAddress) result++;
        if (null != mPhoneNumber) result++;
        if (null != mEmail) result++;
        if (null != mCompany) result++;
        if (null != mCreditCard) result++;
        if (null != mBankAccount) result++;
        return result;
    }

    public SecureItem getName() {
        return mName;
    }

    public void setName(SecureItem mName) {
        this.mName = mName;
    }

    public SecureItem getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(SecureItem phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mAddress, mAvatar, mBankAccount, mCompany, mCreditCard, mDefault, mEmail, mHash, mId, mIdentityName, mName, mPhoneNumber);
    }

    public boolean isDefault() {
        return mDefault;
    }

    public void setDefault(boolean mDefault) {
        this.mDefault = mDefault;
    }

    public boolean isNew() {
        return Strings.isNullOrEmpty(mId);
    }

    public void setCreatedDateNow() {
        setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
    }

    public void setLastModifiedDateNow() {
        setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
    }
}
