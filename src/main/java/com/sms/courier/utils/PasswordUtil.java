package com.sms.courier.utils;

import java.security.SecureRandom;
import java.util.Map;

public class PasswordUtil {

    private static final Map<Integer, String> CHAR_MAP = Map
        .of(0, "abcdefghijklmnopqrstuvwxyz",
            1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            2, "!@#$%^&*",
            3, "0123456789");
    private static final int LENGTH = 8;
    private static final int MAX_KEY = 4;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String randomPassword() {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            if (i < 4) {
                password.append(getChar(i));
            } else {
                password.append(getChar(RANDOM.nextInt(MAX_KEY)));
            }
        }
        return password.toString();
    }

    private static char getChar(int key) {
        String chars = CHAR_MAP.get(key);
        return chars.charAt(RANDOM.nextInt(chars.length()));
    }
}