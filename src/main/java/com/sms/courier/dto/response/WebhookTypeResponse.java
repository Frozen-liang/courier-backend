package com.sms.courier.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookTypeResponse {

    private Integer type;
    private String name;
    private String defaultPayload;
    private List<Map<String, Object>> fieldDesc;
}