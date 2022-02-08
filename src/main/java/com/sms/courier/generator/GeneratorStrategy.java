package com.sms.courier.generator;

import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.pojo.FilePackageVo;
import java.util.List;

public interface GeneratorStrategy {

    List<FilePackageVo> generate(CodeGenRequest request, ApiResponse apiEntity, GeneratorTemplateEntity templateEntity);

}
