package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.CaseType.CASE;
import static com.sms.courier.common.enums.CaseType.OTHER_OBJECT_SCENE_CASE_COUNT;
import static com.sms.courier.common.enums.CaseType.SCENE_CASE;

import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.listener.event.AddCaseEvent;
import com.sms.courier.common.listener.event.DeleteCaseEvent;
import com.sms.courier.dto.ApiCountIdsDto;
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
                        new ObjectId(entity.getApiTestCase().getProjectId()), Boolean.TRUE);
            if (count > 0) {
                AddCaseEvent addCaseEvent = new AddCaseEvent(List.of(entity.getApiTestCase().getApiEntity().getId()),
                    CaseType.SCENE_CASE, Integer.parseInt(String.valueOf(count)));
                applicationEventPublisher.publishEvent(addCaseEvent);
            }
            long otherObjectCount = customizedSceneCaseApiRepository
                .findCountByCaseTemplateIdAndNowProjectId(new ObjectId(entity.getCaseTemplateId()),
                    new ObjectId(entity.getApiTestCase().getProjectId()), Boolean.FALSE);
            if (otherObjectCount > 0) {
                AddCaseEvent addCaseEvent = new AddCaseEvent(List.of(entity.getApiTestCase().getApiEntity().getId()),
                    CaseType.OTHER_OBJECT_SCENE_CASE_COUNT, Integer.parseInt(String.valueOf(otherObjectCount)));
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
                        new ObjectId(entity.getApiTestCase().getProjectId()), Boolean.TRUE);
            if (count > 0) {
                DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(
                    List.of(entity.getApiTestCase().getApiEntity().getId()),
                    CaseType.SCENE_CASE, Integer.parseInt(String.valueOf(count)));
                applicationEventPublisher.publishEvent(deleteCaseEvent);
            }
            long otherObjectCount = customizedSceneCaseApiRepository
                .findCountByCaseTemplateIdAndNowProjectId(new ObjectId(entity.getCaseTemplateId()),
                    new ObjectId(entity.getApiTestCase().getProjectId()), Boolean.FALSE);
            if (otherObjectCount > 0) {
                DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(
                    List.of(entity.getApiTestCase().getApiEntity().getId()),
                    CaseType.OTHER_OBJECT_SCENE_CASE_COUNT, Integer.parseInt(String.valueOf(otherObjectCount)));
                applicationEventPublisher.publishEvent(deleteCaseEvent);
            }
        }
    }

    //删除流程用例api时，发送事件
    @Override
    public void deleteSceneCaseBySceneCaseApiIds(List<String> ids) {
        List<SceneCaseApiEntity> entityList = sceneCaseApiRepository.findAllByIdIsIn(ids);
        ApiCountIdsDto apiIds = getApiIdsByEntityList(entityList);
        if (CollectionUtils.isNotEmpty(apiIds.getSceneCaseCountApiIds())) {
            DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(apiIds.getSceneCaseCountApiIds(), SCENE_CASE, null);
            applicationEventPublisher.publishEvent(deleteCaseEvent);
        }
        if (CollectionUtils.isNotEmpty(apiIds.getOtherProjectSceneCaseCountApiIds())) {
            DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(apiIds.getOtherProjectSceneCaseCountApiIds(),
                OTHER_OBJECT_SCENE_CASE_COUNT, null);
            applicationEventPublisher.publishEvent(deleteCaseEvent);
        }
    }

    //增加流程用例api时，发送事件
    @Override
    public void addSceneCaseBySceneCaseApiIds(List<String> sceneCaseApiIds) {
        List<SceneCaseApiEntity> entityList = sceneCaseApiRepository.findAllByIdIsIn(sceneCaseApiIds);
        ApiCountIdsDto apiIds = getApiIdsByEntityList(entityList);
        if (CollectionUtils.isNotEmpty(apiIds.getSceneCaseCountApiIds())) {
            AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds.getSceneCaseCountApiIds(), SCENE_CASE, null);
            applicationEventPublisher.publishEvent(addCaseEvent);
        }
        if (CollectionUtils.isNotEmpty(apiIds.getOtherProjectSceneCaseCountApiIds())) {
            AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds.getOtherProjectSceneCaseCountApiIds(),
                OTHER_OBJECT_SCENE_CASE_COUNT, null);
            applicationEventPublisher.publishEvent(addCaseEvent);
        }
    }

    //增加流程用例api时，发送事件
    @Override
    public void addSceneCaseByApiIds(List<String> apiIds, boolean isNowObject) {
        AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds, isNowObject ? SCENE_CASE : OTHER_OBJECT_SCENE_CASE_COUNT,
            null);
        applicationEventPublisher.publishEvent(addCaseEvent);
    }

    //增加单个用例时，发送事件
    @Override
    public void addTestCaseByApiIds(List<String> apiIds, Integer count) {
        AddCaseEvent addCaseEvent = new AddCaseEvent(apiIds, CASE, null);
        applicationEventPublisher.publishEvent(addCaseEvent);
    }

    //删除单个用例时，发送事件
    @Override
    public void deleteTestCaseByApiIds(List<String> apiIds) {
        DeleteCaseEvent deleteCaseEvent = new DeleteCaseEvent(apiIds, CASE, null);
        applicationEventPublisher.publishEvent(deleteCaseEvent);
    }

    private ApiCountIdsDto getApiIdsByEntityList(List<SceneCaseApiEntity> sceneCaseApiEntityList) {
        List<String> sceneCaseApiIds = sceneCaseApiEntityList.stream()
            .filter(entity -> Objects.isNull(entity.getCaseTemplateId())
                && CollectionUtils.isEmpty(entity.getCaseTemplateApiConnList())
                && Objects.equals(entity.getProjectId(), entity.getApiTestCase().getProjectId()))
            .map(entity -> entity.getApiTestCase().getApiEntity().getId())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        List<String> otherObjectSceneCaseApiIds = sceneCaseApiEntityList.stream()
            .filter(entity -> Objects.isNull(entity.getCaseTemplateId())
                && CollectionUtils.isEmpty(entity.getCaseTemplateApiConnList())
                && !Objects.equals(entity.getProjectId(), entity.getApiTestCase().getProjectId()))
            .map(entity -> entity.getApiTestCase().getApiEntity().getId())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        List<SceneCaseApiEntity> templateApiCase = sceneCaseApiEntityList.stream()
            .filter(entity -> Objects.nonNull(entity.getCaseTemplateId()))
            .collect(Collectors.toList());
        for (SceneCaseApiEntity sceneCaseApiEntity : templateApiCase) {
            List<CaseTemplateApiEntity> caseTemplateApiEntityList = caseTemplateApiRepository
                .findAllByCaseTemplateIdAndRemovedOrderByOrder(sceneCaseApiEntity.getCaseTemplateId(), Boolean.FALSE);
            for (CaseTemplateApiEntity caseTemplateApiEntity : caseTemplateApiEntityList) {
                if (Objects.equals(caseTemplateApiEntity.getApiType(), ApiType.API)) {
                    if (Objects.equals(sceneCaseApiEntity.getProjectId(),
                        caseTemplateApiEntity.getApiTestCase().getProjectId())) {
                        sceneCaseApiIds.add(caseTemplateApiEntity.getApiTestCase().getApiEntity().getId());
                    } else {
                        otherObjectSceneCaseApiIds.add(caseTemplateApiEntity.getApiTestCase().getApiEntity().getId());
                    }
                }
            }
        }

        return ApiCountIdsDto.builder().sceneCaseCountApiIds(sceneCaseApiIds)
            .otherProjectSceneCaseCountApiIds(otherObjectSceneCaseApiIds).build();
    }

}
