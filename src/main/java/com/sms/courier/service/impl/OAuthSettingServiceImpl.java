package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AUTH_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_AUTH_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_AUTH_SETTING_LIST_ERROR;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.OAuthSettingRequest;
import com.sms.courier.dto.response.OAuthSettingResponse;
import com.sms.courier.mapper.AuthSettingMapper;
import com.sms.courier.repository.OAuthSettingRepository;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.service.OAuthSettingService;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OAuthSettingServiceImpl implements OAuthSettingService {

    private final OAuthSettingRepository oauthSettingRepository;
    private final AuthSettingMapper authSettingMapper;

    public OAuthSettingServiceImpl(OAuthSettingRepository oauthSettingRepository,
        AuthSettingMapper authSettingMapper) {
        this.oauthSettingRepository = oauthSettingRepository;
        this.authSettingMapper = authSettingMapper;
    }

    @Override
    public OAuthSettingResponse findById(String id) {
        return oauthSettingRepository.findById(id).map(authSettingMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_AUTH_SETTING_BY_ID_ERROR));
    }

    @Override
    public List<OAuthSettingResponse> list() {
        try {
            return authSettingMapper.toDtoList(oauthSettingRepository.findAll());
        } catch (Exception e) {
            log.error("Get auth setting list error!", e);
            throw ExceptionUtils.mpe(GET_AUTH_SETTING_LIST_ERROR);
        }
    }


    @Override
    public Boolean add(OAuthSettingRequest authSettingRequest) {
        log.info("AuthSettingService-add()-params: [AuthSetting]={}", authSettingRequest.toString());
        try {
            OAuthSettingEntity authSetting = authSettingMapper.toEntity(authSettingRequest);
            Assert.isFalse(oauthSettingRepository.existsByName(authSettingRequest.getName()),
                "The %s already exists!", authSettingRequest.getName());
            authSetting.setClientSecret(AesUtil.encrypt(authSettingRequest.getClientSecret()));
            oauthSettingRepository.insert(authSetting);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the AuthSetting!", e);
            throw new ApiTestPlatformException(ADD_AUTH_SETTING_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(OAuthSettingRequest authSettingRequest) {
        log.info("AuthSettingService-edit()-params: [AuthSetting]={}", authSettingRequest.toString());
        try {
            OAuthSettingEntity oldAuthSetting = oauthSettingRepository.findById(authSettingRequest.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "AuthSetting", authSettingRequest.getId()));
            Assert.isFalse(!authSettingRequest.getName().equals(oldAuthSetting.getName()) && oauthSettingRepository
                    .existsByName(authSettingRequest.getName()),
                "The %s already exists!", authSettingRequest.getName());
            OAuthSettingEntity authSetting = authSettingMapper.toEntity(authSettingRequest);
            if (StringUtils.isNotBlank(authSettingRequest.getClientSecret())) {
                authSetting.setClientSecret(AesUtil.encrypt(authSettingRequest.getClientSecret()));
            } else {
                authSetting.setClientSecret(oldAuthSetting.getClientSecret());
            }
            oauthSettingRepository.save(authSetting);
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
            oauthSettingRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the AuthSetting!", e);
            throw new ApiTestPlatformException(DELETE_AUTH_SETTING_BY_ID_ERROR);
        }
        return Boolean.TRUE;
    }

}
