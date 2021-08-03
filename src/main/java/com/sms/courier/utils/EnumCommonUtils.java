package com.sms.courier.utils;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Objects;

public abstract class EnumCommonUtils {

    public static Integer getCode(EnumCommon enumCommon) {
        return Objects.isNull(enumCommon) ? null : enumCommon.getCode();
    }
}
