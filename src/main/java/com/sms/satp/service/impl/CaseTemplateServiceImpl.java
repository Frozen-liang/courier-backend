package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_CASE_TEMPLATE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.SEARCH_CASE_TEMPLATE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.dto.CaseTemplateApiDto;
import com.sms.satp.entity.dto.CaseTemplateDto;
import com.sms.satp.entity.dto.CaseTemplateSearchDto;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.CaseTemplateMapper;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateService;
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
public class CaseTemplateServiceImpl implements CaseTemplateService {

    private final CaseTemplateRepository sceneCaseTemplateRepository;
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private final CaseTemplateMapper caseTemplateMapper;
    private final CaseTemplateApiService caseTemplateApiService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SceneCaseApiLogMapper sceneCaseApiLogMapper;

    public CaseTemplateServiceImpl(CaseTemplateRepository sceneCaseRepository,
        CustomizedCaseTemplateRepository customizedSceneCaseRepository,
        CaseTemplateMapper sceneCaseMapper, CaseTemplateApiService sceneCaseApiService,
        ApplicationEventPublisher applicationEventPublisher,
        SceneCaseApiLogMapper sceneCaseApiLogMapper) {
        this.sceneCaseTemplateRepository = sceneCaseRepository;
        this.customizedCaseTemplateRepository = customizedSceneCaseRepository;
        this.caseTemplateMapper = sceneCaseMapper;
        this.caseTemplateApiService = sceneCaseApiService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.sceneCaseApiLogMapper = sceneCaseApiLogMapper;
    }

    @Override
    public void add(CaseTemplateDto sceneCaseTemplateDto) {
        log.info("CaseTemplateService-add()-params: [CaseTemplate]={}", sceneCaseTemplateDto.toString());
        try {
            CaseTemplate sceneCaseTemplate = caseTemplateMapper.toAddCaseTemplate(sceneCaseTemplateDto);
            //query user by "createUserId",write for filed createUserName.
            sceneCaseTemplateRepository.insert(sceneCaseTemplate);
            publishSceneCaseTemplateEvent(sceneCaseTemplate, OperationType.ADD);
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplate!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        log.info("CaseTemplateService-deleteById()-params: [id]={}", id);
        try {
            Optional<CaseTemplate> sceneCaseTemplate = sceneCaseTemplateRepository.findById(id);
            sceneCaseTemplate.ifPresent(scene -> {
                sceneCaseTemplateRepository.deleteById(id);
                List<CaseTemplateApi> sceneCaseApiList = caseTemplateApiService.listByCaseTemplateId(id);
                deleteSceneCaseApi(sceneCaseApiList);
                publishSceneCaseTemplateEvent(scene, OperationType.DELETE);
            });
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplate!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public void edit(CaseTemplateDto caseTemplateDto) {
        log.info("CaseTemplateService-edit()-params: [CaseTemplate]={}", caseTemplateDto.toString());
        try {
            CaseTemplate sceneCaseTemplate = caseTemplateMapper.toUpdateCaseTemplate(caseTemplateDto);
            Optional<CaseTemplate> optionalSceneCase = sceneCaseTemplateRepository.findById(sceneCaseTemplate.getId());
            optionalSceneCase.ifPresent(sceneCaseFindById -> {
                sceneCaseTemplate.setCreateUserId(sceneCaseFindById.getCreateUserId());
                sceneCaseTemplate.setCreateDateTime(sceneCaseFindById.getCreateDateTime());
                if (!Objects.equals(sceneCaseTemplate.isRemove(), sceneCaseFindById.isRemove())) {
                    editCaseTemplateApiStatus(sceneCaseTemplate, sceneCaseFindById.isRemove());
                }
                sceneCaseTemplateRepository.save(sceneCaseTemplate);
                publishSceneCaseTemplateEvent(sceneCaseTemplate, OperationType.EDIT);
            });
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplate!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public Page<CaseTemplateDto> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            CaseTemplate caseTemplate = CaseTemplate.builder()
                .projectId(projectId)
                .remove(Boolean.FALSE)
                .build();
            Example<CaseTemplate> example = Example.of(caseTemplate);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return sceneCaseTemplateRepository.findAll(example, pageable)
                .map(caseTemplateMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplate page!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_PAGE_ERROR);
        }
    }

    @Override
    public Page<CaseTemplateDto> search(CaseTemplateSearchDto searchDto, String projectId) {
        try {
            Page<CaseTemplate> resultPage = customizedCaseTemplateRepository.search(searchDto, projectId);
            return resultPage.map(caseTemplateMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to search the CaseTemplate!", e);
            throw new ApiTestPlatformException(SEARCH_CASE_TEMPLATE_ERROR);
        }
    }

    private void deleteSceneCaseApi(List<CaseTemplateApi> caseTemplateApiList) {
        for (CaseTemplateApi caseTemplateApi : caseTemplateApiList) {
            caseTemplateApiService.deleteById(caseTemplateApi.getId());
        }
    }

    private void editCaseTemplateApiStatus(CaseTemplate caseTemplate, boolean oldRemove) {
        List<CaseTemplateApiDto> sceneCaseApiDtoList = caseTemplateApiService
            .listByCaseTemplateId(caseTemplate.getId(), oldRemove);
        for (CaseTemplateApiDto dto : sceneCaseApiDtoList) {
            dto.setRemove(caseTemplate.isRemove());
            caseTemplateApiService.edit(dto);
        }
    }

    private void publishSceneCaseTemplateEvent(CaseTemplate sceneCaseTemplate, OperationType operationType) {
        SceneCaseApiLogDto sceneCaseApiLogDto = sceneCaseApiLogMapper.toDtoBySceneCaseTemplate(sceneCaseTemplate,
            operationType);
        SceneCaseApiLogEvent event = new SceneCaseApiLogEvent(this, sceneCaseApiLogDto);
        applicationEventPublisher.publishEvent(event);
    }

}
