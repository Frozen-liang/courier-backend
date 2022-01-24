package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.WEBHOOK;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_WEBHOOK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_WEBHOOK_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_WEBHOOK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WEBHOOK_PAGE_ERROR;
import static com.sms.courier.common.field.WebhookField.URL;
import static com.sms.courier.common.field.WebhookField.WEBHOOK_TYPE;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.WebhookPageRequest;
import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookResponse;
import com.sms.courier.dto.response.WebhookTypeResponse;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.mapper.WebhookMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.WebhookRepository;
import com.sms.courier.repository.WebhookTypeRepository;
import com.sms.courier.service.WebhookService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.JsonUtils;
import com.sms.courier.utils.MustacheUtils;
import com.sms.courier.webhook.enums.WebhookType;
import com.sms.courier.webhook.model.WebhookEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    private final WebhookRepository webhookRepository;
    private final CommonRepository commonRepository;
    private final WebhookTypeRepository webhookTypeRepository;
    private final WebhookMapper webhookMapper;
    private final RestTemplate restTemplate;
    private static final String KEY = "key";
    private static final String PARAM_TYPE = "paramType";
    private static final String JSON = "json";
    private static final String EXAMPLE = "example";

    public WebhookServiceImpl(WebhookRepository webhookRepository,
        CommonRepository commonRepository,
        WebhookTypeRepository webhookTypeRepository, WebhookMapper webhookMapper,
        RestTemplate restTemplate) {
        this.webhookRepository = webhookRepository;
        this.commonRepository = commonRepository;
        this.webhookTypeRepository = webhookTypeRepository;
        this.webhookMapper = webhookMapper;
        this.restTemplate = restTemplate;
    }


    @Override
    public Page<WebhookResponse> page(WebhookPageRequest request) {
        try {
            QueryVo queryVo = new QueryVo();
            queryVo.setCriteriaList(List.of(URL.like(request.getUrl()), WEBHOOK_TYPE.in(request.getWebhookType())));
            queryVo.setLookupVo(List.of(LookupVo.createLookupUser()));
            queryVo.setCollectionName("Webhook");
            Page<WebhookResponse> page = commonRepository.page(queryVo, request, WebhookResponse.class);
            page.forEach(webhookResponse -> webhookResponse
                .setTypeName(WebhookType.getType(webhookResponse.getWebhookType()).getName()));
            return page;
        } catch (Exception e) {
            log.error("Failed to get the Webhook list!", e);
            throw new ApiTestPlatformException(GET_WEBHOOK_PAGE_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = WEBHOOK, template = "#{{webhookRequest.url}}")
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
    @LogRecord(operationType = EDIT, operationModule = WEBHOOK, template = "#{{webhookRequest.url}}")
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
    @LogRecord(operationType = DELETE, operationModule = WEBHOOK,
        template = "{{#result?.![#this.url]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            webhookRepository.deleteByIdIn(ids);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the Webhook!", e);
            throw new ApiTestPlatformException(DELETE_WEBHOOK_BY_ID_ERROR);
        }
    }

    @Override
    public List<WebhookTypeResponse> getAllType() {
        return webhookMapper.toWebhookTypeList(webhookTypeRepository.findAll());
    }

    @Override
    public Boolean testConnection(WebhookRequest request) {
        try {
            HttpHeaders header = new HttpHeaders();
            request.getHeader().forEach(header::add);
            header.setContentType(MediaType.APPLICATION_JSON);
            List<Map<String, Object>> fieldDesc = webhookTypeRepository.findByType(request.getWebhookType())
                .getFieldDesc();
            Map<Object, Object> scope = fieldDesc.stream()
                .collect(Collectors.toMap(field -> field.get(KEY),
                    field -> JSON.equals(field.get(PARAM_TYPE)) ? JsonUtils.serializeObjectNotNull(field.get(EXAMPLE))
                        : field.get(EXAMPLE)));
            String body = MustacheUtils.getContent(request.getPayload(), scope);
            HttpEntity<String> httpEntity = new HttpEntity<>(body, header);
            restTemplate.exchange(request.getUrl(), HttpMethod.POST, httpEntity, String.class);
        } catch (RestClientException e) {
            throw ExceptionUtils.mpe("Test connection fail!", e);
        }
        return true;
    }

}
