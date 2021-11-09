package com.sms.courier.mapper;

import com.sms.courier.docker.enmu.LabelType;
import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {EnumCommonUtils.class}, imports = LabelType.class)
public interface MockSettingMapper {

    MockSettingEntity toEntity(MockSettingRequest mockSettingRequest);

    MockSettingResponse toResponse(MockSettingEntity mockSettingEntity);

    @Mapping(target = "destination", constant = "/user/mock/message")
    @Mapping(target = "labelType", expression = "java(LabelType.MOCK)")
    ContainerInfo toContainerSetting(MockSettingEntity mockSettingEntity);
}
