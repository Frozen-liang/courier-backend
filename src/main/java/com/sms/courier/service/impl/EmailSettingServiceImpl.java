package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.DISABLE_EMAIL_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.ENABLE_EMAIL_SERVICE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPDATE_EMAIL_CONFIGURATION_ERROR;

import com.sms.courier.chat.sender.Sender;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.mapper.EmailServiceMapper;
import com.sms.courier.repository.EmailServiceRepository;
import com.sms.courier.service.EmailService;
import com.sms.courier.service.EmailSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSettingServiceImpl implements EmailSettingService {

    private final Sender sender;
    private final EmailService emailService;
    private final EmailServiceMapper emailServiceMapper;
    private final EmailServiceRepository emailServiceRepository;

    public EmailSettingServiceImpl(
        Sender sender,
        EmailService emailService, EmailServiceMapper emailServiceMapper,
        EmailServiceRepository emailServiceRepository) {
        this.sender = sender;
        this.emailService = emailService;
        this.emailServiceMapper = emailServiceMapper;
        this.emailServiceRepository = emailServiceRepository;
    }

    @Override
    public boolean updateEmailConfiguration(EmailRequest emailRequest) {
        try {
            emailServiceRepository.deleteAll();
            EmailServiceEntity entity = emailServiceMapper.toEntity(emailRequest);
            emailServiceRepository.save(entity);
            sender.updateConfiguration();
            return true;
        } catch (Exception exception) {
            log.error(UPDATE_EMAIL_CONFIGURATION_ERROR.getMessage(), exception);
            throw new ApiTestPlatformException(UPDATE_EMAIL_CONFIGURATION_ERROR);
        }
    }

    @Override
    public boolean enable() {
        try {
            EmailServiceEntity entity = emailService.getEmailServiceEntity();
            entity.setEnabled(Boolean.TRUE);
            emailServiceRepository.save(entity);
            return true;
        } catch (Exception exception) {
            log.error(ENABLE_EMAIL_SERVICE_ERROR.getMessage(), exception);
            throw new ApiTestPlatformException(ENABLE_EMAIL_SERVICE_ERROR);
        }
    }

    @Override
    public boolean disable() {
        try {
            EmailServiceEntity entity = emailService.getEmailServiceEntity();
            entity.setEnabled(Boolean.FALSE);
            emailServiceRepository.save(entity);
            return true;
        } catch (Exception exception) {
            log.error(DISABLE_EMAIL_SERVICE_ERROR.getMessage(), exception);
            throw new ApiTestPlatformException(DISABLE_EMAIL_SERVICE_ERROR);
        }
    }
}