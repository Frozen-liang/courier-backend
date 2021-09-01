package com.sms.courier.mapper;

import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.entity.notification.NotificationTemplateEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationTemplateMapper {

    NotificationTemplateEntity toEntity(NotificationTemplateRequest request);
}
