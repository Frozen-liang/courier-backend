package com.sms.courier.chat.common;

import com.sms.courier.chat.modal.TemplateField;
import com.sms.courier.dto.response.NotificationTemplateTypeResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum NotificationTemplateType {

    ACCOUNT_PWD_RESET(1, "Password-Reset-Template", accountPwdResetFiled()),
    TEST_REPORT(2, "Test-Report-Template", testReportFiled()),
    SCHEDULE_TEST_REPORT(3, "Schedule-Test-Report-Template", scheduleTestReportFiled());

    private static List<TemplateField> scheduleTestReportFiled() {
        return List.of(
            TemplateField.builder().name("name").description("Schedule name").build(),
            TemplateField.builder().name("dataName").description("Data name").build(),
            TemplateField.builder().name("projectId").description("Project Id").build(),
            TemplateField.builder().name("testCompletionTime").description("Test Completion Time").build(),
            TemplateField.builder().name("testStartTime").description("Test Start Time").build(),
            TemplateField.builder().name("success").description("Success count").build(),
            TemplateField.builder().name("fail").description("Fail count").build()
        );
    }

    private static List<TemplateField> testReportFiled() {
        return List.of(
            TemplateField.builder().name("name").description("Case name").build(),
            TemplateField.builder().name("dataName").description("Data name").build(),
            TemplateField.builder().name("projectId").description("Project Id").build(),
            TemplateField.builder().name("totalTimeCost").description("Execution time").build(),
            TemplateField.builder().name("paramsTotalTimeCost").description("Parameter conversion time").build(),
            TemplateField.builder().name("delayTimeTotalTimeCost").description("Delay time").build(),
            TemplateField.builder().name("success").description("Success count").build(),
            TemplateField.builder().name("fail").description("Fail count").build()
        );
    }

    private static List<TemplateField> accountPwdResetFiled() {
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
