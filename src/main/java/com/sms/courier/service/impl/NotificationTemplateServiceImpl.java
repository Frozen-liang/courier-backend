package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_NOTIFICATION_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPDATE_NOTIFICATION_TEMPLATE_ERROR;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.dto.response.NotificationTemplateResponse;
import com.sms.courier.entity.notification.NotificationTemplateEntity;
import com.sms.courier.mapper.NotificationTemplateMapper;
import com.sms.courier.repository.NotificationTemplateRepository;
import com.sms.courier.service.NotificationTemplateService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
        try {
            NotificationTemplateEntity newEntity = mapper.toEntity(request);
            NotificationTemplateEntity oldEntity = notificationTemplateRepository.findByType(request.getType());
            if (Objects.nonNull(oldEntity)) {
                newEntity.setId(oldEntity.getId());
            }
            notificationTemplateRepository.save(newEntity);
            return true;
        } catch (Exception exception) {
            log.error(UPDATE_NOTIFICATION_TEMPLATE_ERROR.getMessage(), exception);
            throw new ApiTestPlatformException(UPDATE_NOTIFICATION_TEMPLATE_ERROR);
        }
    }

    @Override
    public NotificationTemplateResponse getResponseByType(Integer templateType) {
        try {
            return mapper.toResponse(notificationTemplateRepository.findByType(templateType));
        } catch (Exception exception) {
            log.error(GET_NOTIFICATION_TEMPLATE_ERROR.getMessage(), exception);
            throw new ApiTestPlatformException(GET_NOTIFICATION_TEMPLATE_ERROR);
        }
    }
}