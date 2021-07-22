package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.API;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.CLEAR_RECYCLE_BIN;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.enums.OperationType.RECOVER;
import static com.sms.satp.common.enums.OperationType.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_PAGE_ERROR;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiHistoryEntity;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import com.sms.satp.mapper.ApiHistoryMapper;
import com.sms.satp.mapper.ApiMapper;
import com.sms.satp.repository.ApiHistoryRepository;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.service.AsyncService;
import com.sms.satp.service.ProjectImportSourceService;
import com.sms.satp.utils.ExceptionUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    private final ApiRepository apiRepository;
    private final ApiHistoryRepository apiHistoryRepository;
    private final ApiMapper apiMapper;
    private final ApiHistoryMapper apiHistoryMapper;
    private final CustomizedApiRepository customizedApiRepository;
    private final AsyncService asyncService;
    private final ProjectImportSourceService projectImportSourceService;


    public ApiServiceImpl(ApiRepository apiRepository, ApiHistoryRepository apiHistoryRepository, ApiMapper apiMapper,
        ApiHistoryMapper apiHistoryMapper, CustomizedApiRepository customizedApiRepository,
        AsyncService asyncService,
        ProjectImportSourceService projectImportSourceService) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.apiMapper = apiMapper;
        this.apiHistoryMapper = apiHistoryMapper;
        this.customizedApiRepository = customizedApiRepository;
        this.asyncService = asyncService;
        this.projectImportSourceService = projectImportSourceService;
    }

    @SneakyThrows
    @Override
    public boolean importDocumentByFile(ApiImportRequest apiImportRequest) {
        String content = IOUtils.toString(apiImportRequest.getFile().getInputStream(), StandardCharsets.UTF_8);
        asyncService.importApi(apiMapper.toImportSource(apiImportRequest, content));
        return true;
    }

    @Override
    public Boolean syncApiByProImpSourceIds(List<String> proImpSourceIds) {
        Iterable<ProjectImportSourceEntity> projectImportSources = projectImportSourceService
            .findByIds(proImpSourceIds);
        projectImportSources.forEach(projectImportSource -> {
            asyncService.importApi(apiMapper.toImportSource(projectImportSource));
        });
        return true;
    }

    @Override
    public ApiResponse findById(String id) {

        return customizedApiRepository.findById(id).orElseThrow(() -> ExceptionUtils.mpe(GET_API_BY_ID_ERROR));
    }

    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        try {
            return customizedApiRepository.page(apiPageRequest);
        } catch (Exception e) {
            log.error("Failed to get the Api page!", e);
            throw new ApiTestPlatformException(GET_API_PAGE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API, template = "{{#apiRequestDto.apiName}}")
    public Boolean add(ApiRequest apiRequestDto) {
        log.info("ApiService-add()-params: [Api]={}", apiRequestDto.toString());
        try {
            ApiEntity apiEntity = apiMapper.toEntity(apiRequestDto);
            ApiEntity newApiEntity = apiRepository.insert(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(newApiEntity)).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(ADD_API_ERROR);
        }
        return true;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = API, template = "{{#apiRequestDto.apiName}}")
    public Boolean edit(ApiRequest apiRequest) {
        log.info("ApiService-edit()-params: [Api]={}", apiRequest.toString());
        try {
            boolean exists = apiRepository.existsById(apiRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "Api", apiRequest.getId());
            ApiEntity apiEntity = apiMapper.toEntity(apiRequest);
            ApiEntity newApiEntity = apiRepository.save(apiEntity);
            ApiHistoryEntity apiHistoryEntity = ApiHistoryEntity.builder()
                .record(apiHistoryMapper.toApiHistoryDetail(newApiEntity)).build();
            apiHistoryRepository.insert(apiHistoryEntity);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the Api!", e);
            throw new ApiTestPlatformException(EDIT_API_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = API, template = "{{#result?.![#this.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return customizedApiRepository.deleteByIds(ids);
        } catch (Exception e) {
            log.error("Failed to delete the Api!", e);
            throw new ApiTestPlatformException(DELETE_API_BY_ID_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = API, template = "{{#result?.![#this.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("Delete api ids:{}.", ids);
        apiRepository.deleteAllByIdIn(ids);
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = CLEAR_RECYCLE_BIN, operationModule = API)
    public Boolean deleteAll() {
        log.info("Delete all api when removed is true.");
        apiRepository.deleteAllByRemovedIsTrue();
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = RECOVER, operationModule = API, template = "{{#result?.![#this.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        return customizedApiRepository.recover(ids);
    }

}
