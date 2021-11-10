package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import java.time.LocalDate;
import java.util.Collections;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

@DisplayName("Tests for UserDestinationUtil")
public class UserDestinationUtilTest {

    private final static MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;
    private final static String USER_ID = ObjectId.get().toString();


    static {
        SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(USER_ID);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(new CustomUser("username", "password",
            Collections.emptyList(), USER_ID, "username@qq.com", "nickname", TokenType.USER, LocalDate.now()));
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    private static final String ID = "test";

    @Test
    @DisplayName("Test the getCaseDest method in the UserDestinationUtil")
    public void getCaseDest_test() {
        assertThat(UserDestinationUtil.getCaseDest(ID)).isEqualTo("/user/case/test");
    }

    @Test
    @DisplayName("Test the getSceneCaseDest method in the UserDestinationUtil")
    public void getSceneCaseDest_test() {
        assertThat(UserDestinationUtil.getSceneCaseDest(ID)).isEqualTo("/user/scene/case/test");
    }

    @Test
    @DisplayName("Test the getProjectDest method in the UserDestinationUtil")
    public void getProjectDest_test() {
        assertThat(UserDestinationUtil.getProjectDest(ID)).isEqualTo("/user/project/test");
    }

    @Test
    @DisplayName("Test the getProjectDest method in the UserDestinationUtil")
    public void getLogDest_test() {
        assertThat(UserDestinationUtil.getLogDest(ID)).isEqualTo("/user/log/" + USER_ID + "/test");
    }

    @Test
    @DisplayName("Test the getProjectDest method in the UserDestinationUtil")
    public void getDockerDest_test() {
        assertThat(UserDestinationUtil.getDockerDest("engine")).isEqualTo("/user/engine/message/" + USER_ID);
    }
}
