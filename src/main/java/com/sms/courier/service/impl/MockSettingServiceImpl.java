package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.EDIT_MOCK_SETTING_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_MOCK_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.QUERY_MOCK_SETTING_API_ERROR;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.MockSettingService;
import com.sms.courier.utils.ExceptionUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockSettingServiceImpl implements MockSettingService {

    private final MockSettingRepository mockSettingRepository;
    private final JwtTokenManager jwtTokenManager;
    private final AccessTokenProperties accessTokenProperties;

    public MockSettingServiceImpl(MockSettingRepository mockSettingRepository,
        JwtTokenManager jwtTokenManager, AccessTokenProperties accessTokenProperties) {
        this.mockSettingRepository = mockSettingRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.accessTokenProperties = accessTokenProperties;
    }


    @Override
    @LogRecord(operationType = OperationType.EDIT, operationModule = OperationModule.MOCK_SETTING)
    public Boolean edit(MockSettingRequest request) {
        try {
            MockSettingEntity mockSettingEntity =
                mockSettingRepository.findById(request.getId()).orElseThrow(() -> ExceptionUtils.mpe(
                    GET_MOCK_SETTING_BY_ID_ERROR));
            mockSettingEntity.setImageName(request.getImageName());
            mockSettingEntity.setContainerName(request.getContainerName());
            mockSettingEntity.setMockUrl(request.getMockUrl());
            mockSettingEntity.setVersion(request.getVersion());
            mockSettingEntity.setEnvVariable(request.getEnvVariable());
            mockSettingRepository.save(mockSettingEntity);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the Mock setting!", e);
            throw ExceptionUtils.mpe(EDIT_MOCK_SETTING_API_ERROR);
        }
    }

    @Override
    public MockSettingResponse findOne() {
        try {
            return mockSettingRepository.getFirstByOrderByModifyDateTimeDesc();
        } catch (Exception e) {
            log.error("Failed to edit the Mock setting!", e);
            throw ExceptionUtils.mpe(QUERY_MOCK_SETTING_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = OperationType.RESET, operationModule = OperationModule.MOCK_SETTING)
    public Boolean resetToken(String id) {
        try {
            MockSettingEntity mockSettingEntity =
                mockSettingRepository.findById(id).orElseThrow(() -> ExceptionUtils.mpe(GET_MOCK_SETTING_BY_ID_ERROR));
            CustomUser mock = CustomUser.createMock();
            String key = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
            accessTokenProperties.setMockSecretKey(key);
            String token = jwtTokenManager.generateAccessToken(mock);
            Map<String, String> envVariable = Collections.isEmpty(mockSettingEntity.getEnvVariable()) ? new HashMap<>()
                : mockSettingEntity.getEnvVariable();
            envVariable.put(Constants.API_TOKEN, token);
            mockSettingEntity.setSecretKey(key);
            mockSettingEntity.setEnvVariable(envVariable);
            mockSettingRepository.save(mockSettingEntity);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the Mock setting!", e);
            throw ExceptionUtils.mpe(QUERY_MOCK_SETTING_API_ERROR);
        }
    }

}
