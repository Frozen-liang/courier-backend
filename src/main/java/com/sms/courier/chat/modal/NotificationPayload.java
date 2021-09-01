package com.sms.courier.chat.modal;

import com.sms.courier.chat.common.AdditionalParam;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayload {

    private Object titleVariable;
    private Object contentVariable;
    @Builder.Default
    private Map<AdditionalParam, Object> additionalParam = new HashMap<>();
}