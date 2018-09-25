package cn.tz.chenjia.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryptUtils {

    private static final String ALGORITHM = "AES";

    public static final String KEY = "SECRET";

    public static final int N = 1;

    private static Key toKey(byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        return secretKey;
    }

    private static String encrypt(String data, String key) {
        Key k = toKey(Base64.decodeBase64(key));
        byte[] raw = k.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
        Cipher cipher = null;
        byte[] bytes = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(1, secretKeySpec);
            bytes = cipher.doFinal(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(bytes);
    }

    private static String decrypt(String data, String key) {
        Key k = toKey(Base64.decodeBase64(key));
        byte[] raw = k.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
        Cipher cipher = null;
        byte[] bytes = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(2, secretKeySpec);
            bytes = cipher.doFinal(Base64.decodeBase64(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        String r = null;
        try {
            r = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return r;
    }

    private static String getSecrtKey(String encrypted) {
        byte[] bytes = new byte[0];
        try {
            bytes = encrypted.getBytes("ISO8859-1");
            bytes = Arrays.copyOf(bytes, 16);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(bytes);
    }

    public static String decrypt(String data, String key, int n) {
        String r = "";
        String secrtKey = getSecrtKey(key);
        for (int i = 0; i <= n; i++) {
            r = decrypt(data, secrtKey);
        }
        return r;
    }

    public static String encrypt(String data, String key, int n) {
        String r = "";
        String secrtKey = getSecrtKey(key);
        for (int i = 0; i <= n; i++) {
            r = encrypt(data, secrtKey);
        }
        return r;
    }

}
