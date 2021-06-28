package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.API_TEST_CASE;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static com.sms.satp.common.field.CommonFiled.API_ID;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CustomizedApiTestCaseRepository;
import com.sms.satp.service.ApiTestCaseService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiTestCaseServiceImpl implements ApiTestCaseService {

    private final ApiTestCaseRepository apiTestCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;

    public ApiTestCaseServiceImpl(ApiTestCaseRepository apiTestCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        ApiTestCaseMapper apiTestCaseMapper) {
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
    }

    @Override
    public ApiTestCaseResponse findById(String id) {
        return apiTestCaseRepository.findById(id).map(apiTestCaseMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_TEST_CASE_BY_ID_ERROR));
    }

    @Override
    public List<ApiTestCaseResponse> list(String apiId, String projectId) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getFiled());
            ApiTestCase apiTestCase = ApiTestCase.builder().apiId(apiId).projectId(projectId).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(API_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(REMOVE.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<ApiTestCase> example = Example.of(apiTestCase, exampleMatcher);
            return apiTestCaseMapper.toDtoList(apiTestCaseRepository.findAll(example, sort));
        } catch (Exception e) {
            log.error("Failed to get the ApiTestCase list!", e);
            throw new ApiTestPlatformException(GET_API_TEST_CASE_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = API_TEST_CASE,
        template = "{{#apiTestCaseRequest.caseName}}")
    public Boolean add(ApiTestCaseRequest apiTestCaseRequest) {
        log.info("ApiTestCaseService-add()-params: [ApiTestCase]={}", apiTestCaseRequest.toString());
        try {
            ApiTestCase apiTestCase = apiTestCaseMapper.toEntity(apiTestCaseRequest);
            apiTestCaseRepository.insert(apiTestCase);
        } catch (Exception e) {
            log.error("Failed to add the ApiTestCase!", e);
            throw new ApiTestPlatformException(ADD_API_TEST_CASE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = API_TEST_CASE,
        template = "{{#apiTestCaseRequest.caseName}}")
    public Boolean edit(ApiTestCaseRequest apiTestCaseRequest) {
        log.info("ApiTestCaseService-edit()-params: [ApiTestCase]={}", apiTestCaseRequest.toString());
        try {
            boolean exists = apiTestCaseRepository.existsById(apiTestCaseRequest.getId());
            isTrue(exists, EDIT_NOT_EXIST_ERROR, "ApiTestCase", apiTestCaseRequest.getId());
            ApiTestCase apiTestCase = apiTestCaseMapper.toEntity(apiTestCaseRequest);
            apiTestCaseRepository.save(apiTestCase);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the ApiTestCase!", e);
            throw new ApiTestPlatformException(EDIT_API_TEST_CASE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = API_TEST_CASE,
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return customizedApiTestCaseRepository.deleteByIds(ids);
        } catch (Exception e) {
            log.error("Failed to delete the ApiTestCase!", e);
            throw new ApiTestPlatformException(DELETE_API_TEST_CASE_BY_ID_ERROR);
        }
    }

    @Override
    public void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status) {
        log.info("Update ApiTestCase's status to {},apiIds = {}", status.getCode(), apiIds);
        customizedApiTestCaseRepository.updateApiTestCaseStatusByApiId(apiIds, status);
    }

}