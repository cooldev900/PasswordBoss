package com.passwordboss.android.model;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.common.collect.ImmutableBiMap;
import com.passwordboss.android.R;
import com.passwordboss.android.database.beans.SecureItem;

public enum ItemType {
    // add new item button
    AddDifferentItem(R.string.AddItemDifferent, R.drawable.item_type_different_item, Color.TRANSPARENT),
    // Digital Wallet
    BankAccount(R.string.ItemBankAccount, R.drawable.item_type_bank, 0xff107c10),
    CreditCard(R.string.ItemCreditCard, R.drawable.item_type_credit_card, 0xffb99834),
    DigitalWallet(R.string.DigitalWallet,
            R.drawable.item_type_digital_wallet,
            new ItemType[]{
                    BankAccount,
                    CreditCard,
                    AddDifferentItem
            }),
    // Password
    Application(R.string.ItemApp, R.drawable.item_type_application, 0xff4f4da0),
    Database(R.string.ItemDatabase, R.drawable.item_type_database, 0xffbf0077),
    EmailAccount(R.string.ItemEmailAccount, R.drawable.item_type_email_account, 0xff2ecc71),
    InstantMessenger(R.string.ItemInstantMessenger, R.drawable.item_type_instant_messenger, 0xffda3b01),
    Server(R.string.ItemServer, R.drawable.item_type_server, 0xff486860),
    SshKey(R.string.ItemSSHKeys, R.drawable.item_type_ssh_keys, 0xff212121),
    Website(R.string.ItemWebsite, R.drawable.item_type_website, 0xff0078d7),
    WiFi(R.string.ItemWiFi, R.drawable.item_type_wifi, 0xff881798),
    Password(R.string.Password,
            R.drawable.item_type_password,
            new ItemType[]{
                    Website,
                    Application,
                    Database,
                    EmailAccount,
                    InstantMessenger,
                    Server,
                    SshKey,
                    WiFi,
                    AddDifferentItem
            }),
    // Personal Info
    Address(R.string.ItemAddress, R.drawable.item_type_address, 0xff9951c0),
    Company(R.string.ItemCompany, R.drawable.item_type_company, 0xff2d7d9a),
    Email(R.string.ItemEmail, R.drawable.item_type_email, 0xff00b7c3),
    Name(R.string.ItemName, R.drawable.item_type_name, 0xffd3746a),
    Phone(R.string.ItemPhone, R.drawable.item_type_phone, 0xff495b49),
    PersonalInfo(R.string.PersonalInfo,
            R.drawable.item_type_personal_info,
            new ItemType[]{
                    Address,
                    Company,
                    Email,
                    Name,
                    Phone,
                    AddDifferentItem
            }),
    // Secure Notes
    Alarm(R.string.ItemAlarmCode, R.drawable.item_type_alarm, 0xffe81123),
    DriversLicense(R.string.ItemDriversLicense, R.drawable.item_type_drivers_license, 0xff5d6a7c),
    EstatePlan(R.string.ItemEstatePlan, R.drawable.item_type_estate_plan, 0xff7b6d3f),
    FrequentFlyer(R.string.ItemFrequentFlyer, R.drawable.item_type_frequent_flyer, 0xff8c989c),
    Note(R.string.ItemNote, R.drawable.item_type_note, 0xffffb900),
    HealthInsurance(R.string.ItemHealthInsurance, R.drawable.item_type_health_insurance, 0xff0099bc),
    HotelRewards(R.string.ItemHotelRewards, R.drawable.item_type_hotel_rewards, 0xffc239b3),
    Insurance(R.string.ItemInsurance, R.drawable.item_type_insurance, 0xff4d5767),
    MemberId(R.string.ItemMemberID, R.drawable.item_type_member_id, 0xffca5010),
    Passport(R.string.ItemPassport, R.drawable.item_type_passport, 0xff2c3e50),
    Prescription(R.string.ItemPrescription, R.drawable.item_type_prescription, 0xff3498db),
    SocialSecurity(R.string.ItemSocialSecurity, R.drawable.item_type_social_security, 0xffbdc3c7),
    SoftwareLicense(R.string.ItemSoftwareLicense, R.drawable.item_type_software_license, 0xff6596d9),
    SecureNotes(R.string.SecureNotes,
            R.drawable.item_type_secure_note,
            new ItemType[]{
                    Alarm,
                    DriversLicense,
                    EstatePlan,
                    FrequentFlyer,
                    Note,
                    HealthInsurance,
                    HotelRewards,
                    Insurance,
                    MemberId,
                    Passport,
                    Prescription,
                    SocialSecurity,
                    SoftwareLicense,
                    AddDifferentItem
            }),
    // Other
    Identity(R.string.ItemIdentity, R.drawable.item_type_identity),
    SharedItem(R.string.ItemSharedItem, R.drawable.item_type_shared_item),
    EmergencyContact(R.string.ItemEmergencyContact, R.drawable.item_type_emergency_contact),
    Folder(R.string.ItemFolder, R.drawable.item_type_folder);

