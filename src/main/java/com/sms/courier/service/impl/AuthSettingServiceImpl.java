package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AUTH_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AuthSettingRequest;
import com.sms.courier.dto.response.AuthSettingResponse;
import com.sms.courier.mapper.AuthSettingMapper;
import com.sms.courier.repository.AuthSettingRepository;
import com.sms.courier.security.oauth.AuthSettingEntity;
import com.sms.courier.service.AuthSettingService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthSettingServiceImpl implements AuthSettingService {

    private final AuthSettingRepository authSettingRepository;
    private final AuthSettingMapper authSettingMapper;

    public AuthSettingServiceImpl(AuthSettingRepository authSettingRepository,
        AuthSettingMapper authSettingMapper) {
        this.authSettingRepository = authSettingRepository;
        this.authSettingMapper = authSettingMapper;
    }

    @Override
    public AuthSettingResponse findById(String id) {
        return null;
    }

    @Override
    public List<AuthSettingResponse> list() {
        return null;
    }


    @Override
    public Boolean add(AuthSettingRequest authSettingRequest) {
        log.info("AuthSettingService-add()-params: [AuthSetting]={}", authSettingRequest.toString());
        try {
            AuthSettingEntity authSetting = authSettingMapper.toEntity(authSettingRequest);
            authSettingRepository.insert(authSetting);
        } catch (Exception e) {
            log.error("Failed to add the AuthSetting!", e);
            throw new ApiTestPlatformException(ADD_AUTH_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(AuthSettingRequest authSettingRequest) {
        log.info("AuthSettingService-edit()-params: [AuthSetting]={}", authSettingRequest.toString());
        try {
            boolean exists = authSettingRepository.existsById(authSettingRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "AuthSetting", authSettingRequest.getId());
            }
            AuthSettingEntity authSetting = authSettingMapper.toEntity(authSettingRequest);
            authSettingRepository.save(authSetting);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the AuthSetting!", e);
            throw new ApiTestPlatformException(EDIT_AUTH_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(String id) {
        try {
            authSettingRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the AuthSetting!", e);
            throw new ApiTestPlatformException(DELETE_AUTH_SETTING_BY_ID_ERROR);
        }
        return Boolean.TRUE;
    }

}
