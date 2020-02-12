package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.Project;
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
@DisplayName("Tests for ProjectRepository")
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    private static final Integer SIZE = 10;
    private static final String ID = "ID";
    private static final String NAME = "projectName";
    private static final String NAME_EDIT = "name";

    @BeforeEach
    void prepareDate() {
        for (int i = 0; i < SIZE; i++) {
            projectRepository.insert(
                Project.builder()
                    .name(NAME)
                    .build());
        }
    }

    @AfterEach
    void deleteDate() {
        projectRepository.deleteAll();
    }

    @Test
    @DisplayName("Test the insert method in the project repository")
    void insert() {
        Project project = Project.builder()
            .id(ID)
            .name(NAME)
            .build();
        projectRepository.insert(project);
        Optional<Project> apiInterfaceOptional = projectRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get()).isEqualTo(project);
    }

    @Test
    @DisplayName("Test the findAll method with Example in the project repository")
    void findAll_By_Example() {
        Project project = Project.builder()
            .name(NAME)
            .build();
        Example<Project> example = Example.of(project);
        List<Project> apiInterfaceList = projectRepository.findAll(example);
        assertThat(apiInterfaceList).allMatch(apiInterface1 -> StringUtils
            .equals(apiInterface1.getName(), NAME));
    }

    @Test
    @DisplayName("Test the findAll method with Example and Pageable parameters in the project repository")
    void findAll_By_Pageable_And_Example() {
        PageDto pageDto = PageDto.builder().build();
        Project project = Project.builder()
            .name(NAME)
            .build();
        Example<Project> example = Example.of(project);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        Page<Project> apiInterfacePage = projectRepository.findAll(example, pageable);
        assertThat(apiInterfacePage.getTotalElements()).isEqualTo(SIZE.longValue());
        assertThat(apiInterfacePage.getContent()).allMatch(item -> StringUtils.equals(item.getName(), NAME));
    }

    @Test
    @DisplayName("Test the save method with in the project repository")
    void edit() {
        Project project = Project.builder()
            .id(ID)
            .name(NAME)
            .build();
        projectRepository.insert(project);
        project.setName(NAME_EDIT);
        projectRepository.save(project);
        Optional<Project> apiInterfaceOptional = projectRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get().getName()).isEqualTo(NAME_EDIT);
    }

    @Test
    @DisplayName("Test the deleteById method in the project repository")
    void delete_By_Id() {
        Project project = Project.builder()
            .id(ID)
            .name(NAME)
            .build();
        projectRepository.insert(project);
        projectRepository.deleteById(ID);
        Optional<Project> apiInterfaceOptional = projectRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(false);
    }
}