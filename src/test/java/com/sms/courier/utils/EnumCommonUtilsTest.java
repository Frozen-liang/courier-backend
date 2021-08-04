package com.sms.courier.utils;


import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.common.enums.ApiType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for EnumCommonUtils")
public class EnumCommonUtilsTest {

    @Test
    @DisplayName("Test the getCode method in the EnumCommonUtils")
    public void getCode_null_test() {
        assertThat(EnumCommonUtils.getCode(null)).isNull();
    }

    @Test
    @DisplayName("Test the getCode method in the EnumCommonUtils")
    public void getCode_not_null_test() {
        assertThat(EnumCommonUtils.getCode(ApiType.API)).isEqualTo(ApiType.API.getCode());
    }
}
