package com.sms.courier.mapper;

import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.utils.EnumCommonUtils;
import com.sms.courier.webhook.model.WebhookEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface WebhookMapper {

    WebhookEntity toEntity(WebhookRequest webhookRequest);
}
