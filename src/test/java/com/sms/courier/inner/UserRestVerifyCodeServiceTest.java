package com.sms.courier.inner;

import static org.mockito.Mockito.spy;

import com.sms.courier.inner.impl.UserResetVerifyCodeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserRestVerifyCodeService")
public class UserRestVerifyCodeServiceTest {

    private final UserRestVerifyCodeService service = spy(UserResetVerifyCodeServiceImpl.class);

    @Test
    @DisplayName("Test the verifyCode method in UserRestVerifyCodeService")
    void verifyCode_test() {
    }

    @Test
    @DisplayName("Test the generateCode method in UserRestVerifyCodeService")
    void generateCode_test() {

    }

    @Test
    @DisplayName("Test the verifyIfProcessing method in UserRestVerifyCodeService When a recent email has been sent ")
    void verifyIfProcessing_exception_test() {

    }

    @Test
    @DisplayName("Test the verifyIfProcessing method in UserRestVerifyCodeService")
    void verifyIfProcessing() {

    }
}