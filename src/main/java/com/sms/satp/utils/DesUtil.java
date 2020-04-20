package com.sms.satp.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

@Slf4j
public class DesUtil {

    private DesUtil() {
    }

    private static final String secretKeyBase64 = "QJLVDk89AqE=";

    public static String encrypt(String password) {
        try {
            Key convertSecretKey = generateKey();
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,convertSecretKey);
            byte[] bytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64Utils.encodeToString(bytes);
        } catch (Exception e) {
            log.error("Failed to encrypt the password!", e);
            throw new RuntimeException();
        }
    }

    public static String decrypt(String cipherText) {
        try {
            Key convertSecretKey = generateKey();
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
            byte[] bytes = cipher.doFinal(Base64Utils.decodeFromString(cipherText));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to decrypt the clipherText!", e);
            throw new RuntimeException();
        }
    }

    private static Key generateKey() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytesKey = Base64Utils.decodeFromString(secretKeyBase64);
        DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        return factory.generateSecret(desKeySpec);
    }

}