package com.passwordboss.android.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.passwordboss.android.constants.Constants;
import com.passwordboss.android.constants.JsonConstants;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.jsonbean.DateTimeTypeConverter;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;


public class SecureItemDataJSON {

	private String accountNumber;
	private String address1;
	private String address2;
	private String apt;
	private String bank;
	private String addressRef;
	private String cardNumber;
	private String cardType;
	private String city;
	private String company;
	private String country;
	private String cvv;
	private String dateOfBirth;
	private String email;
	private String expires;
	private String firstName;
	private String gender;
	private String iban;
	private String issueDate;
	private String issuingBank;
	private String lastName;
	private String memberID;
	private String middleName;
	private String nameOnAccount;
	private String nameOnCard;
	private String nationality;
	private String notes;
	private String password;
	private String phoneNumber;
	private String pin;
	private String placeOfIssue;
	private String routingNumber;
	private String state;
	private String swift;
	private String title;
	private String username;
	private String zipCode;
	private String driverLicenceNumber;
	private String ssn;
	private String passportNumber;
	private String siteUrl;
	private String login_url;
	private String nickname;
	private String color = "0";
	private String secureItemType;
	private String itemType;

	private boolean autologin, re_enter_password, sub_domain, use_secure_browser, password_visible_recipient;

	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getApt() {
		return apt;
	}

	public void setApt(String apt) {
		this.apt = apt;
	}

	public boolean getAutologin() {
		return autologin;
	}

