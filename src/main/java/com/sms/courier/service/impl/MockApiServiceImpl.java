package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.MOCK_API;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.entity.mock.MockApiEntity;
import com.sms.courier.mapper.MockApiMapper;
import com.sms.courier.repository.MockApiRepository;
import com.sms.courier.service.MockApiService;
import com.sms.courier.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockApiServiceImpl implements MockApiService {

    private final MockApiRepository mockApiRepository;
    private final MockApiMapper mockApiMapper;

    public MockApiServiceImpl(MockApiRepository mockApiRepository, MockApiMapper mockApiMapper) {
        this.mockApiRepository = mockApiRepository;
        this.mockApiMapper = mockApiMapper;
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

}
