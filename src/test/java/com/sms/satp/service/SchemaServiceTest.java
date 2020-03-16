package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_SCHEMA_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCHEMA_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCHEMA_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCHEMA_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCHEMA_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.Schema;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SchemaDto;
import com.sms.satp.mapper.SchemaMapper;
import com.sms.satp.repository.SchemaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@SpringBootTest(classes = ApplicationTests.class)
@DisplayName("Test cases for SchemaService")
class SchemaServiceTest {

    @MockBean
    private SchemaRepository schemaRepository;

    @SpyBean
    private SchemaService schemaService;

    @SpyBean
    private SchemaMapper schemaMapper;

    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 1;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static String ID = "25";
    private final static String NOT_EXIST_ID = "30";
    private final static String PROJECT_ID = "25";
    private final static String NAME = "title";

    @Test
    @DisplayName("Test the paging method with no parameters in the Schema service")
    void page_default_test() {
        Schema schema = Schema.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<Schema> example = Example.of(schema);
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<Schema> schemaList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            schemaList.add(Schema.builder().name(NAME).build());
        }
        Page<Schema> schemaPage = new PageImpl<>(schemaList, pageable, TOTAL_ELEMENTS);
        when(schemaRepository.findAll(example, pageable)).thenReturn(schemaPage);
        Page<SchemaDto> schemaDtoPage = schemaService.page(pageDto, PROJECT_ID);
        assertThat(schemaDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(schemaDtoPage.getPageable().getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(schemaDtoPage.getPageable().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(schemaDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getName(), NAME));
    }

    @Test
    @DisplayName("Test the paging method with specified parameters in the Schema service")
    void page_test() {
        Schema schema = Schema.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<Schema> example = Example.of(schema);
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<Schema> schemaList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            schemaList.add(Schema.builder().name(NAME).build());
        }
        Page<Schema> schemaPage = new PageImpl<>(schemaList, pageable, TOTAL_ELEMENTS);
        when(schemaRepository.findAll(example, pageable)).thenReturn(schemaPage);
        Page<SchemaDto> schemaDtoPage = schemaService.page(pageDto, PROJECT_ID);
        assertThat(schemaDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(schemaDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER);
        assertThat(schemaDtoPage.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(schemaDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getName(), NAME));
    }

    @Test
    @DisplayName("Test the add method in the Schema service")
    void add_test() {
        SchemaDto schemaDto = SchemaDto.builder().build();
        Schema schema = schemaMapper.toEntity(schemaDto);
        when(schemaRepository.insert(schema)).thenReturn(schema);
        schemaService.add(schemaDto);
        verify(schemaRepository, times(1)).insert(any(Schema.class));
    }

    @Test
    @DisplayName("Test the edit method in the Schema service")
    void edit_test() {
        SchemaDto schemaDto = SchemaDto.builder().id(ID).build();
        Schema schema = schemaMapper.toEntity(schemaDto);
        when(schemaRepository.findById(ID)).thenReturn(Optional.of(Schema.builder().build()));
        when(schemaRepository.save(schema)).thenReturn(schema);
        schemaService.edit(schemaDto);
        verify(schemaRepository, times(1)).save(any(Schema.class));
    }

    @Test
    @DisplayName("Test the method of querying the Schema by id")
    void findProjectEnvironmentById() {
        Schema schema = Schema.builder()
            .name(NAME)
            .build();
        Optional<Schema> schemaOptional = Optional.ofNullable(schema);
        when(schemaRepository.findById(ID)).thenReturn(schemaOptional);
        SchemaDto result1 = schemaService.findById(ID);
        SchemaDto result2 = schemaService.findById(NOT_EXIST_ID);
        assertThat(result1.getName()).isEqualTo(NAME);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("Test the delete method in the Schema service")
    void delete_test() {
        doNothing().when(schemaRepository).deleteById(PROJECT_ID);
        schemaService.deleteById(PROJECT_ID);
        verify(schemaRepository, times(1)).deleteById(PROJECT_ID);
    }

    @Test
    @DisplayName("An exception occurred while getting schema page")
    void page_exception_test() {
        Schema schema = Schema.builder().projectId(PROJECT_ID).build();
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(
            pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        doThrow(new RuntimeException()).when(schemaRepository).findAll(Example.of(schema), pageable);
        assertThatThrownBy(() -> schemaService.page(pageDto, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEMA_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding schema")
    void add_exception_test() {
        doThrow(new RuntimeException()).when(schemaRepository).insert(any(Schema.class));
        assertThatThrownBy(() -> schemaService.add(SchemaDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_SCHEMA_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit schema")
    void edit_exception_test() {
        when(schemaRepository.findById(ID)).thenReturn(Optional.of(Schema.builder().build()));
        doThrow(new RuntimeException()).when(schemaRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> schemaService.edit(SchemaDto.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_SCHEMA_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting schema by id")
    void getProjectEnvironment_exception_test() {
        doThrow(new RuntimeException()).when(schemaRepository).findById(anyString());
        assertThatThrownBy(() -> schemaService.findById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEMA_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete schema")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(schemaRepository).deleteById(anyString());
        assertThatThrownBy(() -> schemaService.deleteById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_SCHEMA_BY_ID_ERROR.getCode());
    }
}