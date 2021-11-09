package com.sms.courier.mapper;

import com.sms.courier.docker.enmu.LabelType;
import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class, imports = LabelType.class)
public interface EngineMapper {

    EngineResponse toResponse(EngineMemberEntity entity);

    List<EngineResponse> toResponseList(List<EngineMemberEntity> entityList);

    @Mapping(target = "destination", constant = "/user/engine/message")
    @Mapping(target = "labelType", expression = "java(LabelType.ENGINE)")
    ContainerInfo toContainerSetting(EngineSettingResponse engineSettingResponse);
}