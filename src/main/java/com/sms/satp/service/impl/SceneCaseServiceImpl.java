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
import com.sms.satp.dto.AddSceneCaseRequest;
import com.sms.satp.dto.CaseTemplateApiResponse;
import com.sms.satp.dto.CaseTemplateConnDto;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseApiResponse;
import com.sms.satp.dto.SceneCaseResponse;
import com.sms.satp.dto.SceneTemplateResponse;
import com.sms.satp.dto.SearchSceneCaseRequest;
import com.sms.satp.dto.UpdateSceneCaseApiDto;
import com.sms.satp.dto.UpdateSceneCaseRequest;
import com.sms.satp.dto.UpdateSceneTemplateRequest;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateConnMapper;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateConnService;
import com.sms.satp.service.SceneCaseApiService;
import com.sms.satp.service.SceneCaseService;
import com.sms.satp.utils.PageDtoConverter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
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
    private final SceneCaseApiMapper sceneCaseApiMapper;
    private final CaseTemplateConnService caseTemplateConnService;
    private final CaseTemplateConnMapper caseTemplateConnMapper;
    private final CaseTemplateApiService caseTemplateApiService;

    public SceneCaseServiceImpl(SceneCaseRepository sceneCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseMapper sceneCaseMapper, SceneCaseApiService sceneCaseApiService,
        SceneCaseApiMapper sceneCaseApiMapper,
        CaseTemplateConnService caseTemplateConnService,
        CaseTemplateConnMapper caseTemplateConnMapper,
        CaseTemplateApiService caseTemplateApiService) {
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseMapper = sceneCaseMapper;
        this.sceneCaseApiService = sceneCaseApiService;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
        this.caseTemplateConnService = caseTemplateConnService;
        this.caseTemplateConnMapper = caseTemplateConnMapper;
        this.caseTemplateApiService = caseTemplateApiService;
    }

    @Override
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
    public Boolean deleteByIds(List<String> ids) {
        log.info("SceneCaseService-deleteById()-params: [ids]={}", ids);
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
    public Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest) {
        log.info("SceneCaseService-edit()-params: [SceneCase]={}", updateSceneCaseRequest.toString());
        try {
            SceneCase sceneCase = sceneCaseMapper.toUpdateSceneCase(updateSceneCaseRequest);
            Optional<SceneCase> optionalSceneCase = sceneCaseRepository.findById(sceneCase.getId());
            optionalSceneCase.ifPresent(sceneCaseFindById -> {
                sceneCase.setCreateUserId(sceneCaseFindById.getCreateUserId());
                sceneCase.setCreateDateTime(sceneCaseFindById.getCreateDateTime());
                if (!Objects.equals(sceneCase.isRemove(), sceneCaseFindById.isRemove())) {
                    editSceneCaseApiStatus(sceneCase, sceneCaseFindById.isRemove());
                    editCaseTemplateStatus(sceneCase, sceneCaseFindById.isRemove());
                }
                sceneCaseRepository.save(sceneCase);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseResponse> page(PageDto pageDto, String projectId) {
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
    public Page<SceneCaseResponse> search(SearchSceneCaseRequest searchDto, String projectId) {
        try {
            Page<SceneCase> resultPage = customizedSceneCaseRepository.search(searchDto, projectId);
            return resultPage.map(sceneCaseMapper::toDto);
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
            List<CaseTemplateConnDto> caseTemplateConnDtoList = Lists.newArrayList();
            for (CaseTemplateConn conn : caseTemplateConnList) {
                CaseTemplateConnDto connDto = caseTemplateConnMapper.toCaseTemplateConnDto(conn);
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
            if (!updateSceneTemplateRequest.getSceneCaseApiDtoList().isEmpty()) {
                sceneCaseApiService.batchEdit(
                    UpdateSceneCaseApiDto.builder()
                        .sceneCaseApiDtoList(updateSceneTemplateRequest.getSceneCaseApiDtoList()).build());
            }
            if (!updateSceneTemplateRequest.getCaseTemplateConnDtoList().isEmpty()) {
                List<CaseTemplateConn> caseTemplateConnList =
                    caseTemplateConnMapper
                        .toCaseTemplateConnList(updateSceneTemplateRequest.getCaseTemplateConnDtoList());
                caseTemplateConnService.editList(caseTemplateConnList);
            }
            return Boolean.TRUE;
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
        List<String> ids = sceneCaseApiList.stream().map(SceneCaseApi::getId).collect(Collectors.toList());
        sceneCaseApiService.deleteByIds(ids);
    }

    private void deleteCaseTemplate(String id) {
        List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService.listBySceneCaseId(id);
        for (CaseTemplateConn conn : caseTemplateConnList) {
            caseTemplateConnService.deleteById(conn.getId());
        }
    }

    private void editSceneCaseApiStatus(SceneCase sceneCase, boolean oldRemove) {
        List<SceneCaseApiResponse> sceneCaseApiResponseList = sceneCaseApiService
            .listBySceneCaseId(sceneCase.getId(), oldRemove);
        List<SceneCaseApi> sceneCaseApiList = sceneCaseApiMapper.toSceneCaseApiList(sceneCaseApiResponseList);
        for (SceneCaseApi sceneCaseApi : sceneCaseApiList) {
            sceneCaseApi.setRemove(sceneCase.isRemove());
        }
        sceneCaseApiService.editAll(sceneCaseApiList);
    }

}
