package com.sms.courier.service;

import com.google.common.collect.Lists;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.AddGeneratorTemplateRequest;
import com.sms.courier.dto.request.UpdateGeneratorTemplateRequest;
import com.sms.courier.entity.generator.CodeTemplate;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.enums.CodeType;
import com.sms.courier.mapper.GeneratorTemplateMapper;
import com.sms.courier.repository.GeneratorTemplateRepository;
import com.sms.courier.service.impl.GeneratorTemplateServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for GeneratorTemplateTest")
public class GeneratorTemplateServiceTest {

    private final GeneratorTemplateRepository generatorTemplateRepository = mock(GeneratorTemplateRepository.class);
    private final GeneratorTemplateMapper generatorTemplateMapper = mock(GeneratorTemplateMapper.class);
    private final GeneratorTemplateService generatorTemplateService =
            new GeneratorTemplateServiceImpl(generatorTemplateRepository, generatorTemplateMapper);

    private static final String MOCK_ID = ObjectId.get().toString();
    private static final String MOCK_NAME = "test";
    private static final CodeType MOCK_CODE_TYPE = CodeType.getCodeType(1);
    private static final Boolean Boolean = java.lang.Boolean.TRUE;

    @Test
    @DisplayName("Test the add method in the GeneratorTemplate service")
    void add_test() {
        GeneratorTemplateEntity generatorTemplate = GeneratorTemplateEntity.builder()
                .name(MOCK_NAME).codeType(MOCK_CODE_TYPE).codeTemplates(List.of(CodeTemplate.builder().name(MOCK_NAME).build())).build();
        when(generatorTemplateMapper.toGeneratorTemplateByAddRequest(any())).thenReturn(generatorTemplate);
        when(generatorTemplateRepository.insert(any(GeneratorTemplateEntity.class))).thenReturn(generatorTemplate);
        assertTrue(generatorTemplateService.add(AddGeneratorTemplateRequest.builder().build()));
    }

    @Test
    @DisplayName("Test the add method in the GeneratorTemplate service throws exception")
    void add_test_thenThrow_Exception() {
        GeneratorTemplateEntity generatorTemplate = GeneratorTemplateEntity.builder()
                .name(MOCK_NAME).codeType(MOCK_CODE_TYPE).codeTemplates(List.of(CodeTemplate.builder().name(MOCK_NAME).build())).build();
        when(generatorTemplateMapper.toGeneratorTemplateByAddRequest(any())).thenReturn(generatorTemplate);
        when(generatorTemplateRepository.insert(any(GeneratorTemplateEntity.class)))
                .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> generatorTemplateService.add(AddGeneratorTemplateRequest.builder().build()))
                .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the add method in the GeneratorTemplate service throws exception")
    void add_test_thenThrowException() {
        GeneratorTemplateEntity generatorTemplate = GeneratorTemplateEntity.builder()
                .name(MOCK_NAME).codeType(MOCK_CODE_TYPE).codeTemplates(List.of(CodeTemplate.builder().name(MOCK_NAME).build())).build();
        when(generatorTemplateMapper.toGeneratorTemplateByAddRequest(any())).thenReturn(generatorTemplate);
        when(generatorTemplateRepository.insert(any(GeneratorTemplateEntity.class)))
                .thenThrow(new ApiTestPlatformException(ErrorCode.ADD_GENERATOR_TEMPLATE_ERROR));
        assertThatThrownBy(() -> generatorTemplateService.add(AddGeneratorTemplateRequest.builder().build()))
                .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the findById method in the GeneratorTemplate service")
    void findById() {
        when(generatorTemplateRepository.findById(MOCK_ID)).thenReturn(Optional.of(GeneratorTemplateEntity.builder().id(MOCK_ID).build()));
        GeneratorTemplateEntity serviceById = generatorTemplateService.findById(MOCK_ID);
        assertThat(serviceById.getId()).isEqualTo(MOCK_ID);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the GeneratorTemplate service")
    void deleteByIds() {
        when(generatorTemplateRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        Boolean result1 = generatorTemplateService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(result1);
    }

    @Test
    @DisplayName("Test the delete method in the GeneratorTemplate service throws exception")
    void delete_test_thenThrow_Exception() {
        doThrow(new ApiTestPlatformException(ErrorCode.DELETE_GENERATOR_TEMPLATE_ERROR)).when(generatorTemplateRepository)
                .deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> generatorTemplateService.deleteByIds(List.of(MOCK_ID)))
                .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the delete method in the GeneratorTemplate service throws exception")
    void delete_test_thenThrowException() {
        doThrow(new RuntimeException()).when(generatorTemplateRepository)
                .deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> generatorTemplateService.deleteByIds(List.of(MOCK_ID)))
                .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the GeneratorTemplate service")
    void edit() {
        GeneratorTemplateEntity generatorTemplate =
                GeneratorTemplateEntity.builder()
                        .id(MOCK_ID).createUserId(MOCK_ID).modifyUserId(MOCK_ID).name(MOCK_NAME).removed(Boolean).build();
        when(generatorTemplateMapper.toGeneratorTemplateByEditRequest(any())).thenReturn(generatorTemplate);
        when(generatorTemplateRepository.save(any())).thenReturn(generatorTemplate);
        Boolean result2 = generatorTemplateService.edit(UpdateGeneratorTemplateRequest.builder().build());
        assertTrue(result2);
    }

    @Test
    @DisplayName("Test the edit method in the GeneratorTemplate service throws exception")
    void edit_test_then_thrownException() {
        GeneratorTemplateEntity generatorTemplate =
                GeneratorTemplateEntity.builder()
                        .id(MOCK_ID).createUserId(MOCK_ID).modifyUserId(MOCK_ID).name(MOCK_NAME).removed(Boolean).build();
        when(generatorTemplateMapper.toGeneratorTemplateByEditRequest(any())).thenReturn(generatorTemplate);
        when(generatorTemplateRepository.save(any()))
                .thenReturn(new ApiTestPlatformException(ErrorCode.DELETE_GENERATOR_TEMPLATE_ERROR));
        assertThatThrownBy(() -> generatorTemplateService.edit(UpdateGeneratorTemplateRequest.builder().build()))
                .isInstanceOf(ApiTestPlatformException.class);
    }
}
