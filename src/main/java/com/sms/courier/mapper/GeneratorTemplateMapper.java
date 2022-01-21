package com.sms.courier.mapper;

import com.sms.courier.dto.request.AddGeneratorTemplateRequest;
import com.sms.courier.dto.request.UpdateGeneratorTemplateRequest;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeneratorTemplateMapper {

    GeneratorTemplateEntity toGeneratorTemplateByAddRequest(AddGeneratorTemplateRequest addGeneratorTemplateRequest);

    GeneratorTemplateEntity toGeneratorTemplateByEditRequest(
            UpdateGeneratorTemplateRequest updateGeneratorTemplateRequest);

}
