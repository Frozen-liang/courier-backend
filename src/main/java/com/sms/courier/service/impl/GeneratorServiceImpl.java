package com.sms.courier.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.CodeGeneratorContext;
import com.sms.courier.generator.ReplaceHelper;
import com.sms.courier.generator.pojo.StringFiles;
import com.sms.courier.service.ApiService;
import com.sms.courier.service.GeneratorService;
import com.sms.courier.service.GeneratorTemplateService;
import com.sms.courier.utils.Assert;
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
    private final ObjectMapper objectMapper;
    private final CodeGeneratorContext codeGeneratorContext;
    private final GeneratorTemplateService generatorTemplateService;

    public GeneratorServiceImpl(ApiService apiService,
        ObjectMapper objectMapper,
        CodeGeneratorContext codeGeneratorContext,
        GeneratorTemplateService generatorTemplateService) {
        this.apiService = apiService;
        this.objectMapper = objectMapper;
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
                apiResponse.setRequestParams(resetApiRequest(apiResponse.getRequestParams()));
            }
            if (CollectionUtils.isNotEmpty(apiResponse.getResponseParams())) {
                apiResponse.setResponseParams(resetApiRequest(apiResponse.getResponseParams()));
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

    private List<ParamInfoResponse> resetApiRequest(List<ParamInfoResponse> responseList) {
        try {
            String jsonString = objectMapper.writeValueAsString(responseList);
            String responseStr = new ReplaceHelper(jsonString).replaceCustomStruct().toInfoList();

            List<ParamInfoResponse> paramInfoResponseList = objectMapper
                .readValue(responseStr, new TypeReference<List<ParamInfoResponse>>() {
                });

            List<ParamInfoResponse> responses = Lists.newArrayList();
            for (ParamInfoResponse infoResponse : paramInfoResponseList) {
                if (infoResponse.isRef()) {
                    resetStruct(responses, infoResponse);
                } else {
                    responses.add(infoResponse);
                }
            }
            return responses;
        } catch (JsonProcessingException e) {
            log.error("Entity serialization error!", e);
            throw ExceptionUtils.mpe(ErrorCode.ENTITY_SERIALIZATION_ERROR);
        }
    }

    private void resetStruct(List<ParamInfoResponse> responses, ParamInfoResponse infoResponse) {
        if (infoResponse.isRef()) {
            for (ParamInfoResponse response : infoResponse.getStructureRef().getStruct()) {
                resetStruct(responses, response);
            }
        } else {
            responses.add(infoResponse);
        }
    }


}
