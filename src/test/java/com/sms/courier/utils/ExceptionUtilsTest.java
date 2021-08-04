package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ExceptionUtils")
public class ExceptionUtilsTest {

    @Test
    @DisplayName("Test the mpe method in the ExceptionUtils")
    public void mpe_error_throwable_test() {
        assertThat(ExceptionUtils.mpe(ErrorCode.ADD_API_ERROR, new RuntimeException()))
            .isInstanceOf(ApiTestPlatformException.class).extracting("code").as(ErrorCode.ADD_API_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the mpe method in the ExceptionUtils")
    public void mpe_error_test() {
        assertThat(ExceptionUtils.mpe(ErrorCode.ADD_API_ERROR)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").as(ErrorCode.ADD_API_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the mpe method in the ExceptionUtils")
    public void mpe_string_throwable_test() {
        assertThat(ExceptionUtils.mpe("error", new RuntimeException())).isInstanceOf(ApiTestPlatformException.class)
            .extracting("message").as("error");
    }

    @Test
    @DisplayName("Test the mpe method in the ExceptionUtils")
    public void mpe_string_test() {
        assertThat(ExceptionUtils.mpe("error")).isInstanceOf(ApiTestPlatformException.class).extracting("message")
            .as("error");
    }
}
