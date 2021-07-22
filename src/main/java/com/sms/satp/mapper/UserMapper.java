package com.sms.satp.mapper;

import com.sms.satp.dto.request.UserQueryListRequest;
import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.security.pojo.CustomUser;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {GrantedAuthority.class, Collectors.class})
public interface UserMapper {

    UserResponse toDto(UserEntity user);

    List<UserResponse> toDtoList(List<UserEntity> userList);

    UserEntity toEntity(UserRequest userRequest);

    UserEntity toEntity(UserQueryListRequest userRequest);

    @Mapping(target = "roles",
        expression = "java(customUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect"
            + "(Collectors.toList()))")
    UserResponse toUserResponse(CustomUser customUser);
}