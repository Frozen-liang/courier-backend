package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

@DisplayName("Tests for AesUtil")
public class AesUtilTest {

    private static final Key key;
    private static final String KEY_STR = "courier/09.16.2020";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String PASSWORD = "test";
    private static final String CIPHERTEXT = "DRKFZPy1E1M3zcZSFLVorg==";

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

    @Test
    @DisplayName("Test the encrypt method in the AesUtilTest")
    public void encrypt_test()
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = PASSWORD.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] doFinal = cipher.doFinal(bytes);
        String result = Base64Utils.encodeToString(doFinal);
        System.out.println(AesUtil.encrypt(PASSWORD));
        assertThat(AesUtil.encrypt(PASSWORD)).isEqualTo(result);
    }

    @Test
    @DisplayName("An exception occurred while encrypt AesUtil")
    public void encrypt_exception_test() {
        assertThatThrownBy(() -> AesUtil.encrypt(null)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Test the decrypt method in the AesUtilTest")
    public void decrypt_test()
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] doFinal = cipher.doFinal(Base64Utils.decodeFromString(CIPHERTEXT));
        String result = new String(doFinal, StandardCharsets.UTF_8);
        assertThat(AesUtil.decrypt(CIPHERTEXT)).isEqualTo(result);
    }

    @Test
    @DisplayName("An exception occurred while decrypt AesUtil")
    public void decrypt_exception_test() {
        assertThatThrownBy(() -> AesUtil.decrypt(null)).isInstanceOf(RuntimeException.class);
    }
}
