package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.CASE_TEMPLATE_API;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.BATCH_EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_BY_ID_ERROR;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.satp.dto.request.AddSceneCaseApi;
import com.sms.satp.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.service.CaseTemplateApiService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateApiServiceImpl implements CaseTemplateApiService {

    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final CaseTemplateApiMapper aseTemplateApiMapper;
    private final CaseTemplateRepository caseTemplateRepository;
    private final ApiRepository apiRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final ApiTestCaseRepository apiTestCaseRepository;

    public CaseTemplateApiServiceImpl(CaseTemplateApiRepository sceneCaseApiRepository,
        CaseTemplateApiMapper sceneCaseApiMapper, CaseTemplateRepository caseTemplateRepository,
        ApiRepository apiRepository, ApiTestCaseMapper apiTestCaseMapper,
        ApiTestCaseRepository apiTestCaseRepository) {
        this.caseTemplateApiRepository = sceneCaseApiRepository;
        this.aseTemplateApiMapper = sceneCaseApiMapper;
        this.caseTemplateRepository = caseTemplateRepository;
        this.apiRepository = apiRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.apiTestCaseRepository = apiTestCaseRepository;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE_API,
        template = "{{#addCaseTemplateApiRequest.addCaseTemplateApiRequestList?.![#this.apiTestCase.apiName]}}",
        projectId = "addCaseTemplateApiRequestList[0].projectId")
    public Boolean batchAdd(BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest) {
        log.info("CaseTemplateApiService-batchAdd()-params: [CaseTemplateApi]={}",
            addCaseTemplateApiRequest.toString());
        try {
            List<CaseTemplateApi> caseApiList = aseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(
                addCaseTemplateApiRequest.getAddCaseTemplateApiRequestList());
            caseTemplateApiRepository.insert(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = CASE_TEMPLATE_API,
        template = "{{#result?.![#this.apiTestCase.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateApiService-deleteById()-params: [id]={}", ids);
        try {
            Long count = caseTemplateApiRepository.deleteAllByIdIsIn(ids);
            return count > 0;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_API,
        template = "{{#updateCaseTemplateApiRequest.apiTestCase.apiName}}")
    public Boolean edit(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest) {
        log.info("CaseTemplateApiService-edit()-params: [CaseTemplateApi]={}", updateCaseTemplateApiRequest.toString());
        try {
            CaseTemplateApi caseTemplateApi = aseTemplateApiMapper
                .toCaseTemplateApiByUpdateRequest(updateCaseTemplateApiRequest);
            caseTemplateApiRepository.save(caseTemplateApi);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_API,
        template = "{{#caseTemplateApiList[0].apiTestCase.apiName}}")
    public Boolean editAll(List<CaseTemplateApi> caseTemplateApiList) {
        log.info("CaseTemplateApiService-edit()-params: [CaseTemplateApi]={}", caseTemplateApiList.toString());
        try {
            caseTemplateApiRepository.saveAll(caseTemplateApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_API, template = "{{#updateCaseTemplateApiDto"
        + ".updateCaseTemplateApiRequestList?.![#this.apiTestCase.apiName]}}",
        projectId = "updateCaseTemplateApiRequestList[0].projectId")
    public Boolean batchEdit(BatchUpdateCaseTemplateApiRequest updateCaseTemplateApiDto) {
        log.info("CaseTemplateApiService-batchEdit()-params: [CaseTemplateApi]={}",
            updateCaseTemplateApiDto.toString());
        try {
            List<CaseTemplateApi> caseApiList = aseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(
                updateCaseTemplateApiDto.getUpdateCaseTemplateApiRequestList());
            caseTemplateApiRepository.saveAll(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to batch edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(BATCH_EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApiResponse> listByCaseTemplateId(String caseTemplateId, boolean removed) {
        try {
            Example<CaseTemplateApi> example = Example
                .of(CaseTemplateApi.builder().caseTemplateId(caseTemplateId).removed(removed).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneFiled.ORDER.getFiled());
            List<CaseTemplateApi> sceneCaseApiList = caseTemplateApiRepository.findAll(example, sort);
            return sceneCaseApiList.stream().map(aseTemplateApiMapper::toCaseTemplateApiDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApi> listByCaseTemplateId(String caseTemplateId) {
        try {
            Example<CaseTemplateApi> example = Example.of(
                CaseTemplateApi.builder().caseTemplateId(caseTemplateId).build());
            return caseTemplateApiRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApi> getApiByCaseTemplateId(String caseTemplateId, boolean removed) {
        try {
            Example<CaseTemplateApi> example = Example
                .of(CaseTemplateApi.builder().caseTemplateId(caseTemplateId).removed(removed).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneFiled.ORDER.getFiled());
            return caseTemplateApiRepository.findAll(example, sort);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public CaseTemplateApiResponse getCaseTemplateApiById(String id) {
        try {
            Optional<CaseTemplateApi> sceneCaseApi = caseTemplateApiRepository.findById(id);
            return sceneCaseApi.map(aseTemplateApiMapper::toCaseTemplateApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi by id!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean add(AddCaseTemplateApiByIdsRequest request) {
        try {
            Optional<CaseTemplate> caseTemplate = caseTemplateRepository.findById(request.getCaseTemplateId());
            if (caseTemplate.isEmpty()) {
                throw new ApiTestPlatformException(GET_CASE_TEMPLATE_BY_ID_ERROR);
            }
            for (AddSceneCaseApi addSceneCaseApi : request.getSceneCaseApis()) {
                if (BooleanUtils.isTrue(addSceneCaseApi.getIsCase())) {
                    addCaseTemplateApiByTestCase(caseTemplate.get(), addSceneCaseApi);
                } else {
                    addCaseTemplateApiByApi(caseTemplate.get(), addSceneCaseApi);
                }
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi by id!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    private void addCaseTemplateApiByApi(CaseTemplate caseTemplate, AddSceneCaseApi addSceneCaseApi) {
        Optional<ApiEntity> apiEntity = apiRepository.findById(addSceneCaseApi.getId());
        if (apiEntity.isPresent()) {
            CaseTemplateApi caseTemplateApi =
                CaseTemplateApi.builder().apiTestCase(apiTestCaseMapper.toEntityByApiEntity(apiEntity.get()))
                    .caseTemplateId(caseTemplate.getId())
                    .projectId(caseTemplate.getProjectId()).order(addSceneCaseApi.getOrder()).apiType(ApiType.API)
                    .apiBindingStatus(ApiBindingStatus.BINDING).build();
            caseTemplateApiRepository.insert(caseTemplateApi);
        }
    }

    private void addCaseTemplateApiByTestCase(CaseTemplate caseTemplate, AddSceneCaseApi addSceneCaseApi) {
        Optional<ApiTestCase> apiTestCase = apiTestCaseRepository.findById(addSceneCaseApi.getId());
        if (apiTestCase.isPresent()) {
            CaseTemplateApi caseTemplateApi =
                CaseTemplateApi.builder().apiTestCase(apiTestCase.get()).caseTemplateId(caseTemplate.getId())
                    .projectId(caseTemplate.getProjectId()).order(addSceneCaseApi.getOrder()).apiType(ApiType.API)
                    .apiBindingStatus(ApiBindingStatus.BINDING).build();
            caseTemplateApiRepository.insert(caseTemplateApi);
        }
    }

}
