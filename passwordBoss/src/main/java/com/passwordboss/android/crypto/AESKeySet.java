package com.passwordboss.android.crypto;

import android.util.Base64;

public class AESKeySet {
    private byte[] key, iv, hmacKey;

    public AESKeySet(byte[] key, byte[] iv, byte[] hmacKey) {
        this.key = key;
        this.iv = iv;
        this.hmacKey = hmacKey;
    }

    public AESKeySet(byte[] key, byte[] hmacKey) {
        this(key, new byte[16], hmacKey);
    }

    public static AESKeySet keysFromString(String base64string) {
        if (base64string == null) return null;
        byte[] data = Base64.decode(base64string, Base64.NO_WRAP);
        if (data.length != 64) return null;
        byte[] key = new byte[32];
        byte[] hmacKey = new byte[32];
        System.arraycopy(data, 0, key, 0, 32);
        System.arraycopy(data, 32, hmacKey, 0, 32);
        return new AESKeySet(key, hmacKey);
    }

    public byte[] getHMACKey() {
        return this.hmacKey;
    }

    public byte[] getIV() {
        return this.iv;
    }

    public byte[] getKey() {
        return this.key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public boolean hasValidKeys() {
        return key != null && key.length == 32 && hmacKey != null
                && hmacKey.length == 32;
    }

    public String keysToString() {
        if (!hasValidKeys()) return null;
        byte[] data = new byte[64];
        System.arraycopy(key, 0, data, 0, 32);
        System.arraycopy(hmacKey, 0, data, 32, 32);
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
