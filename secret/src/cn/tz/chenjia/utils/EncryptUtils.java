package cn.tz.chenjia.utils;

import cn.tz.chenjia.rule.EMsg;
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
            handlingError(e);
        } catch (NoSuchPaddingException e) {
            handlingError(e);
        } catch (BadPaddingException e) {
            handlingError(e);
        } catch (UnsupportedEncodingException e) {
            handlingError(e);
        } catch (IllegalBlockSizeException e) {
            handlingError(e);
        } catch (InvalidKeyException e) {
            handlingError(e);
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
            handlingError(e);
        } catch (NoSuchPaddingException e) {
            handlingError(e);
        } catch (BadPaddingException e) {
            handlingError(e);
        } catch (IllegalBlockSizeException e) {
            handlingError(e);
        } catch (InvalidKeyException e) {
            handlingError(e);
        }
        String r = null;
        try {
            r = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            handlingError(e);
        }
        return r;
    }

    private static String getSecrtKey(String encrypted) {
        byte[] bytes = new byte[0];
        try {
            bytes = encrypted.getBytes("ISO8859-1");
            bytes = Arrays.copyOf(bytes, 16);
        } catch (UnsupportedEncodingException e) {
            handlingError(e);
        }
        return Base64.encodeBase64String(bytes);
    }

    public static String decrypt(String data, String key, int n) {
        String r = "";
        String secrtKey = getSecrtKey(key);
        for(int i = 0; i<=n; i++){
            r = decrypt(data, secrtKey);
        }
        return r;
    }

    public static String encrypt(String data, String key, int n) {
        String r = "";
        String secrtKey = getSecrtKey(key);
        for(int i = 0; i<=n; i++){
            r = encrypt(data, secrtKey);
        }
        return r;
    }

    private static void handlingError(Throwable e){
        EMsg.println(e.getMessage());
        System.exit(1);
    }

}
