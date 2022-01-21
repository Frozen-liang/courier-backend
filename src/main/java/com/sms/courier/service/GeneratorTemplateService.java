package com.sms.courier.service;

import com.sms.courier.dto.request.AddGeneratorTemplateRequest;
import com.sms.courier.dto.request.UpdateGeneratorTemplateRequest;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import java.util.List;

public interface GeneratorTemplateService {

    GeneratorTemplateEntity findById(String id);

    Boolean add(AddGeneratorTemplateRequest addGeneratorTemplateRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateGeneratorTemplateRequest updateGeneratorTemplateRequest);


}
