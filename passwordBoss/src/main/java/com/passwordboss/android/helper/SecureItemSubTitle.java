package com.passwordboss.android.helper;

import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.j256.ormlite.dao.ForeignCollection;
import com.passwordboss.android.database.beans.SecureItem;
import com.passwordboss.android.database.beans.SecureItemData;
import com.passwordboss.android.model.ItemType;

public class SecureItemSubTitle {
    final SecureItem mSecureItem;

    public SecureItemSubTitle(SecureItem secureItem) {
        mSecureItem = secureItem;
    }

    @Nullable
    public String get() {
        ItemType itemType = ItemType.from(mSecureItem);
        if (null == itemType) return null;
        switch (itemType) {
            case Address:
                return getValue(SecureItemData.Identifier.ADDRESS1);
            case BankAccount:
                return getValue(SecureItemData.Identifier.BANK_NAME);
            case CreditCard:
                String cardNumber = Strings.nullToEmpty(getValue(SecureItemData.Identifier.CARD_NUMBER));
                if (cardNumber.length() >= 4) {
                    cardNumber = cardNumber.substring(cardNumber.length() - 4);
                }
                return "####-" + cardNumber;
            case DriversLicense:
                return getValue(SecureItemData.Identifier.DRIVER_LICENSE_NUMBER);
            case Email:
                return getValue(SecureItemData.Identifier.EMAIL);
            case EmailAccount:
            case InstantMessenger:
            case Server:
            case Website:
                return getValue(SecureItemData.Identifier.USERNAME);
            case FrequentFlyer:
                return getValue(SecureItemData.Identifier.FREQUENT_FLYER_NUMBER);
            case HealthInsurance:
                return getValue(SecureItemData.Identifier.MEMBER_ID);
            case HotelRewards:
                return getValue(SecureItemData.Identifier.MEMBERSHIP_NUMBER);
            case Insurance:
                return getValue(SecureItemData.Identifier.POLICY_NUMBER);
            case MemberId:
                return getValue(SecureItemData.Identifier.MEMBER_ID);
            case Passport:
                return getValue(SecureItemData.Identifier.PASSPORT_NUMBER);
            case Phone:
                return getValue(SecureItemData.Identifier.PHONE_NUMBER);
            case Prescription:
                return getValue(SecureItemData.Identifier.PRESCRIPTION_NUMBER);
            case SocialSecurity:
                return getValue(SecureItemData.Identifier.SOCIAL_SECURITY_NUMBER);
            case SshKey:
                return getValue(SecureItemData.Identifier.SERVER_ADDRESS);
            case WiFi:
                return getValue(SecureItemData.Identifier.SSID);
            default:
                return null;
        }
    }

    @Nullable
    private String getValue(String identifier) {
        ForeignCollection<SecureItemData> foreignCollection = mSecureItem.getSecureItemData();
        for (SecureItemData itemData : foreignCollection) {
            if (identifier.equals(itemData.getIdentifier())) return itemData.getValue();
        }
        return null;
    }

}
