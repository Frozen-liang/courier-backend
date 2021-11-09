package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_WEBHOOK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_WEBHOOK_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_WEBHOOK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WEBHOOK_PAGE_ERROR;
import static com.sms.courier.common.field.WebhookField.URL;
import static com.sms.courier.common.field.WebhookField.WEBHOOK_TYPE;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.WebhookPageRequest;
import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookResponse;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.mapper.WebhookMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.WebhookRepository;
import com.sms.courier.service.WebhookService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.webhook.model.WebhookEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    private final WebhookRepository webhookRepository;
    private final CommonRepository commonRepository;
    private final WebhookMapper webhookMapper;

    public WebhookServiceImpl(WebhookRepository webhookRepository,
        CommonRepository commonRepository,
        WebhookMapper webhookMapper) {
        this.webhookRepository = webhookRepository;
        this.commonRepository = commonRepository;
        this.webhookMapper = webhookMapper;
    }


    @Override
    public Page<WebhookResponse> page(WebhookPageRequest request) {
        try {
            QueryVo queryVo = new QueryVo();
            queryVo.setCriteriaList(List.of(URL.is(request.getUrl()), WEBHOOK_TYPE.in(request.getWebhookType())));
            queryVo.setLookupVo(List.of(LookupVo.createLookupUser()));
            queryVo.setCollectionName("Webhook");
            return commonRepository.page(queryVo, request, WebhookResponse.class);
        } catch (Exception e) {
            log.error("Failed to get the Webhook list!", e);
            throw new ApiTestPlatformException(GET_WEBHOOK_PAGE_ERROR);
        }
    }


    @Override
    public Boolean add(WebhookRequest webhookRequest) {
        log.info("WebhookService-add()-params: [Webhook]={}", webhookRequest.toString());
        try {
            WebhookEntity webhook = webhookMapper.toEntity(webhookRequest);
            webhookRepository.insert(webhook);
        } catch (Exception e) {
            log.error("Failed to add the Webhook!", e);
            throw new ApiTestPlatformException(ADD_WEBHOOK_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(WebhookRequest webhookRequest) {
        log.info("WebhookService-edit()-params: [Webhook]={}", webhookRequest.toString());
        try {
            boolean exists = webhookRepository.existsById(webhookRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "Webhook", webhookRequest.getId());
            }
            WebhookEntity webhook = webhookMapper.toEntity(webhookRequest);
            webhookRepository.save(webhook);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the Webhook!", e);
            throw new ApiTestPlatformException(EDIT_WEBHOOK_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<String> ids) {
        try {
            webhookRepository.deleteByIdIn(ids);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the Webhook!", e);
            throw new ApiTestPlatformException(DELETE_WEBHOOK_BY_ID_ERROR);
        }
    }

}
