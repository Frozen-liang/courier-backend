package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.GENERATOR_TEMPLATE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.REMOVE;
import static com.sms.courier.common.exception.ErrorCode.GET_GENERATOR_TEMPLATE_ERROR;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.GeneratorTemplateRequest;
import com.sms.courier.dto.response.GeneratorTemplateResponse;
import com.sms.courier.dto.response.GeneratorTemplateTypeResponse;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.mapper.GeneratorTemplateMapper;
import com.sms.courier.repository.GeneratorTemplateRepository;
import com.sms.courier.repository.GeneratorTemplateTypeRepository;
import com.sms.courier.service.GeneratorTemplateService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeneratorTemplateServiceImpl implements GeneratorTemplateService {

    private final GeneratorTemplateRepository generatorTemplateRepository;
    private final GeneratorTemplateMapper generatorTemplateMapper;
    private final GeneratorTemplateTypeRepository generatorTemplateTypeRepository;

    public GeneratorTemplateServiceImpl(GeneratorTemplateRepository generatorTemplateRepository,
        GeneratorTemplateMapper generatorTemplateMapper,
        GeneratorTemplateTypeRepository generatorTemplateTypeRepository) {
        this.generatorTemplateRepository = generatorTemplateRepository;
        this.generatorTemplateMapper = generatorTemplateMapper;
        this.generatorTemplateTypeRepository = generatorTemplateTypeRepository;
    }

    @Override
    public GeneratorTemplateEntity findById(String id) {
        return generatorTemplateRepository.findById(id)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_GENERATOR_TEMPLATE_ERROR));
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = GENERATOR_TEMPLATE,
        template = "{{#generatorTemplateRequest.name}}")
    public Boolean add(GeneratorTemplateRequest generatorTemplateRequest) {
        log.info("GeneratorTemplateService-add()-params: [GeneratorTemplate]={}",
            generatorTemplateRequest.toString());
        try {
            GeneratorTemplateEntity generatorTemplateEntity = generatorTemplateMapper
                .toEntity(generatorTemplateRequest);
            generatorTemplateRepository.insert(generatorTemplateEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the GeneratorTemplate!");
            throw ExceptionUtils.mpe(ErrorCode.ADD_GENERATOR_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = GENERATOR_TEMPLATE, template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("GeneratorTemplateService-deleteById()-params: [id]={}", ids);
        try {
            generatorTemplateRepository.deleteAllByIdIsIn(ids);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the GeneratorTemplate!", e);
            throw ExceptionUtils.mpe(ErrorCode.DELETE_GENERATOR_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = GENERATOR_TEMPLATE, template =
        "{{#generatorTemplateRequest.name}}")
    public Boolean edit(GeneratorTemplateRequest generatorTemplateRequest) {
        log.info("GeneratorTemplateService-edit()-params: [GeneratorTemplate]={}",
            generatorTemplateRequest.toString());
        try {
            GeneratorTemplateEntity generatorTemplate = generatorTemplateMapper
                .toEntity(generatorTemplateRequest);
            generatorTemplateRepository.save(generatorTemplate);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the GeneratorTemplate!", e);
            throw ExceptionUtils.mpe(ErrorCode.EDIT_GENERATOR_TEMPLATE_ERROR);
        }
    }

    @Override
    public List<GeneratorTemplateResponse> list(String projectId) {
        try {
            List<GeneratorTemplateEntity> defaultTemplate =
                generatorTemplateRepository.findAllByDefaultTemplate(true);

            List<GeneratorTemplateEntity> templateEntityList =
                generatorTemplateRepository.findByProjectIdAndDefaultTemplateIsFalse(projectId);
            defaultTemplate.addAll(templateEntityList);
            return generatorTemplateMapper.toResponseList(defaultTemplate);
        } catch (Exception e) {
            log.error("Failed to get the GeneratorTemplate list!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_GENERATOR_TEMPLATE_LIST_ERROR);
        }
    }

    @Override
    public List<GeneratorTemplateTypeResponse> getAllType() {
        return generatorTemplateMapper.toTypeList(generatorTemplateTypeRepository.findAll());
    }

}
