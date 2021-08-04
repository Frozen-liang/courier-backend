package com.sms.courier.mapper;

import com.sms.courier.dto.request.SceneCaseGroupRequest;
import com.sms.courier.dto.response.SceneCaseGroupResponse;
import com.sms.courier.entity.group.SceneCaseGroupEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseGroupMapper {

    SceneCaseGroupEntity toSceneCaseGroupEntity(SceneCaseGroupRequest request);

    List<SceneCaseGroupResponse> toResponse(List<SceneCaseGroupEntity> sceneCaseGroupEntityList);

}
