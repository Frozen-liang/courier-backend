package com.sms.courier.engine.impl;

import static com.sms.courier.common.enums.OperationModule.ENGINE_SETTING;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.EDIT_ENGINE_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_ENGINE_SETTING_ERROR;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.EngineSettingRequest;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.EngineSettingService;
import com.sms.courier.engine.model.EngineSettingEntity;
import com.sms.courier.repository.EngineSettingRepository;
import com.sms.courier.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EngineSettingServiceImpl implements EngineSettingService {

    private final EngineSettingRepository engineSettingRepository;

    public EngineSettingServiceImpl(EngineSettingRepository engineSettingRepository) {
        this.engineSettingRepository = engineSettingRepository;
    }

    @Override
    public EngineSettingResponse findOne() {
        try {
            return engineSettingRepository.getFirstByOrderByModifyDateTimeDesc();
        } catch (Exception e) {
            log.error("Failed to get engine setting!", e);
            throw ExceptionUtils.mpe(GET_ENGINE_SETTING_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = ENGINE_SETTING)
    public Boolean edit(EngineSettingRequest request) {
        try {
            EngineSettingEntity engineSettingEntity = engineSettingRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_ENGINE_SETTING_ERROR));
            engineSettingEntity.setEnvVariable(request.getEnvVariable());
            engineSettingEntity.setVersion(request.getVersion());
            engineSettingRepository.save(engineSettingEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
