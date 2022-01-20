package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.webhook.enums.WebhookType;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {

    private String id;

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The name cannot be empty.")
    private String name;

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The url cannot be empty.")
    private String url;

    private String description;

    @NotNull(groups = {UpdateGroup.class, InsertGroup.class}, message = "The webhookType cannot be null.")
    private WebhookType webhookType;

    @Default
    private Map<String, String> header = new HashMap<>();

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The payload cannot be empty.")
    private String payload;
}
