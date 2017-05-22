package com.passwordboss.android.crypto;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {
    public byte[] decryptAndVerify(byte[] data, AESKeySet keySet) {
        try {
            byte[] signature = new byte[32];
            System.arraycopy(data, 0, signature, 0, 32);
            byte[] cyphertext = new byte[data.length - 32];
            System.arraycopy(data, 32, cyphertext, 0, cyphertext.length);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySet.getHMACKey(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] hmac = sha256_HMAC.doFinal(cyphertext);
            boolean valid = true;
            for (int i = 0; i < signature.length; i++) {
                if (hmac[i] != signature[i]) valid = false;
            }
            if (!valid) return null;
            byte[] iv = new byte[16];
            System.arraycopy(cyphertext, 0, iv, 0, 16);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(keySet.getKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            byte[] encrypted = new byte[cyphertext.length - 16];
            System.arraycopy(cyphertext, 16, encrypted, 0, encrypted.length);
            return cipher.doFinal(encrypted);
        } catch (Exception ignore) {
        }
        return null;
    }

    public byte[] encryptAndSign(byte[] data, AESKeySet keySet) {
        try {
            AlgorithmParameterSpec iv = new IvParameterSpec(keySet.getIV());
            SecretKeySpec key = new SecretKeySpec(keySet.getKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cyphertext = cipher.doFinal(data);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySet.getHMACKey(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] signData = new byte[cyphertext.length + 16];
            System.arraycopy(keySet.getIV(), 0, signData, 0, 16);
            System.arraycopy(cyphertext, 0, signData, 16, cyphertext.length);
            byte[] hmac = sha256_HMAC.doFinal(signData);
            byte[] output = new byte[hmac.length + signData.length];
            System.arraycopy(hmac, 0, output, 0, hmac.length);
            System.arraycopy(signData, 0, output, hmac.length, signData.length);
            return output;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}
