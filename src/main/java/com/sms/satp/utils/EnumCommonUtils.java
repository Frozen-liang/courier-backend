package com.sms.satp.utils;

import com.sms.satp.common.enums.EnumCommon;
import java.util.Objects;

public abstract class EnumCommonUtils {

    public static Integer getCode(EnumCommon enumCommon) {
        return Objects.isNull(enumCommon) ? null : enumCommon.getCode();
    }
}
