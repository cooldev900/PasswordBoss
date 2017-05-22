package com.passwordboss.android.crypto;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@SuppressLint("TrulyRandom")
public class CryptoHelper {

    public static String SymmetricAlgorithm = "AES";


    public static String AsymmetricAlgorithm = "RSA";
    public static String AsymmetricAlgorithmECB = "RSA/ECB/PKCS1Padding";

    private static byte[] concat(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static KeyPair createKeyPair() throws Exception {
        return generateRSAKeyPair(2048);
    }

    public static AESKeySet createSecretAESKeySet() throws Exception {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(SymmetricAlgorithm);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
            return null;
        }
        keyGenerator.init(640);
        SecretKey secretKeyAES = keyGenerator.generateKey();
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        byte[] hmacKey = new byte[32];
        System.arraycopy(secretKeyAES.getEncoded(), 0, key, 0, 32);
        System.arraycopy(secretKeyAES.getEncoded(), 32, iv, 0, 16);
        System.arraycopy(secretKeyAES.getEncoded(), 48, hmacKey, 0, 32);
        return new AESKeySet(key, iv, hmacKey);
    }

    @NonNull
    public static String createSecretAESKeySetString() throws Exception {
        AESKeySet secretAESKeySet = createSecretAESKeySet();
        return null == secretAESKeySet ? "" :
                secretAESKeySet.keysToString();

    }

    private static String decryptAESKeySet(String encryptedText,
                                           AESKeySet keySet) throws Exception {
        byte[] encryptedTextByte = Base64.decode(encryptedText, Base64.DEFAULT);
        AESCrypto aes = new AESCrypto();
        byte[] decryptedByte = aes.decryptAndVerify(encryptedTextByte, keySet);
        return new String(decryptedByte);
    }

