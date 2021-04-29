package com.sms.satp.mapper;

import com.sms.satp.entity.ApiLabel;
import com.sms.satp.dto.ApiLabelDto;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiLabelMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ApiLabelDto toDto(ApiLabel apiLabel);

    List<ApiLabelDto> toDtoList(List<ApiLabel> apiLabels);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    ApiLabel toEntity(ApiLabelDto apiLabelDto);

}