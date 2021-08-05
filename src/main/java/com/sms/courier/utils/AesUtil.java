package com.sms.courier.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

@Slf4j
public class AesUtil {

    private AesUtil() {
    }

    private static final Key key;
    private static final String KEY_STR = "sms-stap";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";


    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            random.setSeed(KEY_STR.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(128, random);
            key = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String encrypt(String password) {
        try {
            byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return Base64Utils.encodeToString(doFinal);
        } catch (Exception e) {
            log.error("Failed to encrypt the password!", e);
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(Base64Utils.decodeFromString(cipherText));
            return new String(doFinal, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to decrypt the clipherText!", e);
            throw new RuntimeException(e);
        }
    }

}