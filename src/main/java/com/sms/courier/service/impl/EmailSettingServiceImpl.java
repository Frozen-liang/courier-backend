package com.sms.courier.service.impl;

import com.sms.courier.chat.sender.Sender;
import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.mapper.EmailServiceMapper;
import com.sms.courier.repository.EmailServiceRepository;
import com.sms.courier.service.EmailService;
import com.sms.courier.service.EmailSettingService;
import org.springframework.stereotype.Service;

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
        emailServiceRepository.deleteAll();
        EmailServiceEntity entity = emailServiceMapper.toEntity(emailRequest);
        emailServiceRepository.save(entity);
        sender.updateConfiguration();
        return true;
    }

    @Override
    public boolean enable() {
        EmailServiceEntity entity = emailService.getEmailServiceEntity();
        entity.setEnabled(Boolean.TRUE);
        emailServiceRepository.save(entity);
        return true;
    }

    @Override
    public boolean disable() {
        EmailServiceEntity entity = emailService.getEmailServiceEntity();
        entity.setEnabled(Boolean.FALSE);
        emailServiceRepository.save(entity);
        return true;
    }
}