package com.sms.courier.service.impl;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.CodeGeneratorContext;
import com.sms.courier.generator.pojo.StringFiles;
import com.sms.courier.service.ApiService;
import com.sms.courier.service.GeneratorService;
import com.sms.courier.service.GeneratorTemplateService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.DataStructureUtil;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.ZipUtil;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    private final ApiService apiService;
    private final CodeGeneratorContext codeGeneratorContext;
    private final GeneratorTemplateService generatorTemplateService;

    public GeneratorServiceImpl(ApiService apiService,
        CodeGeneratorContext codeGeneratorContext,
        GeneratorTemplateService generatorTemplateService) {
        this.apiService = apiService;
        this.codeGeneratorContext = codeGeneratorContext;
        this.generatorTemplateService = generatorTemplateService;
    }

    @Override
    public void generator(OutputStream outputStream, CodeGenRequest request) {
        try {
            ApiResponse apiResponse = apiService.findById(request.getApiId());
            GeneratorTemplateEntity templateEntity = generatorTemplateService.findById(request.getTemplateId());
            Assert.isTrue(Objects.nonNull(apiResponse), ErrorCode.API_CAN_NOT_BE_NULL);
            Assert.isTrue(Objects.nonNull(templateEntity), ErrorCode.TEMPLATE_CAN_NOT_BE_NULL);

            List<StringFiles> fileList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(apiResponse.getRequestParams())) {
                apiResponse.setRequestParams(DataStructureUtil.resetApiRequest(apiResponse.getRequestParams()));
            }
            if (CollectionUtils.isNotEmpty(apiResponse.getResponseParams())) {
                apiResponse.setResponseParams(DataStructureUtil.resetApiRequest(apiResponse.getResponseParams()));
            }

            List<StringFiles> requestFile =
                codeGeneratorContext.getGeneratorStrategy(templateEntity.getCodeType().getCode()).generate(request,
                    apiResponse, templateEntity);
            fileList.addAll(requestFile);
            ZipUtil.compressionFileByString(outputStream, fileList, request.getPackageName());
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to generate code!", e);
            throw ExceptionUtils.mpe(ErrorCode.GENERATE_CODE_ERROR);
        }
    }

}