    private static final ImmutableBiMap<ItemType, String> TYPES_MAP =
            new ImmutableBiMap.Builder<ItemType, String>()
                    .put(ItemType.BankAccount, "Bank")
                    .put(ItemType.CreditCard, "CreditCard")
                    .put(ItemType.Application, "Application")
                    .put(ItemType.Database, "Database")
                    .put(ItemType.EmailAccount, "EmailAccount")
                    .put(ItemType.InstantMessenger, "InstantMessenger")
                    .put(ItemType.Server, "Server")
                    .put(ItemType.SshKey, "SSHKey")
                    .put(ItemType.Website, "Website")
                    .put(ItemType.WiFi, "WiFi")
                    .put(ItemType.Address, "Address")
                    .put(ItemType.Company, "Company")
                    .put(ItemType.Email, "Email")
                    .put(ItemType.Name, "Names")
                    .put(ItemType.Phone, "PhoneNumber")
                    .put(ItemType.Alarm, "AlarmCode")
                    .put(ItemType.DriversLicense, "DriverLicense")
                    .put(ItemType.EstatePlan, "EstatePlan")
                    .put(ItemType.FrequentFlyer, "FrequentFlyer")
                    .put(ItemType.Note, "GenericNote")
                    .put(ItemType.HealthInsurance, "HealthInsurance")
                    .put(ItemType.HotelRewards, "HotelRewards")
                    .put(ItemType.Insurance, "Insurance")
                    .put(ItemType.MemberId, "MemberIDs")
                    .put(ItemType.Passport, "Passport")
                    .put(ItemType.Prescription, "Prescription")
                    .put(ItemType.SocialSecurity, "SocialSecurity")
                    .put(ItemType.SoftwareLicense, "SoftwareLicense")
                    .build();
    private static final String TYPE_PASSWORD = "PV";
    private static final String TYPE_DIGITAL_WALLET = "DW";
    private static final String TYPE_PERSONAL_INFO = "PI";
    private static final String TYPE_SECURE_NOTES = "SN";
    @StringRes
    private final int mNameId;
    @DrawableRes
    private final int mIconId;
    private final int mColor;
    private ItemType[] mChildren;

    ItemType(@StringRes int nameId, @DrawableRes int iconId) {
        this(nameId, iconId, Color.WHITE);
    }

    ItemType(@StringRes int nameId, @DrawableRes int iconId, int color) {
        this(nameId, iconId, color, null);
    }

    ItemType(@StringRes int nameId, @DrawableRes int iconId, ItemType[] children) {
        this(nameId, iconId, Color.WHITE, children);

    }

    ItemType(@StringRes int nameId, @DrawableRes int iconId, int color, ItemType[] children) {
        mNameId = nameId;
        mIconId = iconId;
        mChildren = children;
        mColor = color;
    }

    @Nullable
    public static ItemType from(SecureItem secureItem) {
        if (null == secureItem) return null;
        return TYPES_MAP.inverse().get(secureItem.getType());
    }

    @Nullable
    public static String toSecureItemType(ItemType itemType) {
        if (null == itemType) return null;
        switch (itemType) {
            case Password:
                return TYPE_PASSWORD;
            case DigitalWallet:
                return TYPE_DIGITAL_WALLET;
            case PersonalInfo:
                return TYPE_PERSONAL_INFO;
            case SecureNotes:
                return TYPE_SECURE_NOTES;
        }
        if (ItemType.Password.contains(itemType)) return TYPE_PASSWORD;
        if (ItemType.DigitalWallet.contains(itemType)) return TYPE_DIGITAL_WALLET;
        if (ItemType.PersonalInfo.contains(itemType)) return TYPE_PERSONAL_INFO;
        if (ItemType.SecureNotes.contains(itemType)) return TYPE_SECURE_NOTES;
        return null;

    }

    @Nullable
    public static String toType(ItemType itemType) {
        return TYPES_MAP.get(itemType);
    }

    public boolean contains(ItemType itemType) {
        if (null == itemType || null == mChildren) return false;
        for (ItemType value : mChildren) {
            if (value == itemType) return true;
        }
        return false;
    }

    public ItemType[] getChildren() {
        return mChildren;
    }

    public int getColor() {
        return mColor;
    }

    public int getIconId() {
        return mIconId;
    }

    public int getNameId() {
        return mNameId;
    }

    public boolean hasChildren() {
        return null != mChildren && mChildren.length > 0;
    }


}
