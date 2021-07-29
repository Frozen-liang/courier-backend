package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;

@DisplayName("Tests for Assert")
public class AssertTest {

    private static final String MESSAGE = "test";

    @Test
    @DisplayName("An exception occurred while isTrue AssertTest")
    public void isTrue1_exception_test() {
        assertThatThrownBy(() -> Assert.isTrue(false, MESSAGE, MESSAGE)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while isTrue AssertTest")
    public void isTrue2_exception_test() {
        assertThatThrownBy(() -> Assert.isTrue(false, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while isFalse AssertTest")
    public void isFalse1_exception_test() {
        assertThatThrownBy(() -> Assert.isFalse(true, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while isFalse AssertTest")
    public void isFalse2_exception_test() {
        assertThatThrownBy(() -> Assert.isFalse(true, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while isNull AssertTest")
    public void isNull1_exception_test() {
        assertThatThrownBy(() -> Assert.isNull(NotNull.NOT_NULL, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while isNull AssertTest")
    public void isNull2_exception_test() {
        assertThatThrownBy(() -> Assert.isNull(NotNull.NOT_NULL, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notNull AssertTest")
    public void notNull1_exception_test() {
        assertThatThrownBy(() -> Assert.notNull(null, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notNull AssertTest")
    public void notNull2_exception_test() {
        assertThatThrownBy(() -> Assert.notNull(null, ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty1_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty("", ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty2_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty("", ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty1_collection_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty(List.of(), ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty2_collection_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty(List.of(), ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty1_map_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty(Map.of(), ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty2_map_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty(Map.of(), ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty1_array_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty(List.of().toArray(), ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while notEmpty AssertTest")
    public void notEmpty2_array_exception_test() {
        assertThatThrownBy(() -> Assert.notEmpty(List.of().toArray(), ErrorCode.ADD_STATUS_CODE_DOC_ERROR, MESSAGE))
            .isInstanceOf(ApiTestPlatformException.class);
    }
}