	public void setAutologin(boolean autologin) {
		this.autologin = autologin;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAddressRef() {
		return addressRef;
	}

	public void setAddressRef(String addressRef) {
		this.addressRef = addressRef;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCVV() {
		return cvv;
	}

	public void setCVV(String cVV) {
		cvv = cVV;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssuingBank() {
		return issuingBank;
	}

	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNameOnAccount() {
		return nameOnAccount;
	}

	public void setNameOnAccount(String nameOnAccount) {
		this.nameOnAccount = nameOnAccount;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPIN() {
		return pin;
	}

	public void setPIN(String pIN) {
		pin = pIN;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public boolean getReEnterPassword() {
		return re_enter_password;
	}

	public void setReEnterPassword(boolean reEnterPassword) {
		this.re_enter_password = reEnterPassword;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean getSubDomain() {
		return sub_domain;
	}

	public void setSubDomain(boolean subDomain) {
		this.sub_domain = subDomain;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public boolean getUseSecureBrowser() {
		return use_secure_browser;
	}

	public void setUseSecureBrowser(boolean useSecureBrowser) {
		this.use_secure_browser = useSecureBrowser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
		
	public String getDriverLicenceNumber() {
		return driverLicenceNumber;
	}

	public void setDriverLicenceNumber(String driverLicenceNumber) {
		this.driverLicenceNumber = driverLicenceNumber;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	
	public boolean getPasswordVisibleRecipient() {
		return password_visible_recipient;
	}

	public void setPasswordVisibleRecipient(boolean password_visible_recipient) {
		this.password_visible_recipient = password_visible_recipient;
	}
	
	public String getLoginUrl() {
		return login_url;
	}

	public void setLoginUrl(String login_url) {
		this.login_url = login_url;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getSecureItemType() {
		return secureItemType;
	}
	public void setSecureItemType(String secureItemType) {
		this.secureItemType = secureItemType;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public static SecureItemDataJSON convertToObject(String json) {
		SecureItemDataJSON secureItemData = null;
		try {
			Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, 
					new DateTimeTypeConverter()).create();
			secureItemData = gson.fromJson(json, SecureItemDataJSON.class);
		} catch(Exception e) {
		}
		return secureItemData;
	}
	
	@Override
	public String toString() {
		String result = "{";
		
		if (getAccountNumber() != null && !"".equals(getAccountNumber())) {
			result += "\"" + JsonConstants.ACCOUNT_NUMBER
					+ "\":" + JSONObject.quote(getAccountNumber()) + ",";
		}
		
		if (getAddress1() != null && !"".equals(getAddress1())) {
			result += "\"" + JsonConstants.ADDRESS_ADDRESS1
					+ "\":" + JSONObject.quote(getAddress1()) + ",";
		}
		
		if (getAddress2() != null && !"".equals(getAddress2())) {
			result += "\"" + JsonConstants.ADDRESS_ADDRESS2
					+ "\":" + JSONObject.quote(getAddress2()) + ",";
		}
		
		if (getAddressRef() != null && !"".equals(getAddressRef())) {
			result += "\"" + JsonConstants.ADDRESS_REF
					+ "\":" + JSONObject.quote(getAddressRef()) + ",";
		}
		
		if (getApt() != null && !"".equals(getApt())) {
			result += "\"" + JsonConstants.APT
					+ "\":" + JSONObject.quote(getApt()) + ",";
		}
		
        result += "\"" + JsonConstants.AUTOLOGIN
                + "\":" + JSONObject.quote((getAutologin()? "1" : "0")) + ",";
        
		if (getBank() != null && !"".equals(getBank())) {
			result += "\"" + JsonConstants.BANK
					+ "\":" + JSONObject.quote(getBank()) + ",";
		}
		
		if (getCardNumber() != null && !"".equals(getCardNumber())) {
			result += "\"" + JsonConstants.CARD_NUMBER
					+ "\":" + JSONObject.quote(getCardNumber()) + ",";
		}
		
		if (getCardType() != null && !"".equals(getCardType())) {
			result += "\"" + JsonConstants.CARD_TYPE
					+ "\":" + JSONObject.quote(getCardType()) + ",";
		}
		
		if (getCity() != null && !"".equals(getCity())) {
			result += "\"" + JsonConstants.CITY
					+ "\":" + JSONObject.quote(getCity()) + ",";
		}
		
		if (getCompany() != null && !"".equals(getCompany())) {
			result += "\"" + JsonConstants.COMPANY
					+ "\":" + JSONObject.quote(getCompany()) + ",";
		}
		
		if (getCountry() != null && !"".equals(getCountry())) {
			result += "\"" + JsonConstants.COUNTRY
					+ "\":" + JSONObject.quote(getCountry()) + ",";
		}
		
		if (getCVV() != null && !"".equals(getCVV())) {
			result += "\"" + JsonConstants.CVV
					+ "\":" + JSONObject.quote(getCVV()) + ",";
		}
		
		if (getDateOfBirth() != null && !"".equals(getDateOfBirth())) {
			result += "\"" + JsonConstants.DATE_OF_BIRTH
					+ "\":" + JSONObject.quote(getDateOfBirth()) + ",";
		}
		
		if (getDriverLicenceNumber() != null && !"".equals(getDriverLicenceNumber())) {
			result += "\"" + JsonConstants.DRIVER_LICENCE_NUMBER
					+ "\":" + JSONObject.quote(getDriverLicenceNumber()) + ",";
		}
		
		if (getEmail() != null && !"".equals(getEmail())) {
			result += "\"" + JsonConstants.EMAIL
					+ "\":" + JSONObject.quote(getEmail()) + ",";
		}
		
		if (getExpires() != null && !"".equals(getExpires())) {
			result += "\"" + JsonConstants.EXPIRES
					+ "\":" + JSONObject.quote(getExpires()) + ",";
		}
		
		if (getFirstName() != null && !"".equals(getFirstName())) {
			result += "\"" + JsonConstants.FIRST_NAME
					+ "\":" + JSONObject.quote(getFirstName()) + ",";
		}
		
		if (getGender() != null && !"".equals(getGender())) {
			result += "\"" + JsonConstants.GENDER_DATA
					+ "\":" + JSONObject.quote(getGender()) + ",";
		}
		
		if (getIban() != null && !"".equals(getIban())) {
			result += "\"" + JsonConstants.IBAN
					+ "\":" + JSONObject.quote(getIban()) + ",";
		}
		
		if (getIssueDate() != null && !"".equals(getIssueDate())) {
			result += "\"" + JsonConstants.ISSUE_DATE
					+ "\":" + JSONObject.quote(getIssueDate()) + ",";
		}
		
		if (getIssuingBank() != null && !"".equals(getIssuingBank())) {
			result += "\"" + JsonConstants.ISSUING_BANK
					+ "\":" + JSONObject.quote(getIssuingBank()) + ",";
		}
		
		if (getLastName() != null && !"".equals(getLastName())) {
			result += "\"" + JsonConstants.LAST_NAME
					+ "\":" + JSONObject.quote(getLastName()) + ",";
		}
		
		if (getMemberID() != null && !"".equals(getMemberID())) {
			result += "\"" + JsonConstants.MEMBER_ID
					+ "\":" + JSONObject.quote(getMemberID()) + ",";
		}
		
		if (getMiddleName() != null && !"".equals(getMiddleName())) {
			result += "\"" + JsonConstants.MIDDLE_NAME
					+ "\":" + JSONObject.quote(getMiddleName()) + ",";
		}
		
		if (getNameOnAccount() != null && !"".equals(getNameOnAccount())) {
			result += "\"" + JsonConstants.NAME_ON_ACCOUNT
					+ "\":" + JSONObject.quote(getNameOnAccount()) + ",";
		}
		
		if (getNameOnCard() != null && !"".equals(getNameOnCard())) {
			result += "\"" + JsonConstants.NAME_ON_CARD
					+ "\":" + JSONObject.quote(getNameOnCard()) + ",";
		}
		
		if (getNationality() != null && !"".equals(getNationality())) {
			result += "\"" + JsonConstants.NATIONALITY
					+ "\":" + JSONObject.quote(getNationality()) + ",";
		}
		
		if (getNotes() != null && !"".equals(getNotes())) {
			result += "\"" + JsonConstants.NOTES
					+ "\":" + JSONObject.quote(getNotes()) + ",";
		}
		
		if (getPassportNumber() != null && !"".equals(getPassportNumber())) {
			result += "\"" + JsonConstants.PASSPORT_NUMBER
					+ "\":" + JSONObject.quote(getPassportNumber()) + ",";
		}
		
		if (getPassword() != null && !"".equals(getPassword())) {
			result += "\"" + JsonConstants.PASSWORD
					+ "\":" + JSONObject.quote(getPassword()) + ",";
		}
		
		if (getPhoneNumber() != null && !"".equals(getPhoneNumber())) {
			result += "\"" + JsonConstants.PHONE_NUMBER
					+ "\":" + JSONObject.quote(getPhoneNumber()) + ",";
		}
		
		if (getPIN() != null && !"".equals(getPIN())) {
			result += "\"" + JsonConstants.PIN
					+ "\":" + JSONObject.quote(getPIN()) + ",";
		}
		
		if (getPlaceOfIssue() != null && !"".equals(getPlaceOfIssue())) {
			result += "\"" + JsonConstants.PLACE_OF_ISSUE
					+ "\":" + JSONObject.quote(getPlaceOfIssue()) + ",";
		}
		
		result += "\"" + JsonConstants.RE_ENTER_PASSWORD
				+ "\":" + JSONObject.quote((getReEnterPassword()? "1" : "0")) + ",";
		
		if (getRoutingNumber() != null && !"".equals(getRoutingNumber())) {
			result += "\"" + JsonConstants.ROUTING_NUMBER
					+ "\":" + JSONObject.quote(getRoutingNumber()) + ",";
		}
		
		if (getSsn() != null && !"".equals(getSsn())) {
			result += "\"" + JsonConstants.SSN_NUMBER
					+ "\":" + JSONObject.quote(getSsn()) + ",";
		}
		
		if (getState() != null && !"".equals(getState())) {
			result += "\"" + JsonConstants.STATE
					+ "\":" + JSONObject.quote(getState()) + ",";
		}
		
		result += "\"" + JsonConstants.SUB_DOMAIN
				+ "\":" + "\"" + getSubDomain() + "\",";
		
		if (getSwift() != null && !"".equals(getSwift())) {
			result += "\"" + JsonConstants.SWITFT
					+ "\":" + JSONObject.quote(getSwift()) + ",";
		}
		
		if (getTitle() != null && !"".equals(getTitle())) {
			result += "\"" + JsonConstants.TITLE
					+ "\":" + JSONObject.quote(getTitle()) + ",";
		}
		
		result += "\"" + JsonConstants.USE_SECURE_BROWSER
				+ "\":" + JSONObject.quote((getUseSecureBrowser() ? "1" : "0")) + ",";
		
		if (getUsername() != null && !"".equals(getUsername())) {
			result += "\"" + JsonConstants.USERNAME
					+ "\":" + JSONObject.quote(getUsername()) + ",";
		}
		
		if (getZipCode() != null && !"".equals(getZipCode())) {
			result += "\"" + JsonConstants.ZIP_CODE
					+ "\":" + JSONObject.quote(getZipCode()) + ",";
		}

		if (getSiteUrl() != null && !"".equals(getSiteUrl())) {
			result += "\"" + JsonConstants.SITE_URL
					+ "\":" + JSONObject.quote(getSiteUrl()) + ",";
		}

		if (getLoginUrl() != null && !"".equals(getLoginUrl())) {
			result += "\"" + JsonConstants.LOGIN_URL
					+ "\":" + JSONObject.quote(getLoginUrl()) + ",";
		}

		if (getNickname() != null && !"".equals(getNickname())) {
			result += "\"" + JsonConstants.NICKNAME
					+ "\":" + JSONObject.quote(getNickname()) + ",";
		}
		
		result += "\"" + JsonConstants.PASSWORD_VISIBLE
				+ "\":" + JSONObject.quote((getPasswordVisibleRecipient() ? "1" : "0")) + ",";
		
		result = result.substring(0, result.length()-1);
		result += "}";
		return result;
	}
	
	public ArrayList<com.passwordboss.android.database.beans.SecureItemData> convertToObjectForDatabase(String type) {
		ArrayList<com.passwordboss.android.database.beans.SecureItemData> result =
				new ArrayList<>();
		if (getAccountNumber() != null && !"".equals(getAccountNumber())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ACCOUNT_NUMBER);
			secureItemData.setValue(getAccountNumber());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getPasswordVisibleRecipient()) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.PASSWORD_VISIBLE);
			secureItemData.setValue((getPasswordVisibleRecipient() ? "1" : "0"));
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getAddress1() != null && !"".equals(getAddress1())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ADDRESS_ADDRESS1);
			secureItemData.setValue(getAddress1());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getAddress2() != null && !"".equals(getAddress2())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ADDRESS_ADDRESS2);
			secureItemData.setValue(getAddress2());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getApt() != null && !"".equals(getApt())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.APT);
			secureItemData.setValue(getApt());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getAutologin() && Constants.PV_TYPE.equals(type)) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.AUTOLOGIN);
			secureItemData.setValue((getAutologin() ? "1" : "0") + "");
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getBank() != null && !"".equals(getBank())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.BANK);
			secureItemData.setValue(getBank());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getAddressRef() != null && !"".equals(getAddressRef())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ADDRESS_REF);
			secureItemData.setValue(getAddressRef());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getCardNumber() != null && !"".equals(getCardNumber())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.CARD_NUMBER);
			secureItemData.setValue(getCardNumber());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getCardType() != null && !"".equals(getCardType())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.CARD_TYPE);
			secureItemData.setValue(getCardType());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getCity() != null && !"".equals(getCity())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.CITY);
			secureItemData.setValue(getCity());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getCompany() != null && !"".equals(getCompany())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.COMPANY);
			secureItemData.setValue(getCompany());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getCountry() != null && !"".equals(getCountry())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.COUNTRY);
			secureItemData.setValue(getCountry());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getCVV() != null && !"".equals(getCVV())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.CVV);
			secureItemData.setValue(getCVV());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getDateOfBirth() != null && !"".equals(getDateOfBirth())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.DATE_OF_BIRTH);
			secureItemData.setValue(getDateOfBirth());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getDriverLicenceNumber() != null && !"".equals(getDriverLicenceNumber())) {		
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.DRIVER_LICENCE_NUMBER);
			secureItemData.setValue(getDriverLicenceNumber());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getEmail() != null && !"".equals(getEmail())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.EMAIL);
			secureItemData.setValue(getEmail());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getExpires() != null && !"".equals(getExpires())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.EXPIRES);
			secureItemData.setValue(getExpires());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getFirstName() != null && !"".equals(getFirstName())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.FIRST_NAME);
			secureItemData.setValue(getFirstName());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getGender() != null && !"".equals(getGender())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.GENDER_DATA);
			secureItemData.setValue(getGender());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getIban() != null && !"".equals(getIban())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.IBAN);
			secureItemData.setValue(getIban());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getIssueDate() != null && !"".equals(getIssueDate())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ISSUE_DATE);
			secureItemData.setValue(getIssueDate());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getIssuingBank() != null && !"".equals(getIssuingBank())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ISSUING_BANK);
			secureItemData.setValue(getIssuingBank());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getLastName() != null && !"".equals(getLastName())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.LAST_NAME);
			secureItemData.setValue(getLastName());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getMemberID() != null && !"".equals(getMemberID())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.MEMBER_ID);
			secureItemData.setValue(getMemberID());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getMiddleName() != null && !"".equals(getMiddleName())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.MIDDLE_NAME);
			secureItemData.setValue(getMiddleName());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getNameOnAccount() != null && !"".equals(getNameOnAccount())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.NAME_ON_ACCOUNT);
			secureItemData.setValue(getNameOnAccount());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getNameOnCard() != null && !"".equals(getNameOnCard())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.NAME_ON_CARD);
			secureItemData.setValue(getNameOnCard());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getNationality() != null && !"".equals(getNationality())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.NATIONALITY);
			secureItemData.setValue(getNationality());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getNotes() != null && !"".equals(getNotes())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.NOTES);
			secureItemData.setValue(getNotes());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getPassportNumber() != null && !"".equals(getPassportNumber())) {		
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.PASSPORT_NUMBER);
			secureItemData.setValue(getPassportNumber());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getPassword() != null && !"".equals(getPassword())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.PASSWORD);
			secureItemData.setValue(getPassword());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getPhoneNumber() != null && !"".equals(getPhoneNumber())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.PHONE_NUMBER);
			secureItemData.setValue(getPhoneNumber());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getPIN() != null && !"".equals(getPIN())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.PIN);
			secureItemData.setValue(getPIN());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getPlaceOfIssue() != null && !"".equals(getPlaceOfIssue())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.PLACE_OF_ISSUE);
			secureItemData.setValue(getPlaceOfIssue());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getReEnterPassword() && Constants.PV_TYPE.equals(type)) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.RE_ENTER_PASSWORD);
			secureItemData.setValue(getReEnterPassword()? "1" : "0");
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getRoutingNumber() != null && !"".equals(getRoutingNumber())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ROUTING_NUMBER);
			secureItemData.setValue(getRoutingNumber());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getSsn() != null && !"".equals(getSsn())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.SSN_NUMBER);
			secureItemData.setValue(getSsn());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getState() != null && !"".equals(getState())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.STATE);
			secureItemData.setValue(getState());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getSubDomain() && Constants.PV_TYPE.equals(type)) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.SUB_DOMAIN);
			secureItemData.setValue((getSubDomain()? "1" : "0"));
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getSwift() != null && !"".equals(getSwift())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.SWITFT);
			secureItemData.setValue(getSwift());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}	
		
		if (getTitle() != null && !"".equals(getTitle())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.TITLE);
			secureItemData.setValue(getTitle());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getUseSecureBrowser() && Constants.PV_TYPE.equals(type)) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.USE_SECURE_BROWSER);
			secureItemData.setValue((getUseSecureBrowser() ? "1" : "0"));
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getUsername() != null && !"".equals(getUsername())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.USERNAME);
			secureItemData.setValue(getUsername());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getZipCode() != null && !"".equals(getZipCode())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.ZIP_CODE);
			secureItemData.setValue(getZipCode());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getLoginUrl() != null && !"".equals(getLoginUrl())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.LOGIN_URL);
			secureItemData.setValue(getLoginUrl());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		if (getNickname() != null && !"".equals(getNickname())) {
			com.passwordboss.android.database.beans.SecureItemData secureItemData = 
					new com.passwordboss.android.database.beans.SecureItemData();
			secureItemData.setActive(true);
			secureItemData.setIdentifier(JsonConstants.NICKNAME);
			secureItemData.setValue(getNickname());
			secureItemData.setOrder(0);
			result.add(secureItemData);
		}
		
		return result;
	}

	public static SecureItemDataJSON getSecureItemDataString(
			ForeignCollection<SecureItemData> secureItemData) {
		String data = "";
		SecureItemDataJSON result = null;
		try {
			 CloseableIterator<com.passwordboss.android.database.beans.SecureItemData> iterator =
					 secureItemData.closeableIterator();
			try {
				data = "{";
			    while (iterator.hasNext()) {
			    	com.passwordboss.android.database.beans.SecureItemData itemData 
			    	= iterator.next();
                    String identifier = itemData.getIdentifier();
                    String itemValue = itemData.getValue();
					if (identifier.equals(JsonConstants.AUTOLOGIN) ||
                        identifier.equals(JsonConstants.RE_ENTER_PASSWORD) ||
                        identifier.equals(JsonConstants.SUB_DOMAIN) ||
                        identifier.equals(JsonConstants.USE_SECURE_BROWSER) ||
                        identifier.equals(JsonConstants.PASSWORD_VISIBLE)) {
                        data += "\"" + identifier + "\":";
                        if (itemValue.equals("1")) {
                            data += "\"" + true + "\",";
                        } else {
                            data += "\"" + false + "\",";
                        }
                    } else {
                        data += "\"" + identifier + "\":";
                        data +=  JSONObject.quote(itemValue) + ",";
			    	}
			    }
			} finally {
			    iterator.close();
			}
			data = data.substring(0, data.length()-1) + "}";
			result = SecureItemDataJSON.convertToObject(data);
		} catch (Exception e) {
		}
		return result;
	}
}
