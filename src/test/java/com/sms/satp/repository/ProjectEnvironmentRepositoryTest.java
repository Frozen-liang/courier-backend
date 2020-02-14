package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.ProjectEnvironment;
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
@DisplayName("Tests for ProjectEnvironmentRepository")
class ProjectEnvironmentRepositoryTest {

    @Autowired
    ProjectEnvironmentRepository projectEnvironmentRepository;

    private static final Integer SIZE = 10;
    private static final String ID = "ID";
    private static final String NAME = "title";
    private static final String NAME_EDIT = "title2";
    private static final String PROJECT_ID = "projectId";

    @BeforeEach
    void prepareDate() {
        for (int i = 0; i < SIZE; i++) {
            projectEnvironmentRepository.insert(
                ProjectEnvironment.builder()
                    .projectId(PROJECT_ID)
                    .build());
        }
    }

    @AfterEach
    void deleteDate() {
        projectEnvironmentRepository.deleteAll();
    }

    @Test
    @DisplayName("Test the insert method in the projectEnvironment repository")
    void insert() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .id(ID)
            .name(NAME)
            .build();
        projectEnvironmentRepository.insert(projectEnvironment);
        Optional<ProjectEnvironment> apiInterfaceOptional = projectEnvironmentRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get()).isEqualTo(projectEnvironment);
    }

    @Test
    @DisplayName("Test the findAll method with Example in the projectEnvironment repository")
    void findAll_By_Example() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ProjectEnvironment> example = Example.of(projectEnvironment);
        List<ProjectEnvironment> apiInterfaceList = projectEnvironmentRepository.findAll(example);
        assertThat(apiInterfaceList).allMatch(apiInterface1 -> StringUtils
            .equals(apiInterface1.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the findAll method with Example and Pageable parameters in the projectEnvironment repository")
    void findAll_By_Pageable_And_Example() {
        PageDto pageDto = PageDto.builder().build();
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ProjectEnvironment> example = Example.of(projectEnvironment);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        Page<ProjectEnvironment> apiInterfacePage = projectEnvironmentRepository.findAll(example, pageable);
        assertThat(apiInterfacePage.getTotalElements()).isEqualTo(SIZE.longValue());
        assertThat(apiInterfacePage.getContent()).allMatch(item -> StringUtils.equals(item.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the save method with in the projectEnvironment repository")
    void edit() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .id(ID)
            .name(NAME)
            .build();
        projectEnvironmentRepository.insert(projectEnvironment);
        projectEnvironment.setName(NAME_EDIT);
        projectEnvironmentRepository.save(projectEnvironment);
        Optional<ProjectEnvironment> apiInterfaceOptional = projectEnvironmentRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get().getName()).isEqualTo(NAME_EDIT);
    }

    @Test
    @DisplayName("Test the deleteById method in the projectEnvironment repository")
    void delete_By_Id() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .id(ID)
            .name(NAME)
            .build();
        projectEnvironmentRepository.insert(projectEnvironment);
        projectEnvironmentRepository.deleteById(ID);
        Optional<ProjectEnvironment> apiInterfaceOptional = projectEnvironmentRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(false);
    }

}