package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.EMAIL_CONFIGURATION_NOT_EXIST;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.dto.response.EmailPropertiesResponse;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.mapper.EmailServiceMapper;
import com.sms.courier.repository.EmailServiceRepository;
import com.sms.courier.service.EmailService;
import com.sms.courier.utils.AesUtil;
import java.util.Iterator;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailServiceMapper emailServiceMapper;
    private final EmailServiceRepository emailServiceRepository;

    public EmailServiceImpl(EmailServiceMapper emailServiceMapper,
        EmailServiceRepository emailServiceRepository) {
        this.emailServiceMapper = emailServiceMapper;
        this.emailServiceRepository = emailServiceRepository;
    }

    @Override
    public EmailServiceEntity getEmailServiceEntity() {
        Iterator<EmailServiceEntity> iterator = emailServiceRepository.findAll().iterator();
        isTrue(iterator.hasNext(), EMAIL_CONFIGURATION_NOT_EXIST);
        return iterator.next();
    }

    @Override
    public EmailPropertiesResponse getEmailConfigurationResponse() {
        EmailServiceEntity entity = getEmailServiceEntity();
        return emailServiceMapper.toResponse(entity.getProperties());
    }

    @Override
    public EmailServiceEntity getEntityWithDecryptPwd() {
        EmailServiceEntity entity = getEmailServiceEntity();
        isTrue(Objects.nonNull(entity.getProperties()), EMAIL_CONFIGURATION_NOT_EXIST);
        entity.getProperties().setPassword(AesUtil.decrypt(entity.getProperties().getPassword()));
        return entity;
    }

    @Override
    public boolean isServiceEnabled() {
        EmailServiceEntity entity = getEmailServiceEntity();
        return entity.getEnabled();
    }
}