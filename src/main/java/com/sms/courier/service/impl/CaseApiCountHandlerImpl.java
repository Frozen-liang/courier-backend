package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.CaseType.CASE;
import static com.sms.courier.common.enums.CaseType.SCENE_CASE;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.listener.event.AddCaseEvent;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.service.CaseApiCountHandler;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CaseApiCountHandlerImpl implements CaseApiCountHandler {

    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SceneCaseApiRepository sceneCaseApiRepository;

    public CaseApiCountHandlerImpl(
        CaseTemplateApiRepository caseTemplateApiRepository,
        CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository,
        ApplicationEventPublisher applicationEventPublisher,
        SceneCaseApiRepository sceneCaseApiRepository) {
        this.caseTemplateApiRepository = caseTemplateApiRepository;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
    }

    //增加测试模板api时，查询引用的流程用例个数，并发送事件。
    @Override
    public void addTemplateCaseByCaseTemplateApiIds(List<String> ids) {
        List<CaseTemplateApiEntity> entityList =
            caseTemplateApiRepository.findAllByIdIsIn(ids).stream()
                .filter(entity -> Objects.equals(entity.getApiType(), ApiType.API)).collect(Collectors.toList());
        for (CaseTemplateApiEntity entity : entityList) {
            long count =
                customizedSceneCaseApiRepository
                    .findCountByCaseTemplateIdAndNowProjectId(new ObjectId(entity.getCaseTemplateId()),
                        new ObjectId(entity.getApiTestCase().getProjectId()));
            if (count > 0) {
                AddCaseEvent addCaseEvent = new AddCaseEvent(List.of(entity.getApiTestCase().getApiEntity().getId()),
                    CaseType.SCENE_CASE, Integer.parseInt(String.valueOf(count)));
                applicationEventPublisher.publishEvent(addCaseEvent);
            }
        }
    }

    //删除测试模板api时，查询引用的流程用例个数，并发送事件。
    @Override
    public void deleteTemplateCaseByCaseTemplateApiIds(List<String> ids) {
        List<CaseTemplateApiEntity> entityList =
            caseTemplateApiRepository.findAllByIdIsIn(ids).stream()
                .filter(entity -> Objects.equals(entity.getApiType(), ApiType.API)).collect(Collectors.toList());
        for (CaseTemplateApiEntity entity : entityList) {
            long count =
                customizedSceneCaseApiRepository
                    .findCountByCaseTemplateIdAndNowProjectId(new ObjectId(entity.getCaseTemplateId()),
                        new ObjectId(entity.getApiTestCase().getProjectId()));
            if (count > 0) {
                DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(
                    List.of(entity.getApiTestCase().getApiEntity().getId()),
                    CaseType.SCENE_CASE, Integer.parseInt(String.valueOf(count)));
                applicationEventPublisher.publishEvent(deleteCaseEvent);
            }
        }
    }

    //删除流程用例api时，发送事件
    @Override
    public void deleteSceneCaseBySceneCaseApiIds(List<String> ids) {
        List<SceneCaseApiEntity> entityList = sceneCaseApiRepository.findAllByIdIsIn(ids);
        List<String> apiIds = getApiIdsByEntityList(entityList);
        DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(apiIds, SCENE_CASE, null);
        applicationEventPublisher.publishEvent(deleteCaseEvent);
    }

    //增加流程用例api时，发送事件
    @Override
    public void addSceneCaseBySceneCaseApiIds(List<String> sceneCaseApiIds) {
        List<SceneCaseApiEntity> entityList = sceneCaseApiRepository.findAllByIdIsIn(sceneCaseApiIds);
        List<String> apiIds = getApiIdsByEntityList(entityList);
        AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds, SCENE_CASE, null);
        applicationEventPublisher.publishEvent(addCaseEvent);
    }

    //增加流程用例api时，发送事件
    @Override
    public void addSceneCaseByApiIds(List<String> apiIds) {
        AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds, SCENE_CASE, null);
        applicationEventPublisher.publishEvent(addCaseEvent);
    }

    //增加单个用例时，发送事件
    @Override
    public void addTestCaseByApiIds(List<String> apiIds) {
        AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds, CASE, null);
        applicationEventPublisher.publishEvent(addCaseEvent);
    }

    //删除单个用例时，发送事件
    @Override
    public void deleteTestCaseByApiIds(List<String> apiIds) {
        DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(apiIds, CASE, null);
        applicationEventPublisher.publishEvent(deleteCaseEvent);
    }

    private List<String> getApiIdsByEntityList(List<SceneCaseApiEntity> sceneCaseApiEntityList) {
        List<String> sceneCaseApiIds = sceneCaseApiEntityList.stream()
            .filter(entity -> Objects.isNull(entity.getCaseTemplateId())
                && CollectionUtils.isEmpty(entity.getCaseTemplateApiConnList())
                && Objects.equals(entity.getProjectId(), entity.getApiTestCase().getProjectId()))
            .map(entity -> entity.getApiTestCase().getApiEntity().getId())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        List<String> templateIds = sceneCaseApiEntityList.stream()
            .map(SceneCaseApiEntity::getCaseTemplateId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        List<String> templateApiIds = CollectionUtils.isNotEmpty(templateIds)
            ? caseTemplateApiRepository.findAllByCaseTemplateIdIn(templateIds)
            .stream()
            .filter(entity -> Objects.equals(entity.getApiType(), ApiType.API)
                && Objects.equals(entity.getProjectId(), entity.getApiTestCase().getProjectId()))
            .map(entity -> entity.getApiTestCase().getApiEntity().getId())
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
            : Lists.newArrayList();
        sceneCaseApiIds.addAll(templateApiIds);
        return sceneCaseApiIds;
    }

}
