package com.sms.courier.mapper;

import com.sms.courier.dto.request.UserQueryListRequest;
import com.sms.courier.dto.request.UserRequest;
import com.sms.courier.dto.response.UserProfileResponse;
import com.sms.courier.dto.response.UserResponse;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.security.pojo.CustomUser;
import java.time.LocalDate;
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
    @Mapping(target = "expired", expression = "java(isExpired(customUser.getExpiredDate()))")
    UserProfileResponse toUserResponse(CustomUser customUser);

    default boolean isExpired(LocalDate expiredDate) {
        if (expiredDate == null) {
            return true;
        }
        return expiredDate.isAfter(LocalDate.now());
    }
}