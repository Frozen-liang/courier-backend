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

    public static String formatFirstLowerCase(String name) {
        if (name == null || "".equals(name)) {
            return name;
        }
        if (name.length() == 1) {
            return name.toLowerCase();
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

}
