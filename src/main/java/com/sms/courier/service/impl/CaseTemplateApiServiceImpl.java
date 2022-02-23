package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.CASE_TEMPLATE_API;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.CASE_SYNC;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_CASE_TEMPLATE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.BATCH_EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.CASE_SYNC_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.courier.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.courier.dto.request.SyncApiRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.service.CaseApiCountHandler;
import com.sms.courier.service.CaseTemplateApiService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateApiServiceImpl extends AbstractCaseService implements CaseTemplateApiService {

    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final CaseTemplateApiMapper caseTemplateApiMapper;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private final CaseApiCountHandler sceneCaseApiCountHandler;

    public CaseTemplateApiServiceImpl(CaseTemplateApiRepository caseTemplateApiRepository,
        CaseTemplateApiMapper caseTemplateApiMapper, CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository,
        CaseApiCountHandler sceneCaseApiCountHandler, ApiRepository apiRepository,
        ParamInfoMapper paramInfoMapper) {
        super(apiRepository, paramInfoMapper);
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
        this.sceneCaseApiCountHandler = sceneCaseApiCountHandler;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE_API,
        template = "{{#addCaseTemplateApiRequest.addCaseTemplateApiRequestList?.![#this.apiTestCase.caseName]}}",
        refId = "addCaseTemplateApiRequestList[0].projectId",
        sourceId = "{{#addCaseTemplateApiRequest.addCaseTemplateApiRequestList?.![#this.caseTemplateId]}}")
    public Boolean batchAdd(BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest) {
        log.info("CaseTemplateApiService-batchAdd()-params: [CaseTemplateApi]={}",
            addCaseTemplateApiRequest.toString());
        try {
            List<CaseTemplateApiEntity> caseApiList = caseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(
                addCaseTemplateApiRequest.getAddCaseTemplateApiRequestList());
            caseTemplateApiRepository.insert(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateApi!", e);
            throw ExceptionUtils.mpe(ADD_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = CASE_TEMPLATE_API,
        template = "{{#result?.![#this.apiTestCase.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#result?.![#this.caseTemplateId]}}")
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateApiService-deleteById()-params: [id]={}", ids);
        try {
            sceneCaseApiCountHandler.deleteTemplateCaseByCaseTemplateApiIds(ids);
            Long count = caseTemplateApiRepository.deleteAllByIdIsIn(ids);
            customizedSceneCaseApiRepository.deleteSceneCaseApiConn(ids);
            return count > 0;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplateApi!", e);
            throw ExceptionUtils.mpe(DELETE_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_API,
        template = "{{#updateCaseTemplateApiRequest.apiTestCase.caseName}}",
        sourceId = "{{#updateCaseTemplateApiRequest.caseTemplateId}}")
    public Boolean edit(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest) {
        log.info("CaseTemplateApiService-edit()-params: [CaseTemplateApi]={}", updateCaseTemplateApiRequest.toString());
        try {
            CaseTemplateApiEntity caseTemplateApi = caseTemplateApiMapper
                .toCaseTemplateApiByUpdateRequest(updateCaseTemplateApiRequest);
            caseTemplateApiRepository.save(caseTemplateApi);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateApi!", e);
            throw ExceptionUtils.mpe(EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_API,
        template = "{{#updateCaseTemplateApiDto.updateCaseTemplateApiRequestList?.![#this.apiTestCase.caseName]}}",
        refId = "updateCaseTemplateApiRequestList[0].projectId",
        sourceId = "{{#updateCaseTemplateApiDto.updateCaseTemplateApiRequestList?.![#this.caseTemplateId]}}")
    public Boolean batchEdit(BatchUpdateCaseTemplateApiRequest updateCaseTemplateApiDto) {
        log.info("CaseTemplateApiService-batchEdit()-params: [CaseTemplateApi]={}",
            updateCaseTemplateApiDto.toString());
        try {
            List<CaseTemplateApiEntity> caseApiList = caseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(
                updateCaseTemplateApiDto.getUpdateCaseTemplateApiRequestList());
            caseTemplateApiRepository.saveAll(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to batch edit the CaseTemplateApi!", e);
            throw ExceptionUtils.mpe(BATCH_EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApiResponse> listResponseByCaseTemplateId(String caseTemplateId) {
        try {
            List<CaseTemplateApiEntity> caseTemplateApiEntityList = listByCaseTemplateId(caseTemplateId);
            return caseTemplateApiEntityList.stream().map(caseTemplateApiMapper::toCaseTemplateApiDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw ExceptionUtils.mpe(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApiEntity> listByCaseTemplateId(String caseTemplateId) {
        try {
            Example<CaseTemplateApiEntity> example = Example
                .of(CaseTemplateApiEntity.builder().caseTemplateId(caseTemplateId).build(),
                    ExampleMatcher.matching().withIgnorePaths(SceneField.IS_LOCK.getName()));
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneField.ORDER.getName());
            return caseTemplateApiRepository.findAll(example, sort);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw ExceptionUtils.mpe(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public CaseTemplateApiResponse getCaseTemplateApiById(String id) {
        try {
            Optional<CaseTemplateApiEntity> sceneCaseApi = caseTemplateApiRepository.findById(id);
            return sceneCaseApi.map(caseTemplateApiMapper::toCaseTemplateApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi by id!", e);
            throw ExceptionUtils.mpe(GET_CASE_TEMPLATE_API_BY_ID_ERROR);
        }
    }

    @Override
    public void deleteAllByCaseTemplateIds(List<String> ids) {
        caseTemplateApiRepository.deleteAllByCaseTemplateIdIsIn(ids);
    }

    @Override
    @LogRecord(operationType = CASE_SYNC, operationModule = CASE_TEMPLATE_API,
        template = "{{#request.caseName}}", sourceId = "{{#request.caseTemplateId}}")
    public Boolean syncApi(SyncApiRequest request) {
        try {
            CaseTemplateApiEntity caseTemplateApiEntity = caseTemplateApiRepository.findById(request.getCaseId())
                .orElseThrow(() -> ExceptionUtils.mpe(GET_CASE_TEMPLATE_API_BY_ID_ERROR));
            syncApiEntity(caseTemplateApiEntity.getApiTestCase().getApiEntity());
            caseTemplateApiRepository.save(caseTemplateApiEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException courierException) {
            throw courierException;
        } catch (Exception e) {
            log.error("Sync api error!", e);
            throw ExceptionUtils.mpe(CASE_SYNC_API_ERROR);
        }
    }

}
