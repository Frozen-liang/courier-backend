package com.sms.satp.mapper;

import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import com.sms.satp.entity.group.ApiTagGroup;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiTagGroupMapper {

    ApiTagGroupResponse toDto(ApiTagGroup apiTagGroup);

    List<ApiTagGroupResponse> toDtoList(List<ApiTagGroup> apiTagGroupList);

    ApiTagGroup toEntity(ApiTagGroupRequest apiTagGroupRequest);
}