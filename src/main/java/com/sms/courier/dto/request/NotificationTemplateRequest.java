package com.sms.courier.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateRequest {

    @NotNull
    private Integer type;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String titleVariableKey;
    @NotNull
    private String contentVariableKey;
}