package com.sms.courier.generator;

import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.pojo.StringFiles;
import java.util.List;

public interface GeneratorStrategy {

    List<StringFiles> generate(CodeGenRequest request, ApiResponse apiEntity, GeneratorTemplateEntity templateEntity);

}
