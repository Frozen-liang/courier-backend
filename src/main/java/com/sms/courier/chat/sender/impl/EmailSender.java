package com.sms.courier.chat.sender.impl;

import static com.sms.courier.chat.common.NotificationTemplateType.ACCOUNT_PWD_RESET;
import static com.sms.courier.chat.common.NotificationTemplateType.TEST_REPORT;
import static com.sms.courier.common.exception.ErrorCode.MAIL_SERVICE_IS_DISABLE;
import static com.sms.courier.common.exception.ErrorCode.NOTIFICATION_TEMPLATE_NOT_EXIST;
import static com.sms.courier.utils.Assert.isTrue;
import static com.sms.courier.utils.EmailUtil.applyProperties;
import static com.sms.courier.utils.EmailUtil.invokeExtension;
import static com.sms.courier.utils.EmailUtil.splitAddress;

import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.entity.notification.EmailServiceEntity;
import com.sms.courier.entity.notification.NotificationTemplateEntity;
import com.sms.courier.service.EmailService;
import com.sms.courier.service.NotificationTemplateService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@Component
public class EmailSender extends AbstractSender implements InitializingBean {

    private boolean enabled = false;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;
    private final SpringTemplateEngine engine;
    private final NotificationTemplateService notificationTemplateService;

    public EmailSender(JavaMailSender javaMailSender, EmailService emailService,
        SpringTemplateEngine engine, NotificationTemplateService notificationTemplateService) {
        this.javaMailSender = javaMailSender;
        this.emailService = emailService;
        this.engine = engine;
        this.notificationTemplateService = notificationTemplateService;
    }

    @Override
    public void updateConfiguration() {
        EmailServiceEntity entity = emailService.getEntityWithDecryptPwd();
        applyProperties(entity.getProperties(), (JavaMailSenderImpl) javaMailSender);
        this.enabled = entity.getEnabled();
    }

    @Override
    public boolean validateConnection() {
        try {
            ((JavaMailSenderImpl) javaMailSender).testConnection();
        } catch (Exception exception) {
            log.warn("Mail server is not available, {}", exception.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean sendResetPwdNotification(NotificationPayload notificationPayload) {
        isTrue(enabled, MAIL_SERVICE_IS_DISABLE);
        NotificationTemplateEntity template = notificationTemplateService.findTemplateByType(ACCOUNT_PWD_RESET);
        isTrue(Objects.nonNull(template), NOTIFICATION_TEMPLATE_NOT_EXIST, ACCOUNT_PWD_RESET.getName());

        String title = handleVariable(template.getTitle(), template.getTitleVariableKey(),
            notificationPayload.getTitleVariable());
        String content = handleVariable(template.getContent(), template.getContentVariableKey(),
            notificationPayload.getContentVariable());
        send(notificationPayload, title, content);
        return true;
    }

    @Override
    public boolean sendTestReportNotification(NotificationPayload notificationPayload) {
        isTrue(enabled, MAIL_SERVICE_IS_DISABLE);
        NotificationTemplateEntity template = notificationTemplateService.findTemplateByType(TEST_REPORT);
        isTrue(Objects.nonNull(template), NOTIFICATION_TEMPLATE_NOT_EXIST, TEST_REPORT.getName());
        String title = handleVariable(template.getTitle(), template.getTitleVariableKey(),
            notificationPayload.getTitleVariable());
        String content = handleVariable(template.getContent(), template.getContentVariableKey(),
            notificationPayload.getContentVariable());
        send(notificationPayload, title, content);
        return true;
    }

    private void send(NotificationPayload notificationPayload, String title, String content) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        splitAddress(notificationPayload, to, cc);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(to.toArray(new String[]{}));
            helper.setCc(cc.toArray(new String[]{}));
            helper.setText(content, true);
            helper.setSubject(title);
            helper.setFrom(emailService.getEmailConfigurationResponse().getUsername());
            invokeExtension(notificationPayload, helper);
            javaMailSender.send(message);
        } catch (Exception exception) {
            log.error("Failed to send email", exception);
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            enabled = validateConnection();
            EmailServiceEntity entity = emailService.getEntityWithDecryptPwd();
            if (entity.getEnabled()) {
                applyProperties(entity.getProperties(), (JavaMailSenderImpl) javaMailSender);
                enabled = true;
            }
        } catch (Exception exception) {
            log.error("Failed to init JavaMailSender", exception);
        }
    }

    @Override
    protected String processTemplate(String template, String variableKey, Object variable) {
        Context context = new Context();
        context.setVariable(variableKey, variable);
        return engine.process(template, context);
    }

}