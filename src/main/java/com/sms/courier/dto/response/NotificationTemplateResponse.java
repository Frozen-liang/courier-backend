package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateResponse {

    private String title;
    private String content;
    private String titleVariableKey;
    private String contentVariableKey;
}