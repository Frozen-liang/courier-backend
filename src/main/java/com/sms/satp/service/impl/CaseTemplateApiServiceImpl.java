package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.BATCH_EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR;

import com.sms.satp.common.SearchFiled;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.BatchUpdateCaseTemplateApiRequest;
import com.sms.satp.dto.CaseTemplateApiResponse;
import com.sms.satp.dto.UpdateCaseTemplateApiRequest;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.service.CaseTemplateApiService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateApiServiceImpl implements CaseTemplateApiService {

    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final CaseTemplateApiMapper aseTemplateApiMapper;

    public CaseTemplateApiServiceImpl(CaseTemplateApiRepository sceneCaseApiRepository,
        CaseTemplateApiMapper sceneCaseApiMapper) {
        this.caseTemplateApiRepository = sceneCaseApiRepository;
        this.aseTemplateApiMapper = sceneCaseApiMapper;
    }

    @Override
    public Boolean batchAdd(BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest) {
        log.info("CaseTemplateApiService-batchAdd()-params: [CaseTemplateApi]={}",
            addCaseTemplateApiRequest.toString());
        try {
            List<CaseTemplateApi> caseApiList = aseTemplateApiMapper.toCaseTemplateApiListByAddRequestList(
                addCaseTemplateApiRequest.getAddCaseTemplateApiRequestList());
            caseTemplateApiRepository.insert(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateApiService-deleteById()-params: [id]={}", ids);
        try {
            for (String id : ids) {
                caseTemplateApiRepository.deleteById(id);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public Boolean edit(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest) {
        log.info("CaseTemplateApiService-edit()-params: [CaseTemplateApi]={}", updateCaseTemplateApiRequest.toString());
        try {
            CaseTemplateApi caseTemplateApi = aseTemplateApiMapper
                .toCaseTemplateApiByUpdateRequest(updateCaseTemplateApiRequest);
            caseTemplateApiRepository.save(caseTemplateApi);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public Boolean editAll(List<CaseTemplateApi> caseTemplateApiList) {
        log.info("CaseTemplateApiService-edit()-params: [CaseTemplateApi]={}", caseTemplateApiList.toString());
        try {
            caseTemplateApiRepository.saveAll(caseTemplateApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public Boolean batchEdit(BatchUpdateCaseTemplateApiRequest updateCaseTemplateApiDto) {
        log.info("CaseTemplateApiService-batchEdit()-params: [CaseTemplateApi]={}",
            updateCaseTemplateApiDto.toString());
        try {
            if (!updateCaseTemplateApiDto.getUpdateCaseTemplateApiRequestList().isEmpty()) {
                List<CaseTemplateApi> caseApiList = aseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(
                    updateCaseTemplateApiDto.getUpdateCaseTemplateApiRequestList());
                caseTemplateApiRepository.saveAll(caseApiList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to batch edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(BATCH_EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApiResponse> listByCaseTemplateId(String caseTemplateId, boolean remove) {
        try {
            Example<CaseTemplateApi> example = Example
                .of(CaseTemplateApi.builder().caseTemplateId(caseTemplateId).removed(remove).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SearchFiled.ORDER_NUMBER.getFiledName());
            List<CaseTemplateApi> sceneCaseApiList = caseTemplateApiRepository.findAll(example, sort);
            return sceneCaseApiList.stream().map(aseTemplateApiMapper::toCaseTemplateApiDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApi> listByCaseTemplateId(String caseTemplateId) {
        try {
            Example<CaseTemplateApi> example = Example.of(
                CaseTemplateApi.builder().caseTemplateId(caseTemplateId).build());
            return caseTemplateApiRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi list by caseTemplateId!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR);
        }
    }

    @Override
    public CaseTemplateApiResponse getSceneCaseApiById(String id) {
        try {
            Optional<CaseTemplateApi> sceneCaseApi = caseTemplateApiRepository.findById(id);
            return sceneCaseApi.map(aseTemplateApiMapper::toCaseTemplateApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi by id!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_BY_ID_ERROR);
        }
    }

}
