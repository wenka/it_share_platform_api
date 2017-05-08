package com.wenka.commons.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public abstract class SecureCoder {
    private static final String UTF8 = "UTF-8";
    private static final String AES = "AES";
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA1 = "HmacSHA1";
    private static final String _M_K_ = "Zjg2YzcwNDEtODIyNy00MmI3LWFkZGItZmUyYjgxMDIzZGQw";

    public SecureCoder() {
    }

    public static byte[] initAESKey(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        if (seed != null) {
            secureRandom.setSeed(seed.getBytes("UTF-8"));
        } else {
            secureRandom.setSeed("Zjg2YzcwNDEtODIyNy00MmI3LWFkZGItZmUyYjgxMDIzZGQw".getBytes("UTF-8"));
        }

        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, secureRandom);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    public static String aesEncrypt(String source, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, secretKey);
        byte[] bytes = cipher.doFinal(source.getBytes());
        return Base64.encodeBase64URLSafeString(bytes);
    }

    public static String aesDecrypt(String source, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, secretKey);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(source));
        return new String(bytes, "UTF-8");
    }

    public static String md5(String source) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(source.getBytes());
        byte[] bytes = md.digest();
        return Hex.encodeHexString(bytes).toLowerCase();
    }

    public static String sha1(String source) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(source.getBytes());
        byte[] bytes = md.digest();
        return Hex.encodeHexString(bytes).toLowerCase();
    }

    private static byte[] initHMACKey(String algorithm) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private static String encryptHMAC(String algorithm, String source, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(source.getBytes());
        return Hex.encodeHexString(bytes).toLowerCase();
    }

    public static String createHmacMD5Key() throws NoSuchAlgorithmException {
        byte[] key = initHMACKey("HmacMD5");
        return Base64.encodeBase64String(key);
    }

    public static String createHmacSHA1Key() throws NoSuchAlgorithmException {
        byte[] key = initHMACKey("HmacSHA1");
        return Base64.encodeBase64String(key);
    }

    public static String hmacMD5(String source, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        return encryptHMAC("HmacMD5", source, Base64.decodeBase64(key));
    }

    public static String hmacSHA1(String source, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        return encryptHMAC("HmacSHA1", source, Base64.decodeBase64(key));
    }

    public static String encrypt(String source) {
        String result = null;

        try {
            byte[] e = initAESKey("Zjg2YzcwNDEtODIyNy00MmI3LWFkZGItZmUyYjgxMDIzZGQw");
            result = aesEncrypt(source, e);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public static String decrypt(String source) {
        String result = null;

        try {
            byte[] e = initAESKey("Zjg2YzcwNDEtODIyNy00MmI3LWFkZGItZmUyYjgxMDIzZGQw");
            result = aesDecrypt(source, e);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args){
        try {
            String s = sha1("123");
            String s1 = sha1("40bd001563085fc35165329ea1ff5c5ecbdbbeef");
            System.out.println(UUID.randomUUID().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}