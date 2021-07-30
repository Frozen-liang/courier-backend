package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.util.DigestUtils;

@DisplayName("Tests for MD5Util")
public class MD5UtilTest {

    private static final String TEST = "test";
    private static final MockedStatic<DigestUtils> digestUtilsMockedStatic;
    private static final MockedStatic<JsonUtils> JSON_UTILS_MOCKED_STATIC;

    static {
        digestUtilsMockedStatic = Mockito.mockStatic(DigestUtils.class);
        JSON_UTILS_MOCKED_STATIC = Mockito.mockStatic(JsonUtils.class);
    }

    @AfterAll
    public static void close() {
        digestUtilsMockedStatic.close();
        JSON_UTILS_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the encodeJwt method in the MD5Util")
    public void getMD5_test() throws IOException {
        String json = "JSON";
        String result = "RESULT";
        JSON_UTILS_MOCKED_STATIC.when(() -> JsonUtils.serializeObject(TEST)).thenReturn(json);
        digestUtilsMockedStatic.when(() -> DigestUtils.md5DigestAsHex(json.getBytes(StandardCharsets.UTF_8)))
            .thenReturn(result);
        assertThat(MD5Util.getMD5(TEST)).isEqualTo(result);
    }

    @Test
    @DisplayName("An exception occurred while getMD5 MD5Util")
    public void getMD5_exception_test() throws IOException {
        String json = "JSON";
        JSON_UTILS_MOCKED_STATIC.when(() -> JsonUtils.serializeObject(TEST)).thenReturn(json);
        digestUtilsMockedStatic.when(() -> DigestUtils.md5DigestAsHex(json.getBytes(StandardCharsets.UTF_8)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> MD5Util.getMD5(TEST)).isInstanceOf(RuntimeException.class);
    }
}
