package com.sms.satp.mapper;

import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import com.sms.satp.entity.env.GlobalEnvironment;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GlobalEnvironmentMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    GlobalEnvironmentDto toDto(GlobalEnvironment globalEnvironment);

    List<GlobalEnvironmentDto> toDtoList(List<GlobalEnvironment> globalEnvironments);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    GlobalEnvironment toEntity(GlobalEnvironmentDto globalEnvironmentDto);
}
