package com.sms.courier.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.listener.event.AddCaseEvent;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.service.impl.CaseApiCountHandlerImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("Tests for CaseApiCountHandler")
public class CaseApiCountHandlerTest {

    private final CaseTemplateApiRepository caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository =
        mock(CustomizedSceneCaseApiRepository.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final SceneCaseApiRepository sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
    private final CaseApiCountHandler caseApiCountHandler = new CaseApiCountHandlerImpl(caseTemplateApiRepository,
        customizedSceneCaseApiRepository, applicationEventPublisher, sceneCaseApiRepository);
    private static final ObjectId MOCK_ID = new ObjectId();

    @Test
    @DisplayName("Test the addTemplateCaseByCaseTemplateApiIds method in the CaseApiCountHandler service")
    void addTemplateCaseByCaseTemplateApiIds_test() {
        List<CaseTemplateApiEntity> entityList =
            Lists.list(CaseTemplateApiEntity.builder().id(MOCK_ID.toString())
                .caseTemplateId(MOCK_ID.toString())
                .apiType(ApiType.API)
                .projectId(MOCK_ID.toString())
                .apiTestCase(ApiTestCaseEntity.builder().projectId(MOCK_ID.toString()).apiEntity(
                    ApiEntity.builder().id(MOCK_ID.toString()).build()).build()).build());
        when(caseTemplateApiRepository.findAllByIdIsIn(any())).thenReturn(entityList);
        when(customizedSceneCaseApiRepository.findCountByCaseTemplateIdAndNowProjectId(any(), any(), anyBoolean()))
            .thenReturn(1L);
        doNothing().when(applicationEventPublisher).publishEvent(any());
        caseApiCountHandler.addTemplateCaseByCaseTemplateApiIds(List.of(MOCK_ID.toString()));
        verify(caseTemplateApiRepository, times(1)).findAllByIdIsIn(List.of(MOCK_ID.toString()));
    }

    @Test
    @DisplayName("Test the deleteTemplateCaseByCaseTemplateApiIds method in the CaseApiCountHandler service")
    void deleteTemplateCaseByCaseTemplateApiIds_test() {
        List<CaseTemplateApiEntity> entityList =
            Lists.list(CaseTemplateApiEntity.builder().id(MOCK_ID.toString())
                .caseTemplateId(MOCK_ID.toString())
                .projectId(MOCK_ID.toString())
                .apiType(ApiType.API)
                .apiTestCase(ApiTestCaseEntity.builder().projectId(MOCK_ID.toString())
                    .apiEntity(ApiEntity.builder().id(MOCK_ID.toString()).build()).build()).build());
        when(caseTemplateApiRepository.findAllByIdIsIn(any())).thenReturn(entityList);
        when(customizedSceneCaseApiRepository.findCountByCaseTemplateIdAndNowProjectId(any(), any(), anyBoolean()))
            .thenReturn(1L);
        doNothing().when(applicationEventPublisher).publishEvent(any());
        caseApiCountHandler.deleteTemplateCaseByCaseTemplateApiIds(List.of(MOCK_ID.toString()));
        verify(caseTemplateApiRepository, times(1)).findAllByIdIsIn(List.of(MOCK_ID.toString()));
    }

    @Test
    @DisplayName("Test the deleteSceneCaseBySceneCaseApiIds method in the CaseApiCountHandler service")
    void deleteSceneCaseBySceneCaseApiIds_test() {
        List<SceneCaseApiEntity> entityList =
            Lists.list(SceneCaseApiEntity.builder().id(MOCK_ID.toString())
                .caseTemplateId(MOCK_ID.toString())
                .apiType(ApiType.API)
                .apiTestCase(ApiTestCaseEntity.builder().apiEntity(
                    ApiEntity.builder().id(MOCK_ID.toString()).build()).build()).build());
        when(sceneCaseApiRepository.findAllByIdIsIn(any())).thenReturn(entityList);
        List<CaseTemplateApiEntity> caseTemplateApiEntityList =
            List.of(CaseTemplateApiEntity.builder().apiType(ApiType.API).apiTestCase(
                ApiTestCaseEntity.builder().apiEntity(ApiEntity.builder().id(MOCK_ID.toString()).build()).build())
                .build());
        when(caseTemplateApiRepository.findAllByCaseTemplateIdAndRemovedOrderByOrder(any(), anyBoolean()))
            .thenReturn(caseTemplateApiEntityList);
        doNothing().when(applicationEventPublisher).publishEvent(any());
        caseApiCountHandler.deleteSceneCaseBySceneCaseApiIds(List.of(MOCK_ID.toString()));
        verify(sceneCaseApiRepository, times(1)).findAllByIdIsIn(List.of(MOCK_ID.toString()));
    }

    @Test
    @DisplayName("Test the addSceneCaseBySceneCaseApiIds method in the CaseApiCountHandler service")
    void addSceneCaseBySceneCaseApiIds_test() {
        List<SceneCaseApiEntity> entityList =
            Lists.list(SceneCaseApiEntity.builder().id(MOCK_ID.toString())
                .apiType(ApiType.API)
                .apiTestCase(ApiTestCaseEntity.builder().apiEntity(
                    ApiEntity.builder().id(MOCK_ID.toString()).build()).build()).build());
        when(sceneCaseApiRepository.findAllByIdIsIn(any())).thenReturn(entityList);
        List<CaseTemplateApiEntity> caseTemplateApiEntityList =
            List.of(CaseTemplateApiEntity.builder().apiTestCase(
                ApiTestCaseEntity.builder().apiEntity(ApiEntity.builder().id(MOCK_ID.toString()).build()).build())
                .build());
        when(caseTemplateApiRepository.findAllByCaseTemplateIdAndRemovedOrderByOrder(any(), anyBoolean()))
            .thenReturn(caseTemplateApiEntityList);
        doNothing().when(applicationEventPublisher).publishEvent(any());
        caseApiCountHandler.addSceneCaseBySceneCaseApiIds(List.of(MOCK_ID.toString()));
        verify(sceneCaseApiRepository, times(1)).findAllByIdIsIn(List.of(MOCK_ID.toString()));
    }

    @Test
    @DisplayName("Test the addSceneCaseByApiIds method in the CaseApiCountHandler service")
    void addSceneCaseByApiIds_test() {
        caseApiCountHandler.addSceneCaseByApiIds(List.of(MOCK_ID.toString()), Boolean.TRUE);
        verify(applicationEventPublisher, times(1)).publishEvent(any(AddCaseEvent.class));
    }

    @Test
    @DisplayName("Test the addTestCaseByApiIds method in the CaseApiCountHandler service")
    void addTestCaseByApiIds_test() {
        caseApiCountHandler.addTestCaseByApiIds(List.of(MOCK_ID.toString()),null);
        verify(applicationEventPublisher, times(1)).publishEvent(any(AddCaseEvent.class));
    }

    @Test
    @DisplayName("Test the deleteTestCaseByApiIds method in the CaseApiCountHandler service")
    void deleteTestCaseByApiIds_test() {
        caseApiCountHandler.deleteTestCaseByApiIds(List.of(MOCK_ID.toString()));
        verify(applicationEventPublisher, times(1)).publishEvent(any(DeleteCaseEvent.class));
    }
}
