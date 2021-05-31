package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EXECUTE_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_API_TEST_CASE_NOT_EXITS_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_ENVIRONMENT_NOT_EXITS_ERROR;
import static com.sms.satp.common.field.CommonFiled.API_ID;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTestCaseExecuteRequest;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.JobMapper;
import com.sms.satp.repository.ApiTestCaseJobRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.service.ApiTestCaseService;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.utils.ExceptionUtils;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
    private final CommonDeleteRepository commonDeleteRepository;
    private final ProjectEnvironmentService projectEnvironmentService;
    private final ApiTestCaseJobRepository apiTestCaseJobRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final JobMapper jobMapper;

    public ApiTestCaseServiceImpl(ApiTestCaseRepository apiTestCaseRepository,
        CommonDeleteRepository commonDeleteRepository,
        ProjectEnvironmentService projectEnvironmentService,
        ApiTestCaseJobRepository apiTestCaseJobRepository, ApiTestCaseMapper apiTestCaseMapper,
        JobMapper jobMapper) {
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.commonDeleteRepository = commonDeleteRepository;
        this.projectEnvironmentService = projectEnvironmentService;
        this.apiTestCaseJobRepository = apiTestCaseJobRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.jobMapper = jobMapper;
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
    public Boolean edit(ApiTestCaseRequest apiTestCaseRequest) {
        log.info("ApiTestCaseService-edit()-params: [ApiTestCase]={}", apiTestCaseRequest.toString());
        try {
            boolean exists = apiTestCaseRepository.existsById(apiTestCaseRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiTestCase", apiTestCaseRequest.getId());
            }
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
    public Boolean delete(List<String> ids) {
        try {
            return commonDeleteRepository.deleteByIds(ids, ApiTestCase.class);
        } catch (Exception e) {
            log.error("Failed to delete the ApiTestCase!", e);
            throw new ApiTestPlatformException(DELETE_API_TEST_CASE_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean execute(ApiTestCaseExecuteRequest apiTestCaseExecuteRequest) {
        DataCollectionRequest dataCollectionRequest = apiTestCaseExecuteRequest.getDataCollectionRequest();
        try {
            ApiTestCase apiTestCase =
                apiTestCaseRepository.findById(apiTestCaseExecuteRequest.getApiTestCaseId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_API_TEST_CASE_NOT_EXITS_ERROR));
            ProjectEnvironment projectEnvironment = projectEnvironmentService
                .findOne(apiTestCaseExecuteRequest.getEnvId());
            if (Objects.isNull(projectEnvironment)) {
                throw ExceptionUtils.mpe(THE_ENVIRONMENT_NOT_EXITS_ERROR);
            }
            ApiTestCaseJob apiTestCaseJob = ApiTestCaseJob.builder()
                .apiTestCase(jobMapper.toJobApiTestCase(apiTestCase))
                .environment(jobMapper.toJobEnvironment(projectEnvironment))
                .build();
            if (Objects.nonNull(dataCollectionRequest) && CollectionUtils
                .isNotEmpty(dataCollectionRequest.getDataList())) {
                JobDataCollection jobDataCollection = jobMapper.toJobDataCollection(dataCollectionRequest);
                dataCollectionRequest.getDataList().forEach(dataList -> {
                    jobDataCollection.setTestData(jobMapper.toTestDataEntity(dataList));
                    apiTestCaseJob.setDataCollection(jobDataCollection);
                    apiTestCaseJob.setId(null);
                    apiTestCaseJobRepository.insert(apiTestCaseJob);
                });
                return true;
            }
            apiTestCaseJobRepository.insert(apiTestCaseJob);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Execute the ApiTestCase error");
            throw ExceptionUtils.mpe(EXECUTE_API_TEST_CASE_ERROR);
        }
        return true;
    }

}