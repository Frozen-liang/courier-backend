package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.CASE_TEMPLATE;
import static com.sms.courier.common.enums.OperationModule.CASE_TEMPLATE_API;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.RECOVER;
import static com.sms.courier.common.enums.OperationType.REMOVE;
import static com.sms.courier.common.exception.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RECOVER_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.SEARCH_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_API_TEST_CASE_NOT_EXITS_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.enums.ResponseParamsExtractionType;
import com.sms.courier.common.enums.ResultVerificationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.function.FunctionHandler;
import com.sms.courier.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.courier.dto.request.AddCaseTemplateRequest;
import com.sms.courier.dto.request.AddSceneCaseApi;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.request.ConvertCaseTemplateRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.CaseTemplateConnResponse;
import com.sms.courier.dto.response.CaseTemplateDetailResponse;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.dto.response.DataCollectionResponse;
import com.sms.courier.dto.response.EnvDataCollConnResponse;
import com.sms.courier.dto.response.EnvDataCollResponse;
import com.sms.courier.dto.response.IdResponse;
import com.sms.courier.dto.response.ProjectEnvironmentResponse;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.CaseTemplateMapper;
import com.sms.courier.mapper.DataCollectionMapper;
import com.sms.courier.mapper.MatchParamInfoMapper;
import com.sms.courier.mapper.ProjectEnvironmentMapper;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CaseTemplateRepository;
import com.sms.courier.repository.CustomizedCaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedCaseTemplateRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.service.CaseApiCountHandler;
import com.sms.courier.service.CaseTemplateApiService;
import com.sms.courier.service.CaseTemplateService;
import com.sms.courier.service.DataCollectionService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.SceneCaseApiService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateServiceImpl implements CaseTemplateService {

    private final CaseTemplateRepository caseTemplateRepository;
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private final CaseTemplateMapper caseTemplateMapper;
    private final CaseTemplateApiService caseTemplateApiService;
    private final SceneCaseRepository sceneCaseRepository;
    private final SceneCaseApiService sceneCaseApiService;
    private final CaseTemplateApiMapper caseTemplateApiMapper;
    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final ApiTestCaseRepository apiTestCaseRepository;
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;
    private final CaseApiCountHandler caseApiCountHandler;
    private final MatchParamInfoMapper matchParamInfoMapper;
    private final ObjectMapper objectMapper;
    private final ProjectEnvironmentService projectEnvironmentService;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;
    private final DataCollectionService dataCollectionService;
    private final DataCollectionMapper dataCollectionMapper;


    public CaseTemplateServiceImpl(CaseTemplateRepository caseTemplateRepository,
        CustomizedCaseTemplateRepository customizedCaseTemplateRepository,
        CaseTemplateMapper caseTemplateMapper, CaseTemplateApiService caseTemplateApiService,
        SceneCaseRepository sceneCaseRepository, SceneCaseApiService sceneCaseApiService,
        CaseTemplateApiMapper caseTemplateApiMapper,
        CaseTemplateApiRepository caseTemplateApiRepository,
        ApiTestCaseMapper apiTestCaseMapper, ApiTestCaseRepository apiTestCaseRepository,
        CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository,
        CaseApiCountHandler sceneCaseApiCountHandler, MatchParamInfoMapper matchParamInfoMapper,
        ObjectMapper objectMapper, ProjectEnvironmentService projectEnvironmentService,
        ProjectEnvironmentMapper projectEnvironmentMapper,
        DataCollectionService dataCollectionService, DataCollectionMapper dataCollectionMapper) {
        this.caseTemplateRepository = caseTemplateRepository;
        this.customizedCaseTemplateRepository = customizedCaseTemplateRepository;
        this.caseTemplateMapper = caseTemplateMapper;
        this.caseTemplateApiService = caseTemplateApiService;
        this.sceneCaseRepository = sceneCaseRepository;
        this.sceneCaseApiService = sceneCaseApiService;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.customizedCaseTemplateApiRepository = customizedCaseTemplateApiRepository;
        this.caseApiCountHandler = sceneCaseApiCountHandler;
        this.matchParamInfoMapper = matchParamInfoMapper;
        this.objectMapper = objectMapper;
        this.projectEnvironmentService = projectEnvironmentService;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
        this.dataCollectionService = dataCollectionService;
        this.dataCollectionMapper = dataCollectionMapper;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE, template = "{{#addCaseTemplateRequest.name}}")
    public Boolean add(AddCaseTemplateRequest addCaseTemplateRequest) {
        log.info("CaseTemplateService-add()-params: [CaseTemplate]={}", addCaseTemplateRequest.toString());
        try {
            CaseTemplateEntity sceneCaseTemplate = caseTemplateMapper
                .toCaseTemplateByAddRequest(addCaseTemplateRequest);
            caseTemplateRepository.insert(sceneCaseTemplate);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplate!", e);
            throw ExceptionUtils.mpe(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE,
        template = "{{#convertCaseTemplateRequest.templateName}}")
    public IdResponse add(ConvertCaseTemplateRequest convertCaseTemplateRequest) {
        try {
            SceneCaseEntity sceneCase =
                sceneCaseRepository.findById(convertCaseTemplateRequest.getSceneCaseId())
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_BY_ID_ERROR));
            CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplateBySceneCase(sceneCase);
            caseTemplate.setGroupId(convertCaseTemplateRequest.getGroupId());
            caseTemplate.setName(convertCaseTemplateRequest.getTemplateName());
            caseTemplateRepository.insert(caseTemplate);
            List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList();
            Integer index = 0;
            for (String id : convertCaseTemplateRequest.getCaseIds()) {
                SceneCaseApiEntity sceneCaseApi = sceneCaseApiService.findById(id);
                if (Objects.isNull(sceneCaseApi)) {
                    continue;
                }
                if (Objects.isNull(sceneCaseApi.getCaseTemplateId())) {
                    CaseTemplateApiEntity caseTemplateApi =
                        caseTemplateApiMapper.toCaseTemplateApiBySceneCaseApi(sceneCaseApi);
                    caseTemplateApi.setCaseTemplateId(caseTemplate.getId());
                    caseTemplateApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : caseTemplateApi.getOrder());
                    caseTemplateApiList.add(caseTemplateApi);
                    index = caseTemplateApi.getOrder();
                } else {
                    List<CaseTemplateApiEntity> templateApiList =
                        caseTemplateApiRepository
                            .findAllByCaseTemplateIdAndRemovedOrderByOrder(sceneCaseApi.getCaseTemplateId(),
                                Boolean.FALSE);
                    for (CaseTemplateApiEntity caseTemplateApi : templateApiList) {
                        caseTemplateApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : caseTemplateApi.getOrder());
                        caseTemplateApi.setCaseTemplateId(caseTemplate.getId());
                        index = caseTemplateApi.getOrder();
                    }
                    caseTemplateApiList.addAll(templateApiList);
                }
            }
            if (CollectionUtils.isNotEmpty(caseTemplateApiList)) {
                List<CaseTemplateApiEntity> entityList = setIdConvert(caseTemplateApiList);
                caseTemplateApiRepository.insert(entityList);
            }
            return IdResponse.builder().id(caseTemplate.getId()).build();
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplate!", e);
            throw ExceptionUtils.mpe(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = CASE_TEMPLATE, template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"), sourceId = "{{#ids}}")
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateService-deleteById()-params: [id]={}", ids);
        try {
            caseTemplateRepository.deleteAllByIdIsIn(ids);
            caseTemplateApiService.deleteAllByCaseTemplateIds(ids);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplate!", e);
            throw ExceptionUtils.mpe(DELETE_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE, template = "{{#updateCaseTemplateRequest.name}}",
        sourceId = "{{#updateCaseTemplateRequest.id}}")
    public Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest) {
        log.info("CaseTemplateService-edit()-params: [CaseTemplate]={}", updateCaseTemplateRequest.toString());
        try {
            CaseTemplateEntity caseTemplate = caseTemplateMapper
                .toCaseTemplateByUpdateRequest(updateCaseTemplateRequest);
            caseTemplateRepository.save(caseTemplate);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplate!", e);
            throw ExceptionUtils.mpe(EDIT_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId) {
        try {
            return customizedCaseTemplateRepository.page(searchDto, projectId);
        } catch (Exception e) {
            log.error("Failed to search the CaseTemplate!", e);
            throw ExceptionUtils.mpe(SEARCH_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public CaseTemplateDetailResponse getApiList(String caseTemplateId) {
        try {
            CaseTemplateResponse caseTemplateResponse =
                customizedCaseTemplateRepository.findById(caseTemplateId).orElse(null);
            CaseTemplateConnResponse dto = setCaseTemplateConnDto(caseTemplateResponse);
            List<CaseTemplateApiResponse> caseTemplateApiResponseList =
                caseTemplateApiService.listResponseByCaseTemplateId(caseTemplateId);
            return CaseTemplateDetailResponse.builder().caseTemplateResponse(dto)
                .caseTemplateApiResponseList(caseTemplateApiResponseList).build();
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplate detail!", e);
            throw ExceptionUtils.mpe(GET_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE_API,
        template = "{{#request.caseTemplateApis?.![#this.name]}}", sourceId = "{{#request.caseTemplateId}}")
    public Boolean addApi(AddCaseTemplateApiByIdsRequest request) {
        try {
            CaseTemplateEntity caseTemplate =
                caseTemplateRepository.findById(request.getCaseTemplateId())
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_CASE_TEMPLATE_ERROR));
            for (AddSceneCaseApi addSceneCaseApiRequest : request.getCaseTemplateApis()) {
                addCaseTemplateApi(caseTemplate, addSceneCaseApiRequest);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi by id!", e);
            throw ExceptionUtils.mpe(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = CASE_TEMPLATE,
        template = "{{#result.name]}}",
        enhance = @Enhance(enable = true),
        sourceId = "{{#ids}}")
    public Boolean delete(List<String> ids) {
        try {
            Assert.isFalse(sceneCaseApiService.existsByCaseTemplateId(ids),
                "The case template cannot be deleted when has reference! ");
            customizedCaseTemplateRepository.deleteByIds(ids);
            List<CaseTemplateApiEntity> caseTemplateApiEntityList = customizedCaseTemplateApiRepository
                .findCaseTemplateApiIdsByCaseTemplateIds(ids);
            FunctionHandler.confirmed(CollectionUtils.isNotEmpty(caseTemplateApiEntityList),
                caseTemplateApiEntityList).handler(this::batchDelete);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplate!", e);
            throw ExceptionUtils.mpe(DELETE_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = RECOVER, operationModule = CASE_TEMPLATE,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
    public Boolean recover(List<String> ids) {
        try {
            customizedCaseTemplateRepository.recover(ids);
            List<CaseTemplateApiEntity> caseTemplateApiEntityList = customizedCaseTemplateApiRepository
                .findCaseTemplateApiIdsByCaseTemplateIds(ids);
            FunctionHandler.confirmed(CollectionUtils.isNotEmpty(caseTemplateApiEntityList),
                caseTemplateApiEntityList).handler(this::batchAdd);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to recover the CaseTemplate!", e);
            throw ExceptionUtils.mpe(RECOVER_CASE_TEMPLATE_ERROR);
        }
    }

    private void batchDelete(List<CaseTemplateApiEntity> caseTemplateApiEntityList) {
        List<String> caseTemplateApiIds =
            caseTemplateApiEntityList.stream().map(CaseTemplateApiEntity::getId).collect(Collectors.toList());
        customizedCaseTemplateApiRepository.deleteByIds(caseTemplateApiIds);
        caseApiCountHandler.deleteTemplateCaseByCaseTemplateApiIds(caseTemplateApiIds);
    }

    private void batchAdd(List<CaseTemplateApiEntity> caseTemplateApiEntityList) {
        List<String> caseTemplateApiIds =
            caseTemplateApiEntityList.stream().map(CaseTemplateApiEntity::getId).collect(
                Collectors.toList());
        customizedCaseTemplateApiRepository.recover(caseTemplateApiIds);
        caseApiCountHandler.addTemplateCaseByCaseTemplateApiIds(caseTemplateApiIds);
    }

    private CaseTemplateConnResponse setCaseTemplateConnDto(CaseTemplateResponse caseTemplateResponse) {
        CaseTemplateConnResponse dto = caseTemplateMapper.toConnResponse(caseTemplateResponse);
        List<EnvDataCollConnResponse> connResponses = Lists.newArrayList();
        FunctionHandler.confirmedTwoNoReturn(CollectionUtils.isNotEmpty(caseTemplateResponse.getEnvDataCollConnList()),
            connResponses, caseTemplateResponse).handler(this::resetDto);
        dto.setEnvDataCollConnList(connResponses);
        return dto;
    }

    private void resetDto(List<EnvDataCollConnResponse> connResponses, CaseTemplateResponse caseTemplateResponse) {
        for (EnvDataCollResponse response : caseTemplateResponse.getEnvDataCollConnList()) {
            ProjectEnvironmentResponse envResponse = Objects.nonNull(response.getEnvId())
                ? projectEnvironmentMapper.toDto(projectEnvironmentService.findOne(response.getEnvId())) : null;
            DataCollectionResponse dataResponse = Objects.nonNull(response.getDataCollId())
                ? dataCollectionMapper.toDto(dataCollectionService.findOne(response.getDataCollId())) : null;
            EnvDataCollConnResponse collConnResponse = EnvDataCollConnResponse.builder()
                .environment(envResponse)
                .dataCollection(dataResponse)
                .build();
            connResponses.add(collConnResponse);
        }
    }

    private List<CaseTemplateApiEntity> setIdConvert(List<CaseTemplateApiEntity> caseTemplateApiList)
        throws JsonProcessingException {
        List<CaseTemplateApiResponse> caseTemplateApiResponseList =
            caseTemplateApiMapper.toCaseTemplateApiDtoList(caseTemplateApiList);

        if (CollectionUtils.isNotEmpty(caseTemplateApiResponseList)) {
            caseTemplateApiResponseList.sort(Comparator.comparingInt(CaseTemplateApiResponse::getOrder).reversed());
        }
        Map<String, String> newIdAndOldIdMap = new HashMap<>();
        for (CaseTemplateApiResponse caseTemplateApiResponse : caseTemplateApiResponseList) {
            String objId = new ObjectId().toString();
            newIdAndOldIdMap.put(caseTemplateApiResponse.getId(), objId);
            caseTemplateApiResponse.setId(objId);
        }
        String jsonString = objectMapper.writeValueAsString(caseTemplateApiResponseList);
        Set<Entry<String, String>> idSet = newIdAndOldIdMap.entrySet();
        for (Entry<String, String> key : idSet) {
            jsonString = jsonString.replace(key.getKey(), key.getValue());
        }

        List<CaseTemplateApiResponse> entityList = objectMapper
            .readValue(jsonString, new TypeReference<List<CaseTemplateApiResponse>>() {
            });
        return caseTemplateApiMapper.toEntityByResponseList(entityList);
    }

    private void addCaseTemplateApi(CaseTemplateEntity caseTemplate, AddSceneCaseApi addSceneCaseApi) {
        ApiTestCaseEntity apiTestCase;
        if (BooleanUtils.isTrue(addSceneCaseApi.isCase())) {
            apiTestCase =
                apiTestCaseRepository.findById(addSceneCaseApi.getId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_API_TEST_CASE_NOT_EXITS_ERROR));
            resetApiTestCaseByCase(apiTestCase);
        } else {
            apiTestCase = apiTestCaseMapper.toEntityByApiEntity(addSceneCaseApi.getApiEntity());
            resetApiTestCaseByApi(apiTestCase, addSceneCaseApi.getApiEntity());
        }
        apiTestCase.setExecute(Boolean.TRUE);
        CaseTemplateApiEntity caseTemplateApi =
            CaseTemplateApiEntity.builder()
                .apiTestCase(apiTestCase)
                .caseTemplateId(caseTemplate.getId())
                .projectId(caseTemplate.getProjectId())
                .order(addSceneCaseApi.getOrder())
                .apiType(ApiType.API)
                .build();
        caseTemplateApi = caseTemplateApiRepository.insert(caseTemplateApi);
        if (Objects.nonNull(caseTemplateApi.getId())) {
            caseApiCountHandler.addTemplateCaseByCaseTemplateApiIds(List.of(caseTemplateApi.getId()));
        }
    }

    private void resetApiTestCaseByApi(ApiTestCaseEntity apiTestCase, ApiRequest apiEntity) {
        apiTestCase.setExecute(Boolean.TRUE);
        apiTestCase.setResponseParamsExtractionType(ResponseParamsExtractionType.JSON);
        apiTestCase.setHttpStatusVerification(HttpStatusVerification.builder().statusCode(
            Constants.HTTP_DEFAULT_STATUS_CODE).build());
        apiTestCase.setResponseResultVerification(ResponseResultVerification.builder()
            .resultVerificationType(ResultVerificationType.JSON)
            .apiResponseJsonType(apiEntity.getApiResponseJsonType())
            .params(matchParamInfoMapper.toMatchParamInfoList(apiEntity.getResponseParams()))
            .build());
    }

    private void resetApiTestCaseByCase(ApiTestCaseEntity apiTestCase) {
        apiTestCase.setExecute(Boolean.TRUE);
        apiTestCase.setResponseParamsExtractionType(ResponseParamsExtractionType.JSON);
    }

}
