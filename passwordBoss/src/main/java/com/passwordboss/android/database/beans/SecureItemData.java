package com.passwordboss.android.database.beans;

import com.google.common.base.Objects;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;

@DatabaseTable(tableName = "secure_item_data")
public class SecureItemData implements Serializable {
    public static final String COLUMN_IDENTIFIER = "identifier";
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
    @DatabaseField(
            columnName = SecureItemData.COLUMN_IDENTIFIER,
            dataType = DataType.STRING,
            id = true)
    private String identifier;
    @DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE,
            dataType = DataType.STRING)
    private String last_modified_date;
    @DatabaseField(columnName = DatabaseConstants.ORDER,
            dataType = DataType.INTEGER)
    private int order;
    @DatabaseField(
            columnName = DatabaseConstants.SECURE_ITEM_ID,
            foreign = true,
            foreignAutoRefresh = true,
            foreignColumnName = DatabaseConstants.ID)
    private SecureItem secure_item_id;
    @DatabaseField(
            columnName = "sys_value",
            dataType = DataType.STRING)
    private String sys_value;
    @DatabaseField(
            columnName = "value",
            dataType = DataType.STRING)
    private String value;

    public SecureItemData() {

    }

    public void calculateHash() {
        String data = "";
        data += getSecureItemId().getId() + getIdentifier() +
                getValue() + getSysValue() + getOrder() + isActive();
        setHash(Utils.getMD5(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecureItemData itemData = (SecureItemData) o;
        return active == itemData.active &&
                order == itemData.order &&
                Objects.equal(created_date, itemData.created_date) &&
                Objects.equal(hash, itemData.hash) &&
                Objects.equal(identifier, itemData.identifier) &&
                Objects.equal(last_modified_date, itemData.last_modified_date) &&
                Objects.equal(secure_item_id, itemData.secure_item_id) &&
                Objects.equal(sys_value, itemData.sys_value) &&
                Objects.equal(value, itemData.value);
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public SecureItem getSecureItemId() {
        return secure_item_id;
    }

    public void setSecureItemId(SecureItem secureItemId) {
        this.secure_item_id = secureItemId;
    }

    public String getSysValue() {
        return sys_value;
    }

    public void setSysValue(String sysValue) {
        this.sys_value = sysValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(active, created_date, hash, identifier, last_modified_date, order, secure_item_id, sys_value, value);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCreatedDateNow() {
        setCreatedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
    }

    public void setLastModifiedDateNow() {
        setLastModifiedDate(DateTime.now(DateTimeZone.UTC).toDateTimeISO().toString());
    }

    @Override
    public String toString() {

        return "SecureItemData [secure_item_id=" + secure_item_id
                + ", identifier=" + identifier + ", value=" + value + ", hash="
                + hash + ", sys_value=" + sys_value + ", created_date="
                + created_date + ", last_modified_date=" + last_modified_date
                + ", active=" + active + ", order=" + order + "]";
    }

    public interface Identifier {
        String ACCOUNT_NUMBER = "accountNumber";
        String ADDRESS1 = "address1";
        String ADDRESS2 = "address2";
        String AGENT = "agent";
        String AGENT_PHONE_NUMBER = "agent_phone_number";
        String AIRLINE = "airline";
        String ALARM_CODE = "alarm_code";
        String ALARM_COMPANY = "alarm_company";
        String APPLICATION = "application";
        String ATTORNEY = "attorney";
        String AUTHENTICATION = "authentication";
        String AUTOLOGIN = "autologin";
        String BANK_NAME = "bank_name";
        String BANK_PHONE = "bank_phone";
        String BIT_STRENGTH = "bit_strength";
        String CARD_NUMBER = "cardNumber";
        String CARD_TYPE = "cardType";
        String CITY = "city";
        String COUNTRY = "country";
        String DATABASE = "database";
        String DATE_OF_BIRTH = "dateOfBirth";
        String DEDUCTIBLE = "deductible";
        String DOCTOR = "doctor";
        String DOCTOR_PHONE = "doctor_phone";
        String DRIVER_LICENSE_NUMBER = "driverLicenseNumber";
        String EMAIL = "email";
        String ENCRYPTION = "encryption";
        String EXECUTOR = "executor";
        String EXPIRES = "expires";
        String EXPIRES_MONTH = "expires_month";
        String EXPIRES_YEAR = "expires_year";
        String FIPS_MODE = "fips_mode";
        String FIRST_NAME = "firstName";
        String FORMAT = "format";
        String FREQUENT_FLYER_NUMBER = "frequent_flyer_number";
        String FREQUENT_FLYER_PHONE = "frequent_flyer_phone";
        String GROUP_NUMBER = "group_number";
        String HOTEL = "hotel";
        String IBAN = "iban";
        String INSURANCE_COMPANY = "insurance_company";
        String ISSUE_DATE = "issueDate";
        String ISSUING_BANK = "issuingBank";
        String KEY_TYPE = "key_type";
        String LAST_NAME = "lastName";
        String LICENSE_KEY = "license_key";
        String LOCATION_OF_DOCUMENTS = "location_of_documents";
        String MEDICINE = "medicine";
        String MEMBER_ID = "member_id";
        String MEMBERSHIP_NUMBER = "membership_number";
        String MIDDLE_NAME = "middleName";
        String NAME_ON_ACCOUNT = "nameOnAccount";
        String NAME_ON_CARD = "nameOnCard";
        String NATIONALITY = "nationality";
        String NOTES = "notes";
        String NUMBER_OF_LICENSES = "number_of_licenses";
        String ORDER_NUMBER = "order_number";
        String PASSPHRASE = "passphrase";
        String PASSPORT_NUMBER = "passportNumber";
        String PASSWORD = "password";
        String PHARMACY = "pharmacy";
        String PHARMACY_PHONE = "pharmacy_phone";
        String PHONE_CODE = "phone_code";
        String PHONE_NUMBER = "phoneNumber";
        String PIN = "pin";
        String PLACE_OF_ISSUE = "placeOfIssue";
        String POLICY_NUMBER = "policy_number";
        String PORT = "port";
        String PRESCRIPTION_NUMBER = "prescription_number";
        String PRESCRIPTION_PLAN = "prescription_plan";
        String PRICE = "price";
        String PRIVATE_KEY = "private_key";
        String PUBLIC_KEY = "public_key";
        String PUBLISHER = "publisher";
        String PURCHASE_DATE = "purchase_date";
        String REFILLS = "refills";
        String RENEWAL_DATE = "renewal_date";
        String ROUTING_NUMBER = "routingNumber";
        String SECURITY_CODE = "security_code";
        String SERVER_ADDRESS = "server_address";
        String SMPT_SERVER = "smtp_server";
        String SOCIAL_SECURITY_NUMBER = "ssn";
        String SSID = "ssid";
        String STATE = "state";
        String STATUS_LEVEL = "status_level";
        String SUB_DOMAIN = "sub_domain";
        String SUPPORT_THROUGH = "support_through";
        String SWIFT = "swift";
        String TRUSTEE = "trustee";
        String TYPE = "type";
        String USE_SECURE_BROWSER = "use_secure_browser";
        String USERNAME = "username";
        String VERSION = "version";
        String ZIP_CODE = "zipCode";
    }


}