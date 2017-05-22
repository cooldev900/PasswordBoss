package com.passwordboss.android.database.beans;

import java.util.Arrays;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;
import com.passwordboss.android.utils.Utils;

@DatabaseTable(tableName = DatabaseConstants.USER_INFO_TABLE_NAME)
public class UserInfo {

	@DatabaseField(columnName = DatabaseConstants.EMAIL, 
			dataType = DataType.STRING, id = true)
	private String email;
	
	@DatabaseField(columnName = DatabaseConstants.SUBSCRIPTION, 
			dataType = DataType.STRING)
	private String subscription;
	
	@DatabaseField(
			columnName = DatabaseConstants.STORAGE_REGION_UUID, 
			foreign = true, foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.UUID)
	private StorageRegion storage_region_uuid;

	@DatabaseField(columnName = DatabaseConstants.FIRST_NAME, 
			dataType = DataType.STRING, canBeNull = true)
	private String first_name;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_NAME, 
			dataType = DataType.STRING, canBeNull = true)
	private String last_name;

	@DatabaseField(columnName = DatabaseConstants.PUBLICKEY, 
			dataType = DataType.BYTE_ARRAY, canBeNull = true)
	private byte[] public_key;

	@DatabaseField(columnName = DatabaseConstants.PRIVATEKEY, 
			dataType = DataType.BYTE_ARRAY, canBeNull = true)
	private byte[] private_key;

	@DatabaseField(columnName = DatabaseConstants.HASH, 
			dataType = DataType.STRING, canBeNull = true)
	private String hash;

	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING)
	private String last_modified_date;
	
	
	@DatabaseField(
			columnName = DatabaseConstants.MULTI_FACTOR_AUTHENTICATION, 
			dataType = DataType.BOOLEAN, defaultValue = "false")
	private boolean multi_factor_authentication;
	
	@DatabaseField(columnName = DatabaseConstants.PHONE, 
			dataType = DataType.STRING)
	private String phone;
	
	public UserInfo() {
	
	}
	
	public UserInfo(String email, String created_date, 
			String last_modified_date) {
		super();
		this.email = email;
		this.created_date = created_date;
		this.last_modified_date = last_modified_date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	
	public String getLastName() {
		return last_name;
	}

	public void setLastName(String lastName) {
		this.last_name = lastName;
	}

	public byte[] getPublicKey() {
		return public_key;
	}

	public void setPublicKey(byte[] publicKey) {
		this.public_key = publicKey;
	}

	public byte[] getPrivateKey() {
		return private_key;
	}

	public void setPrivateKey(byte[] privateKey) {
		this.private_key = privateKey;
	}

	public String getHash() {
		return hash;	
	}

	public void setHash(String hash) {
		this.hash = hash;
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
	
	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public StorageRegion getStorageRegionUuid() {
		return storage_region_uuid;
	}

	public void setStorageRegionUuid(StorageRegion storageRegionUuid) {
		this.storage_region_uuid = storageRegionUuid;
	}
	
	public boolean isMultiFactorAuthentication() {
		return multi_factor_authentication;
	}

	public void setMultiFactorAuthentication(boolean multi_factor_authentication) {
		this.multi_factor_authentication = multi_factor_authentication;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void calculateHash() {
		String data = "";
		String storageRegionUuid = null;
		if (getStorageRegionUuid() != null && 
			getStorageRegionUuid().getUuid() != null)  {
			storageRegionUuid = getStorageRegionUuid().getUuid();
		}
		data+= getEmail() + getFirstName() + getLastName() + storageRegionUuid + getSubscription();
		setHash(Utils.getMD5(data));
	}

	@Override
	public String toString() {
		return "UserInfo [email=" + email + ", subscription=" + subscription
				+ ", storage_region_uuid=" + storage_region_uuid
				+ ", first_name=" + first_name + ", last_name=" + last_name
				+ ", public_key=" + Arrays.toString(public_key)
				+ ", private_key=" + Arrays.toString(private_key) + ", hash="
				+ hash + ", created_date=" + created_date
				+ ", last_modified_date=" + last_modified_date
				+ ", multi_factor_authentication="
				+ multi_factor_authentication + ", phone=" + phone + "]";
	}
	
	

}
