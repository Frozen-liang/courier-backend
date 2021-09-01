package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.EDIT_MOCK_SETTING_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.QUERY_MOCK_SETTING_API_ERROR;

import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.mapper.MockSettingMapper;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.service.MockSettingService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockSettingServiceImpl implements MockSettingService {

    private final MockSettingRepository mockSettingRepository;
    private final MockSettingMapper mockSettingMapper;

    public MockSettingServiceImpl(MockSettingRepository mockSettingRepository,
        MockSettingMapper mockSettingMapper) {
        this.mockSettingRepository = mockSettingRepository;
        this.mockSettingMapper = mockSettingMapper;
    }

    @Override
    public Boolean editUrl(MockSettingRequest mockSettingRequest) {
        log.info("MockSettingService-editUrl()-params: [mockSettingRequest]={}", mockSettingRequest.toString());
        try {
            Optional<MockSettingEntity> mockSettingEntity = mockSettingRepository.findById(mockSettingRequest.getId());
            mockSettingEntity.ifPresent(entity -> {
                entity.setMockUrl(mockSettingRequest.getMockUrl());
                mockSettingRepository.save(entity);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the Mock setting!", e);
            throw ExceptionUtils.mpe(EDIT_MOCK_SETTING_API_ERROR);
        }
    }

    @Override
    public MockSettingResponse get() {
        try {
            List<MockSettingEntity> entityList = mockSettingRepository.findAll();
            if (CollectionUtils.isNotEmpty(entityList)) {
                return mockSettingMapper.toResponse(entityList.get(0));
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to edit the Mock setting!", e);
            throw ExceptionUtils.mpe(QUERY_MOCK_SETTING_API_ERROR);
        }
    }

}
