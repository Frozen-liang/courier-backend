package com.sms.courier.mapper;

import com.sms.courier.dto.request.GeneratorTemplateRequest;
import com.sms.courier.dto.response.GeneratorTemplateResponse;
import com.sms.courier.dto.response.GeneratorTemplateTypeResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.entity.generator.GeneratorTemplateTypeEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class)
public interface GeneratorTemplateMapper {

    GeneratorTemplateEntity toEntity(GeneratorTemplateRequest generatorTemplateRequest);

    List<GeneratorTemplateResponse> toResponseList(List<GeneratorTemplateEntity> entityList);

    List<GeneratorTemplateTypeResponse> toTypeList(List<GeneratorTemplateTypeEntity> typeEntities);

    @Mapping(target = "name", expression = "java(typeEntity.getTemplateType().getName())")
    GeneratorTemplateTypeResponse toType(GeneratorTemplateTypeEntity typeEntity);
}
