package com.sms.courier.service;

import com.sms.courier.dto.request.GeneratorTemplateRequest;
import com.sms.courier.dto.response.GeneratorTemplateResponse;
import com.sms.courier.dto.response.GeneratorTemplateTypeResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import java.util.List;

public interface GeneratorTemplateService {

    GeneratorTemplateEntity findById(String id);

    Boolean add(GeneratorTemplateRequest generatorTemplateRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(GeneratorTemplateRequest generatorTemplateRequest);

    List<GeneratorTemplateResponse> list(String projectId);

    List<GeneratorTemplateTypeResponse> getAllType();

}
