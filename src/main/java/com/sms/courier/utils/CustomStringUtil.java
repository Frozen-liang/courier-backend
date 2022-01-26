package com.sms.courier.utils;

import org.apache.commons.lang3.StringUtils;

public class CustomStringUtil {

    private CustomStringUtil() {

    }

    public static String formatFirstUpperCase(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        char[] chars = name.toCharArray();
        if (chars[0] >= 97 && chars[0] <= 122) {
            chars[0] -= 32;
        }
        return String.valueOf(chars);
    }
}
