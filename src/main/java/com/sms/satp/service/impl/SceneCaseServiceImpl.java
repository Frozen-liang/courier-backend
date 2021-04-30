package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.SEARCH_SCENE_CASE_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.entity.dto.AddSceneCaseDto;
import com.sms.satp.entity.dto.CaseTemplateApiDto;
import com.sms.satp.entity.dto.CaseTemplateConnDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.dto.SceneCaseDto;
import com.sms.satp.entity.dto.SceneCaseSearchDto;
import com.sms.satp.entity.dto.SceneTemplateDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseDto;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateConnMapper;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateConnService;
import com.sms.satp.service.SceneCaseApiService;
import com.sms.satp.service.SceneCaseService;
import com.sms.satp.service.event.entity.SceneCaseApiLogEvent;
import com.sms.satp.utils.PageDtoConverter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseServiceImpl implements SceneCaseService {

    private final SceneCaseRepository sceneCaseRepository;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final SceneCaseMapper sceneCaseMapper;
    private final SceneCaseApiService sceneCaseApiService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SceneCaseApiLogMapper sceneCaseApiLogMapper;
    private final CaseTemplateConnService caseTemplateConnService;
    private final CaseTemplateConnMapper caseTemplateConnMapper;
    private final CaseTemplateApiService caseTemplateApiService;

    public SceneCaseServiceImpl(SceneCaseRepository sceneCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseMapper sceneCaseMapper, SceneCaseApiService sceneCaseApiService,
        ApplicationEventPublisher applicationEventPublisher,
        SceneCaseApiLogMapper sceneCaseApiLogMapper,
        CaseTemplateConnService caseTemplateConnService,
        CaseTemplateConnMapper caseTemplateConnMapper,
        CaseTemplateApiService caseTemplateApiService) {
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseMapper = sceneCaseMapper;
        this.sceneCaseApiService = sceneCaseApiService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.sceneCaseApiLogMapper = sceneCaseApiLogMapper;
        this.caseTemplateConnService = caseTemplateConnService;
        this.caseTemplateConnMapper = caseTemplateConnMapper;
        this.caseTemplateApiService = caseTemplateApiService;
    }

    @Override
    public void add(AddSceneCaseDto sceneCaseDto) {
        log.info("SceneCaseService-add()-params: [SceneCase]={}", sceneCaseDto.toString());
        try {
            SceneCase sceneCase = sceneCaseMapper.toAddSceneCase(sceneCaseDto);
            //query user by "createUserId",write for filed createUserName.
            sceneCaseRepository.insert(sceneCase);
            publishSceneCaseEvent(sceneCase, OperationType.ADD);
        } catch (Exception e) {
            log.error("Failed to add the SceneCase!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        log.info("SceneCaseService-deleteById()-params: [id]={}", id);
        try {
            Optional<SceneCase> sceneCase = sceneCaseRepository.findById(id);
            sceneCase.ifPresent(scene -> {
                sceneCaseRepository.deleteById(id);
                deleteSceneCaseApi(id);
                deleteCaseTemplate(id);
                publishSceneCaseEvent(scene, OperationType.DELETE);
            });
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR);
        }
    }

    @Override
    public void edit(UpdateSceneCaseDto sceneCaseDto) {
        log.info("SceneCaseService-edit()-params: [SceneCase]={}", sceneCaseDto.toString());
        try {
            SceneCase sceneCase = sceneCaseMapper.toUpdateSceneCase(sceneCaseDto);
            Optional<SceneCase> optionalSceneCase = sceneCaseRepository.findById(sceneCase.getId());
            optionalSceneCase.ifPresent(sceneCaseFindById -> {
                sceneCase.setCreateUserId(sceneCaseFindById.getCreateUserId());
                sceneCase.setCreateDateTime(sceneCaseFindById.getCreateDateTime());
                if (!Objects.equals(sceneCase.isRemove(), sceneCaseFindById.isRemove())) {
                    editSceneCaseApiStatus(sceneCase, sceneCaseFindById.isRemove());
                    editCaseTemplateStatus(sceneCase, sceneCaseFindById.isRemove());
                }
                sceneCaseRepository.save(sceneCase);
                publishSceneCaseEvent(sceneCase, OperationType.EDIT);
            });
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseDto> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            SceneCase sceneCase = SceneCase.builder()
                .projectId(projectId)
                .remove(Boolean.FALSE)
                .build();
            Example<SceneCase> example = Example.of(sceneCase);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return sceneCaseRepository.findAll(example, pageable)
                .map(sceneCaseMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the SceneCase page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_PAGE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseDto> search(SceneCaseSearchDto searchDto, String projectId) {
        try {
            Page<SceneCase> resultPage = customizedSceneCaseRepository.search(searchDto, projectId);
            return resultPage.map(sceneCaseMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to search the SceneCase!", e);
            throw new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR);
        }
    }

    @Override
    public SceneTemplateDto getConn(String id) {
        try {
            Optional<SceneCase> optional = sceneCaseRepository.findById(id);
            SceneCaseDto dto = sceneCaseMapper.toDto(optional.orElse(null));
            List<SceneCaseApiDto> sceneCaseApiDtoList = sceneCaseApiService.listBySceneCaseId(id, Boolean.FALSE);
            List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService.listBySceneCaseId(id, Boolean.FALSE);
            List<CaseTemplateConnDto> caseTemplateConnDtoList = Lists.newArrayList();
            for (CaseTemplateConn conn : caseTemplateConnList) {
                CaseTemplateConnDto connDto = caseTemplateConnMapper.toCaseTemplateConnDto(conn);
                List<CaseTemplateApiDto> caseTemplateApiDtoList =
                    caseTemplateApiService.listByCaseTemplateId(conn.getCaseTemplateId(), Boolean.FALSE);
                connDto.setCaseTemplateApiDtoList(caseTemplateApiDtoList);
                caseTemplateConnDtoList.add(connDto);
            }
            return SceneTemplateDto.builder().sceneCaseDto(dto).sceneCaseApiDtoList(sceneCaseApiDtoList)
                .caseTemplateConnDtoList(caseTemplateConnDtoList).build();
        } catch (Exception e) {
            log.error("Failed to get the SceneCase conn!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public void editConn(SceneTemplateDto dto) {
        log.info("SceneCaseService-editConn()-params: [SceneTemplateDto]={}", dto.toString());
        try {
            if (!dto.getSceneCaseApiDtoList().isEmpty()) {
                sceneCaseApiService.batchEdit(
                    UpdateSceneCaseApiDto.builder().sceneCaseApiDtoList(dto.getSceneCaseApiDtoList()).build());
            }
            if (!dto.getCaseTemplateConnDtoList().isEmpty()) {
                List<CaseTemplateConn> caseTemplateConnList =
                    caseTemplateConnMapper.toCaseTemplateConnList(dto.getCaseTemplateConnDtoList());
                caseTemplateConnService.editList(caseTemplateConnList);
            }
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase conn!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_CONN_ERROR);
        }
    }

    private void editCaseTemplateStatus(SceneCase sceneCase, boolean oldRemove) {
        List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService
            .listBySceneCaseId(sceneCase.getId(), oldRemove);
        for (CaseTemplateConn conn : caseTemplateConnList) {
            conn.setRemove(sceneCase.isRemove());
            caseTemplateConnService.edit(conn);
        }
    }

    private void deleteSceneCaseApi(String id) {
        List<SceneCaseApi> sceneCaseApiList = sceneCaseApiService.listBySceneCaseId(id);
        for (SceneCaseApi sceneCaseApi : sceneCaseApiList) {
            sceneCaseApiService.deleteById(sceneCaseApi.getId());
        }
    }

    private void deleteCaseTemplate(String id) {
        List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService.listBySceneCaseId(id);
        for (CaseTemplateConn conn : caseTemplateConnList) {
            caseTemplateConnService.deleteById(conn.getId());
        }
    }

    private void editSceneCaseApiStatus(SceneCase sceneCase, boolean oldRemove) {
        List<SceneCaseApiDto> sceneCaseApiDtoList = sceneCaseApiService.listBySceneCaseId(sceneCase.getId(), oldRemove);
        for (SceneCaseApiDto dto : sceneCaseApiDtoList) {
            dto.setRemove(sceneCase.isRemove());
            sceneCaseApiService.edit(dto);
        }
    }

    private void publishSceneCaseEvent(SceneCase sceneCase, OperationType operationType) {
        SceneCaseApiLogDto sceneCaseApiLogDto = sceneCaseApiLogMapper.toDtoBySceneCase(sceneCase, operationType);
        SceneCaseApiLogEvent event = new SceneCaseApiLogEvent(this, sceneCaseApiLogDto);
        applicationEventPublisher.publishEvent(event);
    }


}
