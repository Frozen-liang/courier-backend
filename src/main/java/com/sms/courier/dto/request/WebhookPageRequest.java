package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import com.sms.courier.webhook.enums.WebhookType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WebhookPageRequest extends PageDto {

    private String url;

    private String description;

    private List<WebhookType> webhookType;
}
