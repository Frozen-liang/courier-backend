package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.util.DigestUtils;

@DisplayName("Tests for MD5Util")
public class MD5UtilTest {

    private static final String TEST = "test";
    private static final MockedStatic<DigestUtils> digestUtilsMockedStatic;

    static {
        digestUtilsMockedStatic = Mockito.mockStatic(DigestUtils.class);
    }

    @Test
    @DisplayName("Test the encodeJwt method in the MD5Util")
    public void getMD5_test() throws IOException {
        String json = JsonUtils.serializeObject(TEST);
        String result = DigestUtils.md5DigestAsHex(json.getBytes(StandardCharsets.UTF_8));
        assertThat(MD5Util.getMD5(TEST)).isEqualTo(result);
    }

    @Test
    @DisplayName("An exception occurred while getMD5 MD5Util")
    public void getMD5_exception_test() throws IOException {
        digestUtilsMockedStatic.when(() -> DigestUtils.md5DigestAsHex(any(byte[].class)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> MD5Util.getMD5(TEST)).isInstanceOf(RuntimeException.class);
    }
}
