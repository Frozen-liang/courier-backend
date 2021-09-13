package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.dto.request.NotificationTemplateRequest;
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
}