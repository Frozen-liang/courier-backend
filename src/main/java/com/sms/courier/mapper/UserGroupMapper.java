package com.sms.courier.mapper;

import com.sms.courier.dto.request.UserGroupRequest;
import com.sms.courier.dto.response.UserGroupResponse;
import com.sms.courier.entity.system.UserGroupEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupMapper {

    UserGroupResponse toDto(UserGroupEntity userGroup);

    List<UserGroupResponse> toDtoList(List<UserGroupEntity> userGroupList);

    UserGroupEntity toEntity(UserGroupRequest userGroupRequest);
}