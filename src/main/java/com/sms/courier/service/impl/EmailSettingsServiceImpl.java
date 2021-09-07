package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_EMAIL_SETTINGS_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_EMAIL_SETTINGS_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_EMAIL_SETTINGS_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_EMAIL_SETTINGS_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_EMAIL_SETTINGS_LIST_ERROR;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.EmailSettingsRequest;
import com.sms.courier.dto.response.EmailSettingsResponse;
import com.sms.courier.entity.system.EmailSettingsEntity;
import com.sms.courier.mapper.EmailSettingsMapper;
import com.sms.courier.repository.EmailSettingsRepository;
import com.sms.courier.service.EmailSettingsService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSettingsServiceImpl implements EmailSettingsService {

    private final EmailSettingsRepository emailSettingsRepository;
    private final EmailSettingsMapper emailSettingsMapper;

    public EmailSettingsServiceImpl(EmailSettingsRepository emailSettingsRepository,
        EmailSettingsMapper emailSettingsMapper) {
        this.emailSettingsRepository = emailSettingsRepository;
        this.emailSettingsMapper = emailSettingsMapper;
    }

    @Override
    public EmailSettingsResponse findById(String id) {
        return emailSettingsRepository.findById(id).map(emailSettingsMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_EMAIL_SETTINGS_BY_ID_ERROR));
    }

    @Override
    public List<EmailSettingsResponse> list() {
        try {
            Sort sort = Sort.by(Direction.ASC, CREATE_DATE_TIME.getName());
            return emailSettingsMapper.toDtoList(emailSettingsRepository.findAll(sort));
        } catch (Exception e) {
            log.error("Failed to get the EmailSettings list!", e);
            throw new ApiTestPlatformException(GET_EMAIL_SETTINGS_LIST_ERROR);
        }
    }


    @Override
    public Boolean add(EmailSettingsRequest emailSettingsRequest) {
        log.info("EmailSettingsService-add()-params: [EmailSettings]={}", emailSettingsRequest.toString());
        try {
            EmailSettingsEntity emailSettings = emailSettingsMapper.toEntity(emailSettingsRequest);
            emailSettingsRepository.insert(emailSettings);
        } catch (Exception e) {
            log.error("Failed to add the EmailSettings!", e);
            throw new ApiTestPlatformException(ADD_EMAIL_SETTINGS_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(EmailSettingsRequest emailSettingsRequest) {
        log.info("EmailSettingsService-edit()-params: [EmailSettings]={}", emailSettingsRequest.toString());
        try {
            boolean exists = emailSettingsRepository.existsById(emailSettingsRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "EmailSettings", emailSettingsRequest.getId());
            }
            EmailSettingsEntity emailSettings = emailSettingsMapper.toEntity(emailSettingsRequest);
            emailSettingsRepository.save(emailSettings);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the EmailSettings!", e);
            throw new ApiTestPlatformException(EDIT_EMAIL_SETTINGS_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<String> ids) {
        try {
            return emailSettingsRepository.deleteByIdIn(ids) > 0;
        } catch (Exception e) {
            log.error("Failed to delete the EmailSettings!", e);
            throw new ApiTestPlatformException(DELETE_EMAIL_SETTINGS_BY_ID_ERROR);
        }
    }

}
