package com.sms.courier.chat.common;

import com.sms.courier.chat.modal.TemplateField;
import com.sms.courier.dto.response.NotificationTemplateTypeResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum NotificationTemplateType {

    ACCOUNT_PWD_RESET(1, "Password-Reset-Template", accountPwdResetFiled());

    private static final List<TemplateField> accountPwdResetFiled() {
        return Arrays.asList(
            TemplateField.builder()
                .name("name").description("Username").build(),
            TemplateField.builder()
                .name("code").description("Verification code").build());
    }

    private static final List<NotificationTemplateTypeResponse> LIST;

    static {
        LIST = Arrays.stream(values()).map(type ->
            NotificationTemplateTypeResponse.builder()
                .id(type.getValue())
                .name(type.getName())
                .fields(type.getFieldList())
                .build())
            .collect(Collectors.toList());
    }

    private final int value;
    private final String name;
    private final List<TemplateField> fieldList;

    NotificationTemplateType(int value, String name, List<TemplateField> fieldList) {
        this.value = value;
        this.name = name;
        this.fieldList = fieldList;
    }

    public static List<NotificationTemplateTypeResponse> getResponse() {
        return LIST;
    }
}
