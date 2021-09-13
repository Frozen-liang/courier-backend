package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.LOGIN_SETTING;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_LOGIN_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_LOGIN_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_LOGIN_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_LOGIN_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_LOGIN_SETTING_LIST_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.LoginSettingRequest;
import com.sms.courier.dto.response.LoginSettingResponse;
import com.sms.courier.entity.system.LoginSettingEntity;
import com.sms.courier.mapper.LoginSettingMapper;
import com.sms.courier.repository.LoginSettingRepository;
import com.sms.courier.service.LoginSettingService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginSettingServiceImpl implements LoginSettingService {

    private final LoginSettingRepository loginSettingRepository;
    private final LoginSettingMapper loginSettingMapper;

    public LoginSettingServiceImpl(LoginSettingRepository loginSettingRepository,
        LoginSettingMapper loginSettingMapper) {
        this.loginSettingRepository = loginSettingRepository;
        this.loginSettingMapper = loginSettingMapper;
    }

    @Override
    public LoginSettingResponse findById(String id) {
        return loginSettingRepository.findById(id).map(loginSettingMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_LOGIN_SETTING_BY_ID_ERROR));
    }

    @Override
    public List<LoginSettingResponse> list() {
        try {
            Sort sort = Sort.by(Direction.ASC, CREATE_DATE_TIME.getName());
            return loginSettingMapper.toDtoList(loginSettingRepository.findAll(sort));
        } catch (Exception e) {
            log.error("Failed to get the LoginSetting list!", e);
            throw new ApiTestPlatformException(GET_LOGIN_SETTING_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = LOGIN_SETTING,
        template = "{{#loginSettingRequest.emailSuffix}}")
    public Boolean add(LoginSettingRequest loginSettingRequest) {
        log.info("LoginSettingService-add()-params: [LoginSetting]={}", loginSettingRequest.toString());
        try {
            LoginSettingEntity emailSettings = loginSettingMapper.toEntity(loginSettingRequest);
            loginSettingRepository.insert(emailSettings);
        } catch (Exception e) {
            log.error("Failed to add the LoginSetting!", e);
            throw new ApiTestPlatformException(ADD_LOGIN_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = LOGIN_SETTING,
        template = "{{#loginSettingRequest.emailSuffix}}")
    public Boolean edit(LoginSettingRequest loginSettingRequest) {
        log.info("LoginSettingService-edit()-params: [LoginSetting]={}", loginSettingRequest.toString());
        try {
            boolean exists = loginSettingRepository.existsById(loginSettingRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "LoginSetting", loginSettingRequest.getId());
            }
            LoginSettingEntity emailSettings = loginSettingMapper.toEntity(loginSettingRequest);
            loginSettingRepository.save(emailSettings);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the LoginSetting!", e);
            throw new ApiTestPlatformException(EDIT_LOGIN_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = LOGIN_SETTING,
        template = "{{#result?.![#this.emailSuffix]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return loginSettingRepository.deleteByIdIn(ids) > 0;
        } catch (Exception e) {
            log.error("Failed to delete the LoginSetting!", e);
            throw new ApiTestPlatformException(DELETE_LOGIN_SETTING_BY_ID_ERROR);
        }
    }

}
