package com.sms.courier.mapper;

import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookTypeResponse;
import com.sms.courier.utils.EnumCommonUtils;
import com.sms.courier.webhook.model.WebhookEntity;
import com.sms.courier.webhook.model.WebhookTypeEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface WebhookMapper {

    WebhookEntity toEntity(WebhookRequest webhookRequest);

    @Mapping(target = "name", expression = "java(webhookRequest.getType().getName())")
    WebhookTypeResponse toWebhookType(WebhookTypeEntity webhookRequest);

    List<WebhookTypeResponse> toWebhookTypeList(List<WebhookTypeEntity> webhookRequest);
}
