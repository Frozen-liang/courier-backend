package com.sms.satp.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {

    private MD5Util() {}

    public static String getMD5(Object o) {
        try {
            String json = JsonUtils.serializeObject(o);
            return DigestUtils.md5DigestAsHex(json.getBytes());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}