package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.DISABLE_EMAIL_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.ENABLE_EMAIL_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPDATE_EMAIL_CONFIGURATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.chat.sender.Sender;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.mapper.EmailServiceMapper;
import com.sms.courier.repository.EmailServiceRepository;
import com.sms.courier.service.impl.EmailSettingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for EmailSettingService")
public class EmailSettingServiceTest {

    private final Sender sender = mock(Sender.class);
    private final EmailService emailService = mock(EmailService.class);
    private final EmailServiceMapper mapper = mock(EmailServiceMapper.class);
    private final EmailServiceRepository repository = mock(EmailServiceRepository.class);
    private final EmailSettingService service = new EmailSettingServiceImpl(sender, emailService, mapper, repository);

    @Test
    void update_email_configuration() {
        EmailRequest request = mock(EmailRequest.class);
        doNothing().when(repository).deleteAll();
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(mapper.toEntity(request)).thenReturn(entity);
        doNothing().when(sender).updateConfiguration();
        assertThat(service.updateEmailConfiguration(request)).isTrue();
    }

    @Test
    void update_email_configuration_exception() {
        EmailRequest request = mock(EmailRequest.class);
        doThrow(RuntimeException.class).when(repository).deleteAll();
        assertThatThrownBy(() -> service.updateEmailConfiguration(request))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(UPDATE_EMAIL_CONFIGURATION_ERROR.getCode());
    }

    @Test
    void enable_test() {
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(emailService.getEmailServiceEntity()).thenReturn(entity);
        assertThat(service.enable()).isTrue();
    }

    @Test
    void enable_exception_test() {
        when(emailService.getEmailServiceEntity()).thenThrow(RuntimeException.class);
        assertThatThrownBy(service::enable)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ENABLE_EMAIL_SERVICE_ERROR.getCode());
    }

    @Test
    void disable_test() {
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(emailService.getEmailServiceEntity()).thenReturn(entity);
        assertThat(service.disable()).isTrue();
    }

    @Test
    void disable_exception_test() {
        when(emailService.getEmailServiceEntity()).thenThrow(RuntimeException.class);
        assertThatThrownBy(service::disable)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DISABLE_EMAIL_SERVICE_ERROR.getCode());
    }
}
