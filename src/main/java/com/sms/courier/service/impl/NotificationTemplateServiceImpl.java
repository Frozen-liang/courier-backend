package com.sms.courier.service.impl;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.entity.notification.NotificationTemplateEntity;
import com.sms.courier.mapper.NotificationTemplateMapper;
import com.sms.courier.repository.NotificationTemplateRepository;
import com.sms.courier.service.NotificationTemplateService;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateMapper mapper;
    private final NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplateServiceImpl(
        NotificationTemplateMapper mapper,
        NotificationTemplateRepository notificationTemplateRepository) {
        this.mapper = mapper;
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Override
    public NotificationTemplateEntity findTemplateByType(NotificationTemplateType type) {
        return notificationTemplateRepository.findByType(type.getValue());
    }

    @Override
    public boolean save(NotificationTemplateRequest request) {
        NotificationTemplateEntity newEntity = mapper.toEntity(request);
        NotificationTemplateEntity oldEntity = notificationTemplateRepository.findByType(request.getType());
        if (Objects.nonNull(oldEntity)) {
            newEntity.setId(oldEntity.getId());
        }
        notificationTemplateRepository.save(newEntity);
        return true;
    }
}