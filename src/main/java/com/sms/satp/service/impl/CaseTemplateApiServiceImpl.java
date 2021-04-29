package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.ErrorCode.BATCH_EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.satp.common.ErrorCode.GET_CASE_TEMPLATE_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.SearchFiled;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.entity.dto.AddCaseTemplateApiDto;
import com.sms.satp.entity.dto.CaseTemplateApiDto;
import com.sms.satp.entity.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.dto.UpdateCaseTemplateApiDto;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.event.entity.SceneCaseApiLogEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateApiServiceImpl implements CaseTemplateApiService {

    private final CaseTemplateApiRepository caseTemplateApiRepository;
    private final CaseTemplateApiMapper aseTemplateApiMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SceneCaseApiLogMapper sceneCaseApiLogMapper;

    public CaseTemplateApiServiceImpl(CaseTemplateApiRepository sceneCaseApiRepository,
        CaseTemplateApiMapper sceneCaseApiMapper,
        ApplicationEventPublisher applicationEventPublisher,
        SceneCaseApiLogMapper sceneCaseApiLogMapper) {
        this.caseTemplateApiRepository = sceneCaseApiRepository;
        this.aseTemplateApiMapper = sceneCaseApiMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.sceneCaseApiLogMapper = sceneCaseApiLogMapper;
    }

    @Override
    public void batch(AddCaseTemplateApiDto addCaseTemplateApiDto) {
        log.info("CaseTemplateApiService-batch()-params: [CaseTemplateApi]={}", addCaseTemplateApiDto.toString());
        try {
            List<CaseTemplateApi> caseApiList =
                aseTemplateApiMapper.toCaseTemplateApiList(addCaseTemplateApiDto.getApiDtoList());
            caseTemplateApiRepository.insert(caseApiList);
            publishBatchCaseTemplateApiEvent(caseApiList, OperationType.ADD);
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        log.info("CaseTemplateApiService-deleteById()-params: [id]={}", id);
        try {
            Optional<CaseTemplateApi> sceneCaseApi = caseTemplateApiRepository.findById(id);
            sceneCaseApi.ifPresent(api -> {
                caseTemplateApiRepository.deleteById(id);
                publishCaseTemplateApiEvent(api, OperationType.DELETE);
            });
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public void edit(CaseTemplateApiDto caseTemplateApiDto) {
        log.info("CaseTemplateApiService-edit()-params: [CaseTemplateApi]={}", caseTemplateApiDto.toString());
        try {
            CaseTemplateApi caseTemplateApi = aseTemplateApiMapper.toCaseTemplateApi(caseTemplateApiDto);
            caseTemplateApiRepository.save(caseTemplateApi);
            publishCaseTemplateApiEvent(caseTemplateApi, OperationType.EDIT);
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public void batchEdit(UpdateCaseTemplateApiDto updateCaseTemplateApiDto) {
        log.info("CaseTemplateApiService-batchEdit()-params: [CaseTemplateApi]={}",
            updateCaseTemplateApiDto.toString());
        try {
            if (!updateCaseTemplateApiDto.getApiDtoList().isEmpty()) {
                List<CaseTemplateApi> caseApiList = aseTemplateApiMapper
                    .toCaseTemplateApiList(updateCaseTemplateApiDto.getApiDtoList());
                caseTemplateApiRepository.saveAll(caseApiList);
                publishBatchCaseTemplateApiEvent(caseApiList, OperationType.EDIT);
            }
        } catch (Exception e) {
            log.error("Failed to batch edit the CaseTemplateApi!", e);
            throw new ApiTestPlatformException(BATCH_EDIT_CASE_TEMPLATE_API_ERROR);
        }
    }

    @Override
    public List<CaseTemplateApiDto> listByCaseTemplateId(String caseTemplateId, boolean remove) {
        try {
            Example<CaseTemplateApi> example = Example.of(
                CaseTemplateApi.builder().caseTemplateId(caseTemplateId).remove(remove).build());
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
    public CaseTemplateApiDto getSceneCaseApiById(String id) {
        try {
            Optional<CaseTemplateApi> sceneCaseApi = caseTemplateApiRepository.findById(id);
            return sceneCaseApi.map(aseTemplateApiMapper::toCaseTemplateApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplateApi by id!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_API_BY_ID_ERROR);
        }
    }

    private void publishBatchCaseTemplateApiEvent(List<CaseTemplateApi> caseApiList,
        OperationType operationType) {
        for (CaseTemplateApi sceneCaseTemplateApi : caseApiList) {
            publishCaseTemplateApiEvent(sceneCaseTemplateApi, operationType);
        }
    }

    private void publishCaseTemplateApiEvent(CaseTemplateApi sceneCaseApi, OperationType operationType) {
        SceneCaseApiLogDto sceneCaseApiLogDto = sceneCaseApiLogMapper
            .toDtoBySceneCaseTemplateApi(sceneCaseApi, operationType);
        SceneCaseApiLogEvent event = new SceneCaseApiLogEvent(this, sceneCaseApiLogDto);
        applicationEventPublisher.publishEvent(event);
    }

}
