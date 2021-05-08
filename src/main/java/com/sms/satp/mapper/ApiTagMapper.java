package com.sms.satp.mapper;

import com.sms.satp.dto.ApiTagDto;
import com.sms.satp.entity.tag.ApiTag;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiTagMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ApiTagDto toDto(ApiTag apiTag);

    List<ApiTagDto> toDtoList(List<ApiTag> apiTags);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    ApiTag toEntity(ApiTagDto apiTagDto);

}