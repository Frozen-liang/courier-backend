package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_WEBHOOK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_WEBHOOK_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_WEBHOOK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WEBHOOK_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.WebhookPageRequest;
import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookTypeResponse;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.mapper.WebhookMapper;
import com.sms.courier.mapper.WebhookMapperImpl;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.WebhookRepository;
import com.sms.courier.repository.WebhookTypeRepository;
import com.sms.courier.service.impl.WebhookServiceImpl;
import com.sms.courier.webhook.enums.WebhookType;
import com.sms.courier.webhook.model.WebhookEntity;
import com.sms.courier.webhook.model.WebhookTypeEntity;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@DisplayName("Tests for WebhookService")
class WebhookServiceTest {

    private final WebhookRepository webhookRepository = mock(WebhookRepository.class);
    private final WebhookTypeRepository webhookTypeRepository = mock(WebhookTypeRepository.class);
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final WebhookMapper webhookMapper = new WebhookMapperImpl();
    private final WebhookService webhookService = new WebhookServiceImpl(
        webhookRepository, commonRepository, webhookTypeRepository, webhookMapper, restTemplate);
    private final WebhookEntity webhook = WebhookEntity.builder().id(ID).build();
    private final WebhookRequest webhookRequest = WebhookRequest.builder().webhookType(WebhookType.CASE_REPORT).payload("")
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();


    @Test
    @DisplayName("Test the add method in the Webhook service")
    public void add_test() {
        when(webhookRepository.insert(any(WebhookEntity.class))).thenReturn(webhook);
        assertThat(webhookService.add(webhookRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding Webhook")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(webhookRepository).insert(any(WebhookEntity.class));
        assertThatThrownBy(() -> webhookService.add(webhookRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_WEBHOOK_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the Webhook service")
    public void edit_test() {
        when(webhookRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(webhookRepository.save(any(WebhookEntity.class))).thenReturn(webhook);
        assertThat(webhookService.edit(webhookRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit Webhook")
    public void edit_exception_test() {
        when(webhookRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(webhookRepository).save(any(WebhookEntity.class));
        assertThatThrownBy(() -> webhookService.edit(webhookRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_WEBHOOK_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit Webhook")
    public void edit_not_exist_exception_test() {
        when(webhookRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> webhookService.edit(webhookRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the page method in the Webhook service")
    public void page_test() {
        when(commonRepository.page(any(QueryVo.class), any(), any())).thenReturn(Page.empty());
        assertThat(webhookService.page(new WebhookPageRequest())).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while getting Webhook page")
    public void page_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).page(any(QueryVo.class), any(), any());
        assertThatThrownBy(() -> webhookService.page(new WebhookPageRequest()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_WEBHOOK_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the Webhook service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        doNothing().when(webhookRepository).deleteByIdIn(ids);
        assertThat(webhookService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Webhook")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(webhookRepository).deleteByIdIn(ids);
        assertThatThrownBy(() -> webhookService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_WEBHOOK_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getAllType method in the Webhook service")
    public void getAllType_test() {
        when(webhookTypeRepository.findAll()).thenReturn(Collections.emptyList());
        List<WebhookTypeResponse> result = webhookService.getAllType();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test the testConnection method in the Webhook service")
    public void testConnection_test() {
        when(webhookTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(restTemplate.exchange(anyString(),any(HttpMethod.class),any(),any(Class.class))).thenReturn(null);
        WebhookTypeEntity webhookTypeEntity = new WebhookTypeEntity();
        webhookTypeEntity.setFieldDesc(Collections.emptyList());
        when(webhookTypeRepository.findByType(any())).thenReturn(webhookTypeEntity);
        Boolean result = webhookService.testConnection(webhookRequest);
        assertThat(result).isTrue();
    }

}
