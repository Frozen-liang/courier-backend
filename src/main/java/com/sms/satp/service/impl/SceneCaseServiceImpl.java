package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.SCENE_CASE;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_SCENE_CASE_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.dto.response.CaseTemplateConnResponse;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.dto.response.SceneTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateConnMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateConnService;
import com.sms.satp.service.SceneCaseApiService;
import com.sms.satp.service.SceneCaseService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseServiceImpl implements SceneCaseService {

    private final SceneCaseRepository sceneCaseRepository;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final SceneCaseMapper sceneCaseMapper;
    private final SceneCaseApiService sceneCaseApiService;
    private final CaseTemplateConnService caseTemplateConnService;
    private final CaseTemplateConnMapper caseTemplateConnMapper;
    private final CaseTemplateApiService caseTemplateApiService;

    public SceneCaseServiceImpl(SceneCaseRepository sceneCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseMapper sceneCaseMapper, SceneCaseApiService sceneCaseApiService,
        CaseTemplateConnService caseTemplateConnService,
        CaseTemplateConnMapper caseTemplateConnMapper,
        CaseTemplateApiService caseTemplateApiService) {
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseMapper = sceneCaseMapper;
        this.sceneCaseApiService = sceneCaseApiService;
        this.caseTemplateConnService = caseTemplateConnService;
        this.caseTemplateConnMapper = caseTemplateConnMapper;
        this.caseTemplateApiService = caseTemplateApiService;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE, template = "{{#addSceneCaseRequest.name}}")
    public Boolean add(AddSceneCaseRequest addSceneCaseRequest) {
        log.info("SceneCaseService-add()-params: [SceneCase]={}", addSceneCaseRequest.toString());
        try {
            SceneCase sceneCase = sceneCaseMapper.toAddSceneCase(addSceneCaseRequest);
            //query user by "createUserId",write for filed createUserName.
            sceneCaseRepository.insert(sceneCase);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCase!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE, template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("SceneCaseService-deleteById()-params: [ids]={}", ids.toString());
        try {
            for (String id : ids) {
                sceneCaseRepository.deleteById(id);
                deleteSceneCaseApi(id);
                deleteCaseTemplate(id);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE, template = "{{#updateSceneCaseRequest.name}}")
    public Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest) {
        log.info("SceneCaseService-edit()-params: [SceneCase]={}", updateSceneCaseRequest.toString());
        try {
            SceneCase sceneCase = sceneCaseMapper.toUpdateSceneCase(updateSceneCaseRequest);
            updateSceneCase(sceneCase);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE, template = "{{#sceneCaseList[0].name}}")
    public Boolean batchEdit(List<SceneCase> sceneCaseList) {
        log.info("SceneCaseService-batchEdit()-params: [SceneCase]={}", sceneCaseList.toString());
        try {
            for (SceneCase sceneCase : sceneCaseList) {
                updateSceneCase(sceneCase);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseResponse> page(SearchSceneCaseRequest searchDto, ObjectId projectId) {
        try {
            return customizedSceneCaseRepository.search(searchDto, projectId);
        } catch (Exception e) {
            log.error("Failed to search the SceneCase!", e);
            throw new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR);
        }
    }

    @Override
    public SceneTemplateResponse getConn(String id) {
        try {
            Optional<SceneCase> optional = sceneCaseRepository.findById(id);
            SceneCaseResponse dto = sceneCaseMapper.toDto(optional.orElse(null));
            List<SceneCaseApiResponse> sceneCaseApiDtoList = sceneCaseApiService.listBySceneCaseId(id, Boolean.FALSE);
            List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService.listBySceneCaseId(id, Boolean.FALSE);
            List<CaseTemplateConnResponse> caseTemplateConnDtoList = Lists.newArrayList();
            for (CaseTemplateConn conn : caseTemplateConnList) {
                CaseTemplateConnResponse connDto = caseTemplateConnMapper.toCaseTemplateConnDto(conn);
                List<CaseTemplateApiResponse> caseTemplateApiDtoList =
                    caseTemplateApiService.listByCaseTemplateId(conn.getCaseTemplateId(), Boolean.FALSE);
                connDto.setCaseTemplateApiDtoList(caseTemplateApiDtoList);
                caseTemplateConnDtoList.add(connDto);
            }
            return SceneTemplateResponse.builder().sceneCaseDto(dto).sceneCaseApiDtoList(sceneCaseApiDtoList)
                .caseTemplateConnDtoList(caseTemplateConnDtoList).build();
        } catch (Exception e) {
            log.error("Failed to get the SceneCase conn!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public Boolean editConn(UpdateSceneTemplateRequest updateSceneTemplateRequest) {
        log.info("SceneCaseService-editConn()-params: [SceneTemplateDto]={}", updateSceneTemplateRequest.toString());
        try {
            if (CollectionUtils.isNotEmpty(updateSceneTemplateRequest.getUpdateSceneCaseApiRequests())) {
                sceneCaseApiService.batchEdit(
                    BatchUpdateSceneCaseApiRequest.builder()
                        .sceneCaseApiRequestList(updateSceneTemplateRequest.getUpdateSceneCaseApiRequests()).build());
            }
            if (CollectionUtils.isNotEmpty(updateSceneTemplateRequest.getUpdateCaseTemplateConnRequests())) {
                List<CaseTemplateConn> caseTemplateConnList =
                    caseTemplateConnMapper
                        .toCaseTemplateConnList(updateSceneTemplateRequest.getUpdateCaseTemplateConnRequests());
                caseTemplateConnService.editList(caseTemplateConnList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase conn!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public List<SceneCase> get(String groupId, String projectId) {
        try {
            SceneCase sceneCase = SceneCase.builder().groupId(groupId).projectId(projectId).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonFiled.PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(CommonFiled.GROUP_ID.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<SceneCase> example = Example.of(sceneCase, exampleMatcher);
            return sceneCaseRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the SceneCase!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_ERROR);
        }
    }

    private void updateSceneCase(SceneCase sceneCase) {
        Optional<SceneCase> optionalSceneCase = sceneCaseRepository.findById(sceneCase.getId());
        optionalSceneCase.ifPresent(sceneCaseFindById -> {
            if (!Objects.equals(sceneCase.getRemoved(), sceneCaseFindById.getRemoved())) {
                editSceneCaseApiStatus(sceneCase, sceneCaseFindById.getRemoved());
                editCaseTemplateStatus(sceneCase, sceneCaseFindById.getRemoved());
            }
            sceneCaseRepository.save(sceneCase);
        });
    }

    private void editCaseTemplateStatus(SceneCase sceneCase, Boolean oldRemove) {
        List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService
            .listBySceneCaseId(sceneCase.getId(), oldRemove);
        for (CaseTemplateConn conn : caseTemplateConnList) {
            conn.setRemoved(sceneCase.getRemoved());
            caseTemplateConnService.edit(conn);
        }
    }

    private void deleteSceneCaseApi(String id) {
        List<SceneCaseApi> sceneCaseApiList = sceneCaseApiService.listBySceneCaseId(id);
        if (CollectionUtils.isNotEmpty(sceneCaseApiList)) {
            List<String> ids = sceneCaseApiList.stream().map(SceneCaseApi::getId).collect(Collectors.toList());
            sceneCaseApiService.deleteByIds(ids);
        }
    }

    private void deleteCaseTemplate(String id) {
        List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService.listBySceneCaseId(id);
        for (CaseTemplateConn conn : caseTemplateConnList) {
            caseTemplateConnService.deleteById(conn.getId());
        }
    }

    private void editSceneCaseApiStatus(SceneCase sceneCase, Boolean oldRemove) {
        List<SceneCaseApi> sceneCaseApiList = sceneCaseApiService
            .getApiBySceneCaseId(sceneCase.getId(), oldRemove);
        if (CollectionUtils.isNotEmpty(sceneCaseApiList)) {
            for (SceneCaseApi sceneCaseApi : sceneCaseApiList) {
                sceneCaseApi.setRemoved(sceneCase.getRemoved());
            }
            sceneCaseApiService.editAll(sceneCaseApiList);
        }
    }

}
