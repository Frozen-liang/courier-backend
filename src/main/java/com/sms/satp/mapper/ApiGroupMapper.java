package com.sms.satp.mapper;

import com.sms.satp.dto.request.ApiGroupRequest;
import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.entity.group.ApiGroupEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiGroupMapper {

    List<ApiGroupResponse> toResponse(List<ApiGroupEntity> apiGroupEntityList);

    ApiGroupEntity toEntity(ApiGroupRequest request);
}