    public static String decryptRSA(byte[] text, PrivateKey key)
            throws Exception {
        byte[] dectyptedText;
        final Cipher cipher = Cipher.getInstance(AsymmetricAlgorithmECB);
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);
        return new String(dectyptedText);
    }

    private static String encryptAESKeySet(String plainText, AESKeySet keySet)
            throws Exception {
        AESCrypto aes = new AESCrypto();
        byte[] plainTextByte = plainText.getBytes();
        byte[] encryptedByte = aes.encryptAndSign(plainTextByte, keySet);
        return Base64.encodeToString(encryptedByte,
                Base64.DEFAULT);
    }

    private static byte[] encryptRSA(String text, PublicKey key)
            throws Exception {
        byte[] cipherText;
        final Cipher cipher = Cipher.getInstance(AsymmetricAlgorithmECB);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());

        return cipherText;
    }

    private static PrivateKey generatePrivateKeyByteArray(String privateKey)
            throws Exception {
        return generatePrivateKeyByteArray(privateKey.getBytes());

    }

    private static PrivateKey generatePrivateKeyByteArray(byte[] privateKey)
            throws Exception {
        String RSA_PRIVATE_KEY = new String(privateKey);
        RSA_PRIVATE_KEY = RSA_PRIVATE_KEY.replace(
                "-----BEGIN RSA PRIVATE KEY-----", "");
        RSA_PRIVATE_KEY = RSA_PRIVATE_KEY.replace(
                "-----BEGIN PRIVATE KEY-----", "");
        RSA_PRIVATE_KEY = RSA_PRIVATE_KEY.replace(
                "-----END RSA PRIVATE KEY-----", "");
        RSA_PRIVATE_KEY = RSA_PRIVATE_KEY.replace("-----END PRIVATE KEY-----",
                "");
        RSA_PRIVATE_KEY = RSA_PRIVATE_KEY.trim();

        byte[] tmpRSAPrivateKey = Base64
                .decode(RSA_PRIVATE_KEY, Base64.DEFAULT);
        PKCS8EncodedKeySpec privSpec;
        PrivateKey pkey;
        KeyFactory kf = KeyFactory.getInstance(AsymmetricAlgorithm);

        try {
            privSpec = new PKCS8EncodedKeySpec(tmpRSAPrivateKey);
            pkey = kf.generatePrivate(privSpec);
        } catch (Exception e) {
            System.out.println("eeet");
            privSpec = new PKCS8EncodedKeySpec(
                    wrapRSA1024_PKCS8(tmpRSAPrivateKey));
            pkey = kf.generatePrivate(privSpec);
        }

        return pkey;

    }

    private static PublicKey generatePublicKeyByteArray(String publicKey)
            throws Exception {
        return generatePublicKeyByteArray(publicKey.getBytes());

    }

    private static PublicKey generatePublicKeyByteArray(byte[] publicKey)
            throws Exception {
        String RSA_PUBLIC_KEY = new String(publicKey);
        RSA_PUBLIC_KEY = RSA_PUBLIC_KEY.replace(
                "-----BEGIN RSA PUBLIC KEY-----", "");
        RSA_PUBLIC_KEY = RSA_PUBLIC_KEY.replace("-----BEGIN PUBLIC KEY-----",
                "");
        RSA_PUBLIC_KEY = RSA_PUBLIC_KEY.replace("-----END RSA PUBLIC KEY-----",
                "");
        RSA_PUBLIC_KEY = RSA_PUBLIC_KEY.replace("-----END PUBLIC KEY-----", "");
        RSA_PUBLIC_KEY = RSA_PUBLIC_KEY.replace("\n", "");
        RSA_PUBLIC_KEY = RSA_PUBLIC_KEY.trim();

        byte[] tmpRSAPublicKey = Base64.decode(RSA_PUBLIC_KEY, Base64.DEFAULT);
        X509EncodedKeySpec pubSpec;

        try {
            pubSpec = new X509EncodedKeySpec(tmpRSAPublicKey);
        } catch (Exception e) {
            pubSpec = new X509EncodedKeySpec(wrapRSA1024_X509(tmpRSAPublicKey));
        }

        KeyFactory kf = KeyFactory.getInstance(AsymmetricAlgorithm);

        return kf.generatePublic(pubSpec);

    }

    private static KeyPair generateRSAKeyPair(int len)
            throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator
                .getInstance(AsymmetricAlgorithm);
        kpg.initialize(len);
        KeyPair kp;
        kp = kpg.genKeyPair();
        return kp;
    }

    public static String getAESKeySetDecryptedString(String ecryptedPayload,
                                                     String _secretKeyAESKeySet) throws Exception {
        AESKeySet keysSet = AESKeySet.keysFromString(_secretKeyAESKeySet);
        return decryptAESKeySet(ecryptedPayload, keysSet);
    }

    public static String getAESKeySetEncryptedString(String payload,
                                                     String _secretKeyAESKeySet) throws Exception {
        AESKeySet keysSet = AESKeySet.keysFromString(_secretKeyAESKeySet);
        return encryptAESKeySet(payload, keysSet);
    }

    public static PublicKey getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    public static String getRSADecryptedString(String ecryptedPayload,
                                               PrivateKey privateKey) throws Exception {

        return decryptRSA(Base64.decode(ecryptedPayload, Base64.DEFAULT),
                privateKey);
    }

    public static String getRSADecryptedString(String ecryptedPayload,
                                               String privateKey) throws Exception {
        return decryptRSA(Base64.decode(ecryptedPayload, Base64.DEFAULT),
                generatePrivateKeyByteArray(privateKey));
    }

    public static String getRSADecryptedString(String ecryptedPayload,
                                               byte[] privateKey) throws Exception {
        return decryptRSA(Base64.decode(ecryptedPayload, Base64.DEFAULT),
                generatePrivateKeyByteArray(privateKey));
    }

    public static String getRSAEncryptedString(String payload,
                                               PublicKey publicKey) throws Exception {
        return Base64.encodeToString(encryptRSA(payload, publicKey),
                Base64.DEFAULT);
    }

    public static String getRSAEncryptedString(String payload, String publicKey)
            throws Exception {
        return Base64.encodeToString(
                encryptRSA(payload, generatePublicKeyByteArray(publicKey)),
                Base64.DEFAULT);
    }

    public static String getRSAEncryptedString(String payload, byte[] publicKey)
            throws Exception {
        return Base64.encodeToString(
                encryptRSA(payload, generatePublicKeyByteArray(publicKey)),
                Base64.DEFAULT);
    }

    public static int getRSAkeySize(PublicKey publicKey) {

        return ((RSAPublicKey) publicKey).getModulus().bitLength();
    }

    public static boolean isRSAkeySizeV2(byte[] publicKey) {

        if (publicKey == null)
            return false;
        PublicKey _publicKey;
        try {
            _publicKey = generatePublicKeyByteArray(publicKey);
            return getRSAkeySize(_publicKey) == 2048;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static byte[] wrapRSA1024_PKCS8(byte[] pure_DER_rsa_priv_key)
            throws InvalidKeyException {
        if (pure_DER_rsa_priv_key.length > 650) {
            throw new InvalidKeyException("Key too long, not RSA1024");
        }
        byte[] header = toByte("30820278020100300d06092a864886f70d010101050004820262");

        byte seq1LenIndex = 2;
        byte seq2LenIndex = 24;

        int seq2Len = pure_DER_rsa_priv_key.length - 2;
        int seq1Len = seq2Len + 22;

        header[seq1LenIndex] = (byte) (seq1Len / 255);
        header[seq1LenIndex + 1] = (byte) (seq1Len % 255);
        header[seq2LenIndex] = (byte) (seq2Len / 255);
        header[seq2LenIndex + 1] = (byte) (seq2Len % 255);

        return concat(header, pure_DER_rsa_priv_key);
    }

    private static byte[] wrapRSA1024_X509(byte[] pure_DER_rsa_pub_key)
            throws InvalidKeyException {
        if (pure_DER_rsa_pub_key.length > 150) {
            throw new InvalidKeyException("Key too long, not RSA1024");
        }
        byte[] header = toByte("30819f300d06092a864886f70d010101050003818d00");
        byte seq1LenIndex = 2;
        byte seq2LenIndex = 20;

        byte seq2Len = (byte) (pure_DER_rsa_pub_key.length + 1);
        byte seq1Len = (byte) (seq2Len + 18);

        header[seq1LenIndex] = seq1Len;
        header[seq2LenIndex] = seq2Len;

        return concat(header, pure_DER_rsa_pub_key);
    }

}