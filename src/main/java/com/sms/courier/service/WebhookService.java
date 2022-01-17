package com.sms.courier.service;

import com.sms.courier.dto.request.WebhookPageRequest;
import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookResponse;
import com.sms.courier.dto.response.WebhookTypeResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface WebhookService {

    Page<WebhookResponse> page(WebhookPageRequest request);

    Boolean add(WebhookRequest webhookRequest);

    Boolean edit(WebhookRequest webhookRequest);

    Boolean delete(List<String> ids);

    List<WebhookTypeResponse> getAllType();
}
