package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.webhook.enums.WebhookType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The url cannot be empty.")
    private String url;

    private String description;

    private WebhookType webhookType;
}
