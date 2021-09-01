package com.sms.courier.utils;

import static com.sms.courier.chat.common.AdditionalParam.EMAIL_ATTACHMENT;
import static com.sms.courier.chat.common.AdditionalParam.EMAIL_CC;
import static com.sms.courier.chat.common.AdditionalParam.EMAIL_INLINES;
import static com.sms.courier.chat.common.AdditionalParam.EMAIL_TO;

import com.sms.courier.chat.common.AdditionalParam;
import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.config.EmailProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javax.mail.MessagingException;
import org.apache.commons.collections4.MapUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class EmailUtil {

    public static final String ATTACH_PATH = "attach/";
    public static final String INLINES_PATH = "inlines/";

    public static void applyProperties(EmailProperties properties, JavaMailSenderImpl sender) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding());
        }
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()));
        }
    }

    private static Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

    public static void splitAddress(NotificationPayload notificationPayload, List<String> to, List<String> cc) {
        Map<AdditionalParam, Object> additionalParam = notificationPayload.getAdditionalParam();
        if (MapUtils.isNotEmpty(additionalParam)) {
            to.addAll(Objects.requireNonNull(AdditionalParam.getParamValueOrDefault(
                additionalParam, EMAIL_TO, List.class, new ArrayList<>())));
            cc.addAll(Objects.requireNonNull(AdditionalParam.getParamValueOrDefault(
                additionalParam, EMAIL_CC, List.class, new ArrayList<>())));
        }
    }

    public static void invokeExtension(NotificationPayload notificationPayload, MimeMessageHelper helper)
        throws MessagingException {
        Map<AdditionalParam, Object> additionalParam = notificationPayload.getAdditionalParam();
        if (MapUtils.isNotEmpty(additionalParam)) {
            addInlines(additionalParam, helper);
            addAttachments(additionalParam, helper);
        }
    }

    public static void addInlines(Map<AdditionalParam, Object> additionalParam, MimeMessageHelper helper)
        throws MessagingException {
        final Map<String, String> inlines = AdditionalParam.getParamValueOrDefault(
            additionalParam, EMAIL_INLINES, Map.class, new HashMap<>());
        if (MapUtils.isNotEmpty(inlines)) {
            for (Map.Entry<String, String> entry : inlines.entrySet()) {
                helper.addInline(entry.getKey(), new ClassPathResource(INLINES_PATH + entry.getValue()));
            }
        }
    }

    public static void addAttachments(Map<AdditionalParam, Object> additionalParam, MimeMessageHelper helper)
        throws MessagingException {
        final Map<String, String> attachments = AdditionalParam.getParamValueOrDefault(
            additionalParam, EMAIL_ATTACHMENT, Map.class, new HashMap<>());
        if (MapUtils.isNotEmpty(attachments)) {
            for (Map.Entry<String, String> entry : attachments.entrySet()) {
                helper.addAttachment(entry.getKey(), new ClassPathResource(ATTACH_PATH + entry.getValue()));
            }
        }
    }
}