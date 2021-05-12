package com.sms.satp.mapper;

import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import com.sms.satp.entity.tag.ApiTag;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ApiTagMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "tagType", expression = "java(apiTag.getTagType().getCode())")
    ApiTagResponse toDto(ApiTag apiTag);

    List<ApiTagResponse> toDtoList(List<ApiTag> apiTags);

    @Mapping(target = "tagType",
        expression = "java(com.sms.satp.common.enums.ApiTagType.getType(apiTagDto.getTagType()))")
    ApiTag toEntity(ApiTagRequest apiTagDto);

}