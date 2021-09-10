package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.chat.sender.Sender;
import com.sms.courier.dto.request.PasswordResetByEmailRequest;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.inner.UserRestVerifyCodeService;
import com.sms.courier.service.impl.UserComplexServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class UserComplexServiceTest {

    private final Sender sender = mock(Sender.class);
    private final UserService userService = mock(UserService.class);
    private final UserRestVerifyCodeService userRestVerifyCodeService = mock(UserRestVerifyCodeService.class);
    private final UserComplexServiceImpl userComplexService = new UserComplexServiceImpl(
        userService, sender, userRestVerifyCodeService);

    @Test
    void send_reset_email_test(){
        String username = "username";
        doNothing().when(userRestVerifyCodeService).verifyIfProcessing(anyString());
        UserEntity entity = mock(UserEntity.class);
        when(entity.getUsername()).thenReturn(username);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(entity));
        when(userRestVerifyCodeService.generateCode(anyString())).thenReturn("1");
        when(sender.sendResetPwdNotification(any(NotificationPayload.class))).thenReturn(true);
        assertThat(userComplexService.sendResetEmail("")).isTrue();
    }

    @Test
    void reset_pwd_by_email_test(){
        PasswordResetByEmailRequest passwordResetByEmailRequest = new PasswordResetByEmailRequest();
        passwordResetByEmailRequest.setEmail("a");
        passwordResetByEmailRequest.setPassword("bfd");
        when(userRestVerifyCodeService.verifyCode(any(), any())).thenReturn(true);
        when(userService.setPasswordByEmail(anyString(), anyString())).thenReturn(true);
        assertThat(userComplexService.resetPwdByEmail(passwordResetByEmailRequest)).isTrue();
    }
}
