package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.config.EmailProperties;
import com.sms.courier.dto.response.EmailPropertiesResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.mapper.EmailServiceMapper;
import com.sms.courier.repository.EmailServiceRepository;
import com.sms.courier.service.impl.EmailServiceImpl;
import com.sms.courier.utils.AesUtil;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class EmailServiceTest {

    private final EmailServiceMapper mapper = mock(EmailServiceMapper.class);
    private final EmailServiceRepository repository = mock(EmailServiceRepository.class);
    private final EmailService emailService = new EmailServiceImpl(mapper, repository);
    private static final MockedStatic<AesUtil> AES_UTIL_MOCKED_STATIC;

    static {
        AES_UTIL_MOCKED_STATIC = mockStatic(AesUtil.class);
    }

    @AfterAll
    public static void close() {
        AES_UTIL_MOCKED_STATIC.close();
    }

    @Test
    void get_email_service_entity_test() {
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        EmailServiceEntity result = emailService.getEmailServiceEntity();
        assertThat(result).isEqualTo(entity);
    }

    @Test
    void get_email_service_entity_empty_test() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(emailService::getEmailServiceEntity)
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    void get_email_configuration_response() {
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        EmailProperties properties = mock(EmailProperties.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        when(entity.getProperties()).thenReturn(properties);
        EmailPropertiesResponse response = mock(EmailPropertiesResponse.class);
        when(mapper.toResponse(properties)).thenReturn(response);
        assertThat(emailService.getEmailConfigurationResponse()).isEqualTo(response);
    }

    @Test
    void get_entity_with_decrypt_pwd_test() {
        String secretPwd = "secret-pwd";
        String pwd = "pwd";
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));

        EmailProperties properties = EmailProperties.builder().password(secretPwd).build();
        when(entity.getProperties()).thenReturn(properties);
        AES_UTIL_MOCKED_STATIC.when(() -> AesUtil.decrypt(secretPwd)).thenReturn(pwd);
        EmailServiceEntity result = emailService.getEntityWithDecryptPwd();
        assertThat(result).isEqualTo(entity);
        assertThat(result.getProperties().getPassword()).isEqualTo(pwd);
    }

    @Test
    void is_service_enabled_test() {
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        when(entity.getEnabled()).thenReturn(Boolean.TRUE);
        assertThat(emailService.isServiceEnabled()).isEqualTo(Boolean.TRUE);
    }
}
