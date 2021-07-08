package com.sms.satp.mapper;

import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.dto.request.ApiTagListRequest;
import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import com.sms.satp.entity.tag.ApiTag;
import com.sms.satp.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, uses = EnumCommonUtils.class, imports = ApiTagType.class)
public interface ApiTagMapper {

    ApiTagResponse toDto(ApiTag apiTag);

    List<ApiTagResponse> toDtoList(List<ApiTag> apiTags);

    ApiTag toEntity(ApiTagRequest apiTagDto);

    @Mapping(target = "tagType", expression = "java(ApiTagType.getType(apiTagListRequest.getTagType()))")
    ApiTag listRequestToApiTag(ApiTagListRequest apiTagListRequest);
}