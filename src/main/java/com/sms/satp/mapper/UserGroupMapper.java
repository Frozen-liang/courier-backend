package com.sms.satp.mapper;

import com.sms.satp.dto.request.UserGroupRequest;
import com.sms.satp.dto.response.UserGroupResponse;
import com.sms.satp.entity.system.UserGroupEntity;
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