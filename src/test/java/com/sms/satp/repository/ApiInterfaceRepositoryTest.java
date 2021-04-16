package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.ApiInterface;
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
@DisplayName("Tests for ApiInterfaceRepository")
class ApiInterfaceRepositoryTest {

    @Autowired
    ApiInterfaceRepository apiInterfaceRepository;

    private static final Integer SIZE = 10;
    private static final String ID = "ID";
    private static final String TITLE = "title";
    private static final String TITLE_EDIT = "title2";
    private static final String PROJECT_ID = "projectId";

    @BeforeEach
    void prepareDate() {
        for (int i = 0; i < SIZE; i++) {
            apiInterfaceRepository.insert(
                ApiInterface.builder()
                    .projectId(PROJECT_ID)
                    .build());
        }
    }

    @AfterEach
    void deleteDate() {
        apiInterfaceRepository.deleteAll();
    }

    @Test
    @DisplayName("Test the insert method in the apiInterface repository")
    void insert() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .build();
        ApiInterface save = apiInterfaceRepository.save(apiInterface);
        Optional<ApiInterface> apiInterfaceOptional = apiInterfaceRepository.findById(save.getId());
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get()).isEqualTo(apiInterface);
    }

    @Test
    @DisplayName("Test the findAll method with Example in the apiInterface repository")
    void findAll_By_Example() {
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        List<ApiInterface> apiInterfaceList = apiInterfaceRepository.findAll(example);
        assertThat(apiInterfaceList).allMatch(apiInterface1 -> StringUtils.equals(apiInterface1.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the findAll method with Example and Pageable parameters in the apiInterface repository")
    void findAll_By_Pageable_And_Example() {
        PageDto pageDto = PageDto.builder().build();
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        Page<ApiInterface> apiInterfacePage = apiInterfaceRepository.findAll(example, pageable);
        assertThat(apiInterfacePage.getTotalElements()).isEqualTo(SIZE.longValue());
        assertThat(apiInterfacePage.getContent()).allMatch(item -> StringUtils.equals(item.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the save method with in the apiInterface repository")
    void edit() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .build();
        ApiInterface insert = apiInterfaceRepository.insert(apiInterface);
        apiInterface.setTitle(TITLE_EDIT);
        apiInterfaceRepository.save(apiInterface);
        Optional<ApiInterface> apiInterfaceOptional = apiInterfaceRepository.findById(insert.getId());
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get().getTitle()).isEqualTo(TITLE_EDIT);
    }

    @Test
    @DisplayName("Test the deleteById method in the apiInterface repository")
    void delete_By_Id() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .build();
        ApiInterface insert = apiInterfaceRepository.insert(apiInterface);
        apiInterfaceRepository.deleteById(insert.getId());
        Optional<ApiInterface> apiInterfaceOptional = apiInterfaceRepository.findById(insert.getId());
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(false);
    }
}