package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.CASE_TEMPLATE;
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
import static com.sms.courier.common.exception.ErrorCode.THE_API_ENTITY_NOT_EXITS_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_API_TEST_CASE_NOT_EXITS_ERROR;

import com.google.common.collect.Lists;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.courier.dto.request.AddCaseTemplateRequest;
import com.sms.courier.dto.request.AddSceneCaseApi;
import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.request.ConvertCaseTemplateRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.CaseTemplateDetailResponse;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.dto.response.IdResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.CaseTemplateMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CaseTemplateRepository;
import com.sms.courier.repository.CustomizedCaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedCaseTemplateRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.service.CaseTemplateApiService;
import com.sms.courier.service.CaseTemplateService;
import com.sms.courier.service.SceneCaseApiService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
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
    private final ApiRepository apiRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final ApiTestCaseRepository apiTestCaseRepository;
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;

    public CaseTemplateServiceImpl(CaseTemplateRepository caseTemplateRepository,
        CustomizedCaseTemplateRepository customizedCaseTemplateRepository,
        CaseTemplateMapper caseTemplateMapper, CaseTemplateApiService caseTemplateApiService,
        SceneCaseRepository sceneCaseRepository, SceneCaseApiService sceneCaseApiService,
        CaseTemplateApiMapper caseTemplateApiMapper,
        CaseTemplateApiRepository caseTemplateApiRepository, ApiRepository apiRepository,
        ApiTestCaseMapper apiTestCaseMapper, ApiTestCaseRepository apiTestCaseRepository,
        CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository) {
        this.caseTemplateRepository = caseTemplateRepository;
        this.customizedCaseTemplateRepository = customizedCaseTemplateRepository;
        this.caseTemplateMapper = caseTemplateMapper;
        this.caseTemplateApiService = caseTemplateApiService;
        this.sceneCaseRepository = sceneCaseRepository;
        this.sceneCaseApiService = sceneCaseApiService;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.apiRepository = apiRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.customizedCaseTemplateApiRepository = customizedCaseTemplateApiRepository;
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
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE, template = "{{#convertCaseTemplateRequest.name}}")
    public IdResponse add(ConvertCaseTemplateRequest convertCaseTemplateRequest) {
        try {
            SceneCaseEntity sceneCase =
                sceneCaseRepository.findById(convertCaseTemplateRequest.getSceneCaseId())
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_BY_ID_ERROR));
            CaseTemplateEntity caseTemplate = caseTemplateMapper.toCaseTemplateBySceneCase(sceneCase);
            caseTemplate.setGroupId(convertCaseTemplateRequest.getGroupId());
            caseTemplateRepository.insert(caseTemplate);
            List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiService
                .listBySceneCaseId(convertCaseTemplateRequest.getSceneCaseId());

            List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList();
            Integer index = 0;
            for (SceneCaseApiEntity sceneCaseApi : sceneCaseApiList) {
                if (Objects.isNull(sceneCaseApi.getCaseTemplateId())) {
                    CaseTemplateApiEntity caseTemplateApi =
                        caseTemplateApiMapper.toCaseTemplateApiBySceneCaseApi(sceneCaseApi);
                    caseTemplateApi.setCaseTemplateId(caseTemplate.getId());
                    caseTemplateApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : caseTemplateApi.getOrder());
                    caseTemplateApiList.add(caseTemplateApi);
                    index = caseTemplateApi.getOrder();
                } else {
                    List<CaseTemplateApiEntity> templateApiList =
                        caseTemplateApiRepository.findAllByCaseTemplateIdOrderByOrder(sceneCaseApi.getCaseTemplateId());
                    for (CaseTemplateApiEntity caseTemplateApi : templateApiList) {
                        caseTemplateApi.setOrder(index > 0 ? Integer.valueOf(index + 1) : caseTemplateApi.getOrder());
                        caseTemplateApi.setCaseTemplateId(caseTemplate.getId());
                        caseTemplateApi.setId(null);
                        index = caseTemplateApi.getOrder();
                    }
                    caseTemplateApiList.addAll(templateApiList);
                }
            }
            if (CollectionUtils.isNotEmpty(caseTemplateApiList)) {
                caseTemplateApiRepository.insert(caseTemplateApiList);
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
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateService-deleteById()-params: [id]={}", ids);
        try {
            for (String id : ids) {
                caseTemplateRepository.deleteById(id);
                deleteCaseTemplateApi(id);
            }
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
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE, template = "{{#updateCaseTemplateRequest.name}}")
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
    public List<CaseTemplateEntity> get(String groupId, String projectId) {
        try {
            CaseTemplateEntity caseTemplate = CaseTemplateEntity.builder().groupId(groupId).projectId(projectId)
                .build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonField.PROJECT_ID.getName(), GenericPropertyMatchers.exact())
                .withMatcher(CommonField.GROUP_ID.getName(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<CaseTemplateEntity> example = Example.of(caseTemplate, exampleMatcher);
            return caseTemplateRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplate!", e);
            throw ExceptionUtils.mpe(GET_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public CaseTemplateDetailResponse getApiList(String caseTemplateId) {
        try {
            CaseTemplateEntity caseTemplate =
                caseTemplateRepository.findById(caseTemplateId)
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_CASE_TEMPLATE_ERROR));
            CaseTemplateResponse caseTemplateResponse = caseTemplateMapper.toDto(caseTemplate);
            List<CaseTemplateApiResponse> caseTemplateApiResponseList =
                caseTemplateApiService.listResponseByCaseTemplateId(caseTemplateId);
            return CaseTemplateDetailResponse.builder().caseTemplateResponse(caseTemplateResponse)
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
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            customizedCaseTemplateRepository.deleteByIds(ids);
            List<CaseTemplateApiEntity> caseTemplateApiEntityList = customizedCaseTemplateApiRepository
                .findCaseTemplateApiIdsByCaseTemplateIds(ids);
            if (CollectionUtils.isNotEmpty(caseTemplateApiEntityList)) {
                List<String> caseTemplateApiIds =
                    caseTemplateApiEntityList.stream().map(CaseTemplateApiEntity::getId).collect(
                        Collectors.toList());
                customizedCaseTemplateApiRepository.deleteByIds(caseTemplateApiIds);
            }
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
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        try {
            customizedCaseTemplateRepository.recover(ids);
            List<CaseTemplateApiEntity> caseTemplateApiEntityList = customizedCaseTemplateApiRepository
                .findCaseTemplateApiIdsByCaseTemplateIds(ids);
            if (CollectionUtils.isNotEmpty(caseTemplateApiEntityList)) {
                List<String> caseTemplateApiIds =
                    caseTemplateApiEntityList.stream().map(CaseTemplateApiEntity::getId).collect(
                        Collectors.toList());
                customizedCaseTemplateApiRepository.recover(caseTemplateApiIds);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to recover the CaseTemplate!", e);
            throw ExceptionUtils.mpe(RECOVER_CASE_TEMPLATE_ERROR);
        }
    }

    private void addCaseTemplateApi(CaseTemplateEntity caseTemplate, AddSceneCaseApi addSceneCaseApi) {
        ApiTestCaseEntity apiTestCase;
        if (BooleanUtils.isTrue(addSceneCaseApi.isCase())) {
            apiTestCase =
                apiTestCaseRepository.findById(addSceneCaseApi.getId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_API_TEST_CASE_NOT_EXITS_ERROR));
        } else {
            ApiEntity apiEntity = apiRepository.findById(addSceneCaseApi.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(THE_API_ENTITY_NOT_EXITS_ERROR));
            apiTestCase = apiTestCaseMapper.toEntityByApiEntity(apiEntity);
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
        caseTemplateApiRepository.insert(caseTemplateApi);

    }

    private void deleteCaseTemplateApi(String id) {
        List<CaseTemplateApiEntity> caseTemplateApiList = caseTemplateApiService.listByCaseTemplateId(id);
        if (CollectionUtils.isNotEmpty(caseTemplateApiList)) {
            List<String> ids = caseTemplateApiList.stream().map(CaseTemplateApiEntity::getId)
                .collect(Collectors.toList());
            caseTemplateApiService.deleteByIds(ids);
        }
    }

}
