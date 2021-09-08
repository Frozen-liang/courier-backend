package com.sms.courier.chat.sender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.chat.common.AdditionalParam;
import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.chat.sender.impl.EmailSender;
import com.sms.courier.config.EmailProperties;
import com.sms.courier.dto.response.EmailPropertiesResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.entity.notification.NotificationTemplateEntity;
import com.sms.courier.service.EmailService;
import com.sms.courier.service.NotificationTemplateService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

public class EmailSenderTest {

    private final EmailService emailService = mock(EmailService.class);
    private final JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
    private final SpringTemplateEngine engine = mock(SpringTemplateEngine.class);
    private final NotificationTemplateService templateService = mock(NotificationTemplateService.class);
    private final EmailSender emailSender = new EmailSender(javaMailSender, emailService, engine, templateService);

    @Test
    void update_test() {
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        EmailProperties properties = mock(EmailProperties.class);
        when(entity.getProperties()).thenReturn(properties);
        when(emailService.getEntityWithDecryptPwd()).thenReturn(entity);
        emailSender.updateConfiguration();
        verify(emailService).getEntityWithDecryptPwd();
    }

    @Test
    void validate_connection_normal_test() throws MessagingException {
        doNothing().when(javaMailSender).testConnection();
        assertThat(emailSender.validateConnection()).isEqualTo(true);
    }

    @Test
    void validate_connection_exception_test() throws MessagingException {
        doThrow(RuntimeException.class).when(javaMailSender).testConnection();
        assertThat(emailSender.validateConnection()).isEqualTo(false);
    }

    @Test
    void send_reset_pwd_notification() throws MessagingException {
        doNothing().when(javaMailSender).testConnection();
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(entity.getEnabled()).thenReturn(Boolean.FALSE);
        emailSender.afterPropertiesSet();

        String title = "title";
        String content = "content";
        String titleVariableKey = "titleVariableKey";
        NotificationTemplateEntity templateEntity = mock(NotificationTemplateEntity.class);
        when(templateEntity.getTitleVariableKey()).thenReturn(titleVariableKey);
        when(templateEntity.getTitle()).thenReturn(title);
        when(templateEntity.getContent()).thenReturn(content);
        when(templateService.findTemplateByType(NotificationTemplateType.ACCOUNT_PWD_RESET)).thenReturn(templateEntity);

        String processResult = "result";
        when(engine.process(any(String.class), any(Context.class))).thenReturn(processResult);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        String from = "from";
        EmailPropertiesResponse response = mock(EmailPropertiesResponse.class);
        when(emailService.getEmailConfigurationResponse()).thenReturn(response);
        when(response.getUsername()).thenReturn(from);

        Map<AdditionalParam, Object> additionalParam = new HashMap<>();
        additionalParam.put(AdditionalParam.EMAIL_TO, Arrays.asList("to1", "to2"));
        additionalParam.put(AdditionalParam.EMAIL_CC, Arrays.asList("cc1", "cc2"));

        Map<String, String> inlinesOrAttachmentMap = new HashMap<>();
        inlinesOrAttachmentMap.put("key", "value");
        additionalParam.put(AdditionalParam.EMAIL_INLINES, inlinesOrAttachmentMap);
        additionalParam.put(AdditionalParam.EMAIL_ATTACHMENT, inlinesOrAttachmentMap);

        NotificationPayload payload = NotificationPayload.builder().additionalParam(additionalParam).build();

        assertThat(emailSender.sendResetPwdNotification(payload)).isTrue();
    }

    @Test
    void after_properties_set_test() throws MessagingException {
        doNothing().when(javaMailSender).testConnection();
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(entity.getEnabled()).thenReturn(Boolean.TRUE);
        when(emailService.getEntityWithDecryptPwd()).thenReturn(entity);
        emailSender.afterPropertiesSet();
    }

    @Test
    void after_properties_set_exception_test() throws MessagingException {
        doNothing().when(javaMailSender).testConnection();
        when(emailService.getEntityWithDecryptPwd()).thenThrow(RuntimeException.class);
        emailSender.afterPropertiesSet();
    }
}
