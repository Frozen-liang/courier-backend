package com.sms.satp.mapper;

import com.sms.satp.dto.request.AddSceneCaseGroupRequest;
import com.sms.satp.dto.request.UpdateSceneCaseGroupRequest;
import com.sms.satp.dto.response.SceneCaseGroupResponse;
import com.sms.satp.entity.group.SceneCaseGroup;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseGroupMapper {

    SceneCaseGroup toSceneCaseGroupByAdd(AddSceneCaseGroupRequest request);

    SceneCaseGroup toSceneCaseGroupByUpdate(UpdateSceneCaseGroupRequest request);

    List<SceneCaseGroupResponse> toResponseList(List<SceneCaseGroup> sceneCaseGroups);
}
