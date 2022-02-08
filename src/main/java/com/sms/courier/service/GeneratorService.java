package com.sms.courier.service;

import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.generator.pojo.FilePackageVo;
import java.util.List;

public interface GeneratorService {

    List<FilePackageVo> generator(CodeGenRequest request);
}
