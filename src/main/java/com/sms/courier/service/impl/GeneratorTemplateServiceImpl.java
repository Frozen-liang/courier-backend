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
import com.sms.courier.dto.request.AddGeneratorTemplateRequest;
import com.sms.courier.dto.request.UpdateGeneratorTemplateRequest;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.mapper.GeneratorTemplateMapper;
import com.sms.courier.repository.GeneratorTemplateRepository;
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

    public GeneratorTemplateServiceImpl(GeneratorTemplateRepository generatorTemplateRepository,
                                        GeneratorTemplateMapper generatorTemplateMapper) {
        this.generatorTemplateRepository = generatorTemplateRepository;
        this.generatorTemplateMapper = generatorTemplateMapper;
    }

    @Override
    public GeneratorTemplateEntity findById(String id) {
        return generatorTemplateRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(GET_GENERATOR_TEMPLATE_ERROR));
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = GENERATOR_TEMPLATE,
            template = "{{#addGeneratorTemplateRequest.name}}")
    public Boolean add(AddGeneratorTemplateRequest addGeneratorTemplateRequest) {
        log.info("GeneratorTemplateService-add()-params: [GeneratorTemplate]={}",
                addGeneratorTemplateRequest.toString());
        try {
            // scene为什么要加
            GeneratorTemplateEntity sceneGeneratorTemplate = generatorTemplateMapper
                    .toGeneratorTemplateByAddRequest(addGeneratorTemplateRequest);
            generatorTemplateRepository.insert(sceneGeneratorTemplate);
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
            "{{#updateGeneratorTemplateRequest.name}}")
    public Boolean edit(UpdateGeneratorTemplateRequest updateGeneratorTemplateRequest) {
        log.info("GeneratorTemplateService-edit()-params: [GeneratorTemplate]={}",
                updateGeneratorTemplateRequest.toString());
        try {
            GeneratorTemplateEntity generatorTemplate = generatorTemplateMapper
                    .toGeneratorTemplateByEditRequest(updateGeneratorTemplateRequest);
            generatorTemplateRepository.save(generatorTemplate);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the GeneratorTemplate!", e);
            throw ExceptionUtils.mpe(ErrorCode.DELETE_GENERATOR_TEMPLATE_ERROR);
        }
    }
}
