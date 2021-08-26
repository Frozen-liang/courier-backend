package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.MOCK_API;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_MOCK_API_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_MOCK_API_PAGE_ERROR;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.MockApiResponse;
import com.sms.courier.dto.response.MockApiResponseList;
import com.sms.courier.dto.response.MockApiResponsePage;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.mock.MockApiEntity;
import com.sms.courier.mapper.ApiMapper;
import com.sms.courier.mapper.MockApiMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedMockApiRepository;
import com.sms.courier.repository.MockApiRepository;
import com.sms.courier.service.MockApiService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockApiServiceImpl implements MockApiService {

    private final MockApiRepository mockApiRepository;
    private final MockApiMapper mockApiMapper;
    private final CustomizedMockApiRepository customizedMockApiRepository;
    private final ApiRepository apiRepository;
    private final ApiMapper apiMapper;

    public MockApiServiceImpl(MockApiRepository mockApiRepository, MockApiMapper mockApiMapper,
        CustomizedMockApiRepository customizedMockApiRepository, ApiRepository apiRepository,
        ApiMapper apiMapper) {
        this.mockApiRepository = mockApiRepository;
        this.mockApiMapper = mockApiMapper;
        this.customizedMockApiRepository = customizedMockApiRepository;
        this.apiRepository = apiRepository;
        this.apiMapper = apiMapper;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = MOCK_API, template = "{{#request.mockName}}")
    public Boolean add(MockApiRequest request) {
        log.info("MockApiService-add()-params: [MockApiRequest]={}", request.toString());
        try {
            MockApiEntity mockApiEntity = mockApiMapper.toEntity(request);
            mockApiRepository.insert(mockApiEntity);
            return true;
        } catch (Exception e) {
            log.error("Failed to add the MockApi!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_ERROR);
        }
    }

    @Override
    public MockApiResponsePage page(ObjectId apiId, MockApiPageRequest pageRequest) {
        try {
            ApiEntity apiEntity = apiRepository.findApiEntityByIdAndRemoved(String.valueOf(apiId), false);
            if (Objects.nonNull(apiEntity)) {
                Page<MockApiResponse> mockApiResponsePage = customizedMockApiRepository.page(apiId, pageRequest);
                ApiResponse apiResponse = apiMapper.toDto(apiEntity);
                return MockApiResponsePage.builder().mockApiResponsePage(mockApiResponsePage).apiResponse(apiResponse)
                    .build();
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to get the MockApi page!", e);
            throw ExceptionUtils.mpe(GET_MOCK_API_PAGE_ERROR);
        }
    }

    @Override
    public MockApiResponseList list(ObjectId apiId, Boolean isEnable) {
        try {
            ApiEntity apiEntity = apiRepository.findApiEntityByIdAndRemoved(String.valueOf(apiId), false);
            if (Objects.nonNull(apiEntity)) {
                List<MockApiResponse> mockApiResponseList = customizedMockApiRepository.list(apiId, isEnable);
                ApiResponse apiResponse = apiMapper.toDto(apiEntity);
                return MockApiResponseList.builder().mockApiResponseList(mockApiResponseList).apiResponse(apiResponse)
                    .build();
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to get the MockApi list!", e);
            throw ExceptionUtils.mpe(GET_MOCK_API_LIST_ERROR);
        }
    }


}
