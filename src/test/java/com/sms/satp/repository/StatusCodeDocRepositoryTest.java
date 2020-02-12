package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.PageDto;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataMongoTest
@DisplayName("Tests for StatusCodeDocRepository")
class StatusCodeDocRepositoryTest {

    @Autowired
    StatusCodeDocRepository statusCodeDocRepository;

    private static final Integer SIZE = 10;
    private static final String ID = "ID";
    private static final String CODE = "200";
    private static final String CODE_EDIT = "404";
    private static final String PROJECT_ID = "projectId";

    @BeforeEach
    void prepareDate() {
        for (int i = 0; i < SIZE; i++) {
            statusCodeDocRepository.insert(
                StatusCodeDoc.builder()
                    .projectId(PROJECT_ID)
                    .build());
        }
    }

    @AfterEach
    void deleteDate() {
        statusCodeDocRepository.deleteAll();
    }

    @Test
    @DisplayName("Test the insert method in the statusCodeDoc repository")
    void insert() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .id(ID)
            .code(CODE)
            .build();
        statusCodeDocRepository.insert(statusCodeDoc);
        Optional<StatusCodeDoc> apiInterfaceOptional = statusCodeDocRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get()).isEqualTo(statusCodeDoc);
    }

    @Test
    @DisplayName("Test the findAll method with Example in the statusCodeDoc repository")
    void findAll_By_Example() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<StatusCodeDoc> example = Example.of(statusCodeDoc);
        List<StatusCodeDoc> apiInterfaceList = statusCodeDocRepository.findAll(example);
        assertThat(apiInterfaceList).allMatch(apiInterface1 -> StringUtils
            .equals(apiInterface1.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the findAll method with Example and Pageable parameters in the statusCodeDoc repository")
    void findAll_By_Pageable_And_Example() {
        PageDto pageDto = PageDto.builder().build();
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<StatusCodeDoc> example = Example.of(statusCodeDoc);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        Page<StatusCodeDoc> apiInterfacePage = statusCodeDocRepository.findAll(example, pageable);
        assertThat(apiInterfacePage.getTotalElements()).isEqualTo(SIZE.longValue());
        assertThat(apiInterfacePage.getContent()).allMatch(item -> StringUtils.equals(item.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the save method with in the statusCodeDoc repository")
    void edit() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .id(ID)
            .code(CODE)
            .build();
        statusCodeDocRepository.insert(statusCodeDoc);
        statusCodeDoc.setCode(CODE_EDIT);
        statusCodeDocRepository.save(statusCodeDoc);
        Optional<StatusCodeDoc> apiInterfaceOptional = statusCodeDocRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get().getCode()).isEqualTo(CODE_EDIT);
    }

    @Test
    @DisplayName("Test the deleteById method in the statusCodeDoc repository")
    void delete_By_Id() {
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .id(ID)
            .code(CODE)
            .build();
        statusCodeDocRepository.insert(statusCodeDoc);
        statusCodeDocRepository.deleteById(ID);
        Optional<StatusCodeDoc> apiInterfaceOptional = statusCodeDocRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(false);
    }

}