package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_PAGE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_CASE_TEMPLATE_ERROR;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.AddCaseTemplateRequest;
import com.sms.satp.dto.CaseTemplateApiResponse;
import com.sms.satp.dto.CaseTemplateResponse;
import com.sms.satp.dto.CaseTemplateSearchDto;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.UpdateCaseTemplateRequest;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.CaseTemplateMapper;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateService;
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
public class CaseTemplateServiceImpl implements CaseTemplateService {

    private final CaseTemplateRepository sceneCaseTemplateRepository;
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private final CaseTemplateMapper caseTemplateMapper;
    private final CaseTemplateApiService caseTemplateApiService;
    private final CaseTemplateApiMapper caseTemplateApiMapper;

    public CaseTemplateServiceImpl(CaseTemplateRepository sceneCaseRepository,
        CustomizedCaseTemplateRepository customizedSceneCaseRepository,
        CaseTemplateMapper sceneCaseMapper, CaseTemplateApiService sceneCaseApiService,
        CaseTemplateApiMapper caseTemplateApiMapper) {
        this.sceneCaseTemplateRepository = sceneCaseRepository;
        this.customizedCaseTemplateRepository = customizedSceneCaseRepository;
        this.caseTemplateMapper = sceneCaseMapper;
        this.caseTemplateApiService = sceneCaseApiService;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
    }

    @Override
    public Boolean add(AddCaseTemplateRequest addCaseTemplateRequest) {
        log.info("CaseTemplateService-add()-params: [CaseTemplate]={}", addCaseTemplateRequest.toString());
        try {
            CaseTemplate sceneCaseTemplate = caseTemplateMapper.toCaseTemplateByAddRequest(addCaseTemplateRequest);
            //query user by "createUserId",write for filed createUserName.
            sceneCaseTemplateRepository.insert(sceneCaseTemplate);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplate!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateService-deleteById()-params: [id]={}", ids);
        try {
            for (String id : ids) {
                sceneCaseTemplateRepository.deleteById(id);
                List<CaseTemplateApi> caseTemplateApiList = caseTemplateApiService.listByCaseTemplateId(id);
                deleteCaseTemplateApi(caseTemplateApiList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplate!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest) {
        log.info("CaseTemplateService-edit()-params: [CaseTemplate]={}", updateCaseTemplateRequest.toString());
        try {
            CaseTemplate sceneCaseTemplate = caseTemplateMapper
                .toCaseTemplateByUpdateRequest(updateCaseTemplateRequest);
            Optional<CaseTemplate> optionalSceneCase = sceneCaseTemplateRepository.findById(sceneCaseTemplate.getId());
            optionalSceneCase.ifPresent(sceneCaseFindById -> {
                sceneCaseTemplate.setCreateUserId(sceneCaseFindById.getCreateUserId());
                sceneCaseTemplate.setCreateDateTime(sceneCaseFindById.getCreateDateTime());
                if (!Objects.equals(sceneCaseTemplate.isRemove(), sceneCaseFindById.isRemove())) {
                    editCaseTemplateApiStatus(sceneCaseTemplate, sceneCaseFindById.isRemove());
                }
                sceneCaseTemplateRepository.save(sceneCaseTemplate);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplate!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public Page<CaseTemplateResponse> page(PageDto pageDto, String projectId) {
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
    public Page<CaseTemplateResponse> search(CaseTemplateSearchDto searchDto, String projectId) {
        try {
            Page<CaseTemplate> resultPage = customizedCaseTemplateRepository.search(searchDto, projectId);
            return resultPage.map(caseTemplateMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to search the CaseTemplate!", e);
            throw new ApiTestPlatformException(SEARCH_CASE_TEMPLATE_ERROR);
        }
    }

    private void deleteCaseTemplateApi(List<CaseTemplateApi> caseTemplateApiList) {
        List<String> ids = caseTemplateApiList.stream().map(CaseTemplateApi::getId).collect(Collectors.toList());
        caseTemplateApiService.deleteByIds(ids);
    }

    private void editCaseTemplateApiStatus(CaseTemplate caseTemplate, boolean oldRemove) {
        List<CaseTemplateApiResponse> caseTemplateApiResponseList = caseTemplateApiService
            .listByCaseTemplateId(caseTemplate.getId(), oldRemove);
        List<CaseTemplateApi> caseTemplateApiList =
            caseTemplateApiMapper.toCaseTemplateApiByResponseList(caseTemplateApiResponseList);
        for (CaseTemplateApi caseTemplateApi : caseTemplateApiList) {
            caseTemplateApi.setRemove(caseTemplate.isRemove());
        }
        caseTemplateApiService.editAll(caseTemplateApiList);
    }

}
