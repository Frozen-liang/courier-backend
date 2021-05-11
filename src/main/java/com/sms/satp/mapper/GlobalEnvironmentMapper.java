package com.sms.satp.mapper;

import com.sms.satp.dto.GlobalEnvironmentRequest;
import com.sms.satp.dto.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ParamInfoMapper.class)
public interface GlobalEnvironmentMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    GlobalEnvironmentResponse toDto(GlobalEnvironment globalEnvironment);

    List<GlobalEnvironmentResponse> toDtoList(List<GlobalEnvironment> globalEnvironments);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    GlobalEnvironment toEntity(GlobalEnvironmentRequest globalEnvironmentDto);
}
