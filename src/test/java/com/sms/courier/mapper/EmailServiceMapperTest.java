package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.courier.config.EmailProperties;
import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.utils.AesUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

@DisplayName("Tests for EmailServiceMapper")
public class EmailServiceMapperTest {

    private EmailServiceMapper mapper = new EmailServiceMapperImpl();
    private static final MockedStatic<AesUtil> AES_UTIL_MOCKED_STATIC;

    static {
        AES_UTIL_MOCKED_STATIC = mockStatic(AesUtil.class);
    }

    @AfterAll
    public static void close() {
        AES_UTIL_MOCKED_STATIC.close();
    }

    @Test
    void to_entity_test() {
        String pwd = "pwd";
        EmailRequest request = mock(EmailRequest.class);
        EmailProperties properties = mock(EmailProperties.class);
        when(request.getProperties()).thenReturn(properties);
        when(properties.getPassword()).thenReturn(pwd);
        AES_UTIL_MOCKED_STATIC.when(() -> AesUtil.encrypt(pwd)).thenReturn(pwd);
        EmailServiceEntity entity = mapper.toEntity(request);
        assertThat(entity.getProperties().getPassword()).isEqualTo(pwd);
    }

    @Test
    void null_to_entity_test() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void to_response_test() {
        String username = "username";
        EmailServiceEntity entity = mock(EmailServiceEntity.class);
        EmailProperties property = mock(EmailProperties.class);
        when(entity.getProperties()).thenReturn(property);
        when(property.getUsername()).thenReturn(username);
        assertThat(mapper.toResponse(entity).getProperties().getUsername()).isEqualTo(username);
    }

    @Test
    void null_to_response_test() {
        assertThat(mapper.toResponse(null)).isNull();
    }


}