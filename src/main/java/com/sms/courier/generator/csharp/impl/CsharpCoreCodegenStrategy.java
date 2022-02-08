package com.sms.courier.generator.csharp.impl;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ParamType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.entity.generator.CodeTemplate;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.AbstractCodegen;
import com.sms.courier.generator.BaseCodegen;
import com.sms.courier.generator.GeneratorStrategy;
import com.sms.courier.generator.TemplateEngine;
import com.sms.courier.generator.annotation.CodeGenerator;
import com.sms.courier.generator.enums.CodeEntityName;
import com.sms.courier.generator.enums.CodeFileName;
import com.sms.courier.generator.enums.CodeType;
import com.sms.courier.generator.enums.TemplateType;
import com.sms.courier.generator.pojo.CodeEntityParamVo;
import com.sms.courier.generator.pojo.CodegenClassVo;
import com.sms.courier.generator.pojo.FilePackageVo;
import com.sms.courier.generator.pojo.FileVo;
import com.sms.courier.mapper.CodegenMapper;
import com.sms.courier.utils.CustomStringUtil;
import com.sms.courier.utils.ExceptionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@CodeGenerator(type = CodeType.CSHARP)
public class CsharpCoreCodegenStrategy extends AbstractCodegen implements GeneratorStrategy {

    private final CodegenMapper codegenMapper;

    private static final String SUFFIX = ".cs";
    private static final Map<ParamType, String> TYPE_MAPPING;
    private static final Map<RequestMethod, String> SWAGGER_METHOD;

    public CsharpCoreCodegenStrategy(TemplateEngine templateEngine, CodegenMapper codegenMapper) {
        super(templateEngine, TYPE_MAPPING, SUFFIX);
        this.codegenMapper = codegenMapper;
    }

    static {
        TYPE_MAPPING = new HashMap<>();
        TYPE_MAPPING.put(ParamType.STRING, "string");
        TYPE_MAPPING.put(ParamType.FILE, "System.IO.Stream");
        TYPE_MAPPING.put(ParamType.JSON, "string");
        TYPE_MAPPING.put(ParamType.INT, "int?");
        TYPE_MAPPING.put(ParamType.FLOAT, "float?");
        TYPE_MAPPING.put(ParamType.DOUBLE, "double?");
        TYPE_MAPPING.put(ParamType.DATE, "DateTime?");
        TYPE_MAPPING.put(ParamType.DATETIME, "DateTime?");
        TYPE_MAPPING.put(ParamType.BOOLEAN, "bool?");
        TYPE_MAPPING.put(ParamType.BYTE, "int?");
        TYPE_MAPPING.put(ParamType.SHORT, "double?");
        TYPE_MAPPING.put(ParamType.LONG, "long?");
        TYPE_MAPPING.put(ParamType.ARRAY, "List<%s>");
        TYPE_MAPPING.put(ParamType.OBJECT, "%s");
        TYPE_MAPPING.put(ParamType.NUMBER, "decimal?");

        SWAGGER_METHOD = new HashMap<>();
        SWAGGER_METHOD.put(RequestMethod.GET, "HttpGet");
        SWAGGER_METHOD.put(RequestMethod.POST, "HttpPost");
        SWAGGER_METHOD.put(RequestMethod.PUT, "HttpPut");
        SWAGGER_METHOD.put(RequestMethod.PATCH, "HttpPatch");
        SWAGGER_METHOD.put(RequestMethod.DELETE, "HttpDelete");
        SWAGGER_METHOD.put(RequestMethod.HEAD, "HttpHead");
        SWAGGER_METHOD.put(RequestMethod.OPTIONS, "HttpOptions");
    }


    @Override
    public List<FilePackageVo> generate(CodeGenRequest request, ApiResponse apiEntity,
        GeneratorTemplateEntity templateEntity) {
        try {
            List<FilePackageVo> fileVos = Lists.newArrayList();
            List<FileVo> requestFile = Lists.newArrayList();
            List<String> controllerImportString = Lists.newArrayList();
            List<String> serviceImportString = Lists.newArrayList();
            CodegenClassVo codegenClassVo = CodegenClassVo.builder()
                .route(CustomStringUtil.formatFirstLowerCase(getMethodName(apiEntity.getApiPath())))
                .requestMethod(SWAGGER_METHOD.get(RequestMethod.getType(apiEntity.getRequestMethod())))
                .packageName(request.getPackageName())
                .methodName(getMethodName(apiEntity.getApiPath()))
                .build();
            Map<Integer, CodeTemplate> templateMap =
                templateEntity.getCodeTemplates().stream()
                    .collect(Collectors.toMap(code -> code.getTemplateType().getCode(),
                        Function.identity()));

            if (request.getTemplateType().contains(TemplateType.ENTITY.getCode())) {
                generateEntity(requestFile, templateMap, codegenClassVo, apiEntity, request);
            }

            if (CollectionUtils.isNotEmpty(requestFile)) {
                fileVos
                    .add(FilePackageVo.builder().filePackageName(CodeFileName.ENTITY.getName()).fileList(requestFile)
                        .build());
                controllerImportString
                    .add(String.format("%s.%s", request.getPackageName(), CodeFileName.ENTITY.getName()));
                serviceImportString
                    .add(String.format("%s.%s", request.getPackageName(), CodeFileName.ENTITY.getName()));
            }

            String className = getClassName(apiEntity.getApiPath());
            String controllerName = Objects.nonNull(className) ? String.format("%sController", className)
                : CodeEntityName.COURIER_CODEGEN_CONTROLLER.getName();
            String serviceName = Objects.nonNull(className) ? String.format("I%sService", className)
                : CodeEntityName.COURIER_CODEGEN_SERVICE.getName();
            String serviceImplName = Objects.nonNull(className) ? String.format("%sService", className)
                : CodeEntityName.COURIER_CODEGEN_SERVICE_IMPL.getName();

            if (request.getTemplateType().contains(TemplateType.SERVICE.getCode())) {
                setClassVo(codegenClassVo, serviceImportString, serviceName, CodeFileName.SERVICE.getName());
                List<FileVo> serviceFile = Lists.newArrayList();
                setFile(codegenClassVo, serviceFile, templateMap.get(TemplateType.SERVICE.getCode()));
                fileVos.add(
                    FilePackageVo.builder().filePackageName(CodeFileName.SERVICE.getName()).fileList(serviceFile)
                        .build());
                controllerImportString.add(String.format("%s.%s", request.getPackageName(),
                    CodeFileName.SERVICE.getName()));
            }

            if (request.getTemplateType().contains(TemplateType.SERVICE_IMPL.getCode())) {
                codegenClassVo.setServiceName(serviceName);
                setClassVo(codegenClassVo, serviceImportString, serviceImplName, CodeFileName.SERVICE.getName());
                List<FileVo> serviceImplFile = Lists.newArrayList();
                setFile(codegenClassVo, serviceImplFile, templateMap.get(TemplateType.SERVICE_IMPL.getCode()));
                fileVos.add(
                    FilePackageVo.builder().filePackageName(CodeFileName.SERVICE.getName()).fileList(serviceImplFile)
                        .build());
            }

            if (request.getTemplateType().contains(TemplateType.CONTROLLER.getCode())) {
                codegenClassVo.setServiceName(serviceName);
                setClassVo(codegenClassVo, controllerImportString, controllerName, CodeFileName.CONTROLLER.getName());
                List<FileVo> controllerFile = Lists.newArrayList();
                setFile(codegenClassVo, controllerFile, templateMap.get(TemplateType.CONTROLLER.getCode()));
                fileVos.add(
                    FilePackageVo.builder().filePackageName(CodeFileName.CONTROLLER.getName()).fileList(controllerFile)
                        .build());
            }

            return fileVos;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to generate code!", e);
            throw ExceptionUtils.mpe(ErrorCode.GENERATE_CODE_ERROR);
        }
    }

    private void setClassVo(BaseCodegen classVo, List<String> importString, String className, String classPackage) {
        classVo.setImports(importString);
        classVo.setClassName(className);
        classVo.setClassPackage(classPackage);
    }

    private void generateEntity(List<FileVo> requestFile, Map<Integer, CodeTemplate> templateMap,
        CodegenClassVo codegenControllerVo, ApiResponse apiEntity, CodeGenRequest request) {

        if (CollectionUtils.isNotEmpty(apiEntity.getRequestParams())) {
            List<CodeEntityParamVo> requestParams = codegenMapper
                .toParamModelList(apiEntity.getRequestParams());
            requestFile.addAll(generateParamObject(requestParams, CodeEntityName.REQUEST_PARAM.getName(),
                request.getPackageName(), templateMap.get(TemplateType.ENTITY.getCode())));
            codegenControllerVo
                .setRequestName(Objects.equals(ApiJsonType.ARRAY.getCode(), apiEntity.getApiRequestJsonType())
                    ? String.format(TYPE_MAPPING.get(ParamType.ARRAY), CodeEntityName.REQUEST_PARAM.getName())
                    : CodeEntityName.REQUEST_PARAM.getName());
        }
        if (CollectionUtils.isNotEmpty(apiEntity.getRestfulParams())) {
            for(ParamInfoResponse info:apiEntity.getRestfulParams()){
                info.setParamType(ParamType.STRING.getCode());
            }
            List<CodeEntityParamVo> restfulParams = codegenMapper
                .toParamModelList(apiEntity.getRestfulParams());
            requestFile.addAll(generateParamObject(restfulParams, CodeEntityName.QUERY_PARAM.getName(),
                request.getPackageName(), templateMap.get(TemplateType.ENTITY.getCode())));
            codegenControllerVo.setQueryName(CodeEntityName.QUERY_PARAM.getName());
        }
        if (CollectionUtils.isNotEmpty(apiEntity.getResponseParams())) {
            List<CodeEntityParamVo> responseParam = codegenMapper
                .toParamModelList(apiEntity.getResponseParams());
            requestFile.addAll(generateParamObject(responseParam, CodeEntityName.RESPONSE_PARAM.getName(),
                request.getPackageName(), templateMap.get(TemplateType.ENTITY.getCode())));
            codegenControllerVo
                .setResponseName(Objects.equals(ApiJsonType.ARRAY.getCode(), apiEntity.getApiResponseJsonType())
                    ? String.format(TYPE_MAPPING.get(ParamType.ARRAY), CodeEntityName.RESPONSE_PARAM.getName())
                    : CodeEntityName.RESPONSE_PARAM.getName());
        }
        if (CollectionUtils.isNotEmpty(apiEntity.getPathParams())) {
            List<CodeEntityParamVo> pathParam = codegenMapper
                .toParamModelList(apiEntity.getPathParams());
            codegenControllerVo.setPathParams(pathParam);
        }
    }


    private static String getClassName(String apiPath) {
        if (StringUtils.isBlank(apiPath)) {
            return null;
        }
        String[] apiUrls = apiPath.split("/");
        return apiUrls.length > 1 ? apiUrls[1] : null;
    }

    private static String getMethodName(String apiPath) {
        if (StringUtils.isBlank(apiPath)) {
            return null;
        }
        String[] apiUrls = apiPath.split("/");
        return apiUrls.length > 1 ? apiUrls[apiUrls.length - 1] : null;
    }

}
