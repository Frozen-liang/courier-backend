package com.sms.satp.mapper;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import com.sms.satp.entity.group.ApiTagGroup;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiTagGroupMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = TimePatternConstant.DEFAULT_PATTERN)
    ApiTagGroupResponse toDto(ApiTagGroup apiTagGroup);

    List<ApiTagGroupResponse> toDtoList(List<ApiTagGroup> apiTagGroupList);

    ApiTagGroup toEntity(ApiTagGroupRequest apiTagGroupRequest);
}