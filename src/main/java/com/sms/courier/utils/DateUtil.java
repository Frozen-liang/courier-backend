package com.sms.courier.utils;

import com.sms.courier.common.constant.TimePatternConstant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateUtil {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern(TimePatternConstant.DEFAULT_PATTERN);

    private DateUtil() {
    }

    public static String toString(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        return DTF.format(localDateTime);
    }

}
