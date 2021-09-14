package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.GET_NOTIFICATION_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPDATE_NOTIFICATION_TEMPLATE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.dto.response.NotificationTemplateResponse;
import com.sms.courier.entity.notification.NotificationTemplateEntity;
import com.sms.courier.mapper.NotificationTemplateMapper;
import com.sms.courier.repository.NotificationTemplateRepository;
import com.sms.courier.service.impl.NotificationTemplateServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for NotificationTemplateService")
public class NotificationTemplateServiceTest {

    NotificationTemplateMapper mapper = mock(NotificationTemplateMapper.class);
    NotificationTemplateRepository repository = mock(NotificationTemplateRepository.class);
    private final NotificationTemplateService notificationTemplateService =
        new NotificationTemplateServiceImpl(mapper, repository);

    @Test
    @DisplayName("Test the findTemplateByModuleAction method in NotificationTemplateService")
    public void find_template_by_type_test() {
        Integer typeValue = 1;
        NotificationTemplateEntity entity = mock(NotificationTemplateEntity.class);
        NotificationTemplateType type = mock(NotificationTemplateType.class);
        when(type.getValue()).thenReturn(typeValue);
        when(repository.findByType(typeValue)).thenReturn(entity);
        assertThat(notificationTemplateService.findTemplateByType(type)).isEqualTo(entity);
    }

    @Test
    @DisplayName("Test the save method in NotificationTemplateService")
    public void save_test() {
        Integer typeValue = 1;
        NotificationTemplateRequest request = mock(NotificationTemplateRequest.class);
        when(request.getType()).thenReturn(typeValue);
        NotificationTemplateEntity entity = mock(NotificationTemplateEntity.class);
        doReturn(entity).when(mapper).toEntity(request);
        when(repository.findByType(typeValue)).thenReturn(entity);
        doReturn(entity).when(repository).save(entity);
        assertThat(notificationTemplateService.save(request)).isEqualTo(true);
    }

    @Test
    public void save_exception_test() {
        NotificationTemplateRequest request = mock(NotificationTemplateRequest.class);
        when(mapper.toEntity(request)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> notificationTemplateService.save(request))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(UPDATE_NOTIFICATION_TEMPLATE_ERROR.getCode());
    }

    @Test
    public void get_response_by_type_test() {
        Integer typeValue = 1;
        NotificationTemplateResponse response = mock(NotificationTemplateResponse.class);
        NotificationTemplateEntity entity = mock(NotificationTemplateEntity.class);
        when(repository.findByType(typeValue)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);
        assertThat(notificationTemplateService.getResponseByType(typeValue)).isEqualTo(response);
    }

    @Test
    public void get_response_by_type_exception_test() {
        Integer typeValue = 1;
        when(repository.findByType(typeValue)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> notificationTemplateService.getResponseByType(typeValue))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_NOTIFICATION_TEMPLATE_ERROR.getCode());

    }
}