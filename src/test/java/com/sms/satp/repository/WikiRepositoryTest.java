package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.Wiki;
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
@DisplayName("Tests for WikiRepository")
class WikiRepositoryTest {

    @Autowired
    WikiRepository wikiRepository;

    private static final Integer SIZE = 10;
    private static final String ID = "ID";
    private static final String TITLE = "title";
    private static final String TITLE_EDIT = "title2";
    private static final String PROJECT_ID = "projectId";

    @BeforeEach
    void prepareDate() {
        for (int i = 0; i < SIZE; i++) {
            wikiRepository.insert(
                Wiki.builder()
                    .projectId(PROJECT_ID)
                    .build());
        }
    }

    @AfterEach
    void deleteDate() {
        wikiRepository.deleteAll();
    }

    @Test
    @DisplayName("Test the insert method in the wiki repository")
    void insert() {
        Wiki wiki = Wiki.builder()
            .id(ID)
            .title(TITLE)
            .build();
        wikiRepository.insert(wiki);
        Optional<Wiki> apiInterfaceOptional = wikiRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get()).isEqualTo(wiki);
    }

    @Test
    @DisplayName("Test the findAll method with Example in the wiki repository")
    void findAll_By_Example() {
        Wiki wiki = Wiki.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<Wiki> example = Example.of(wiki);
        List<Wiki> apiInterfaceList = wikiRepository.findAll(example);
        assertThat(apiInterfaceList).allMatch(apiInterface1 -> StringUtils
            .equals(apiInterface1.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the findAll method with Example and Pageable parameters in the wiki repository")
    void findAll_By_Pageable_And_Example() {
        PageDto pageDto = PageDto.builder().build();
        Wiki wiki = Wiki.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<Wiki> example = Example.of(wiki);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        Page<Wiki> apiInterfacePage = wikiRepository.findAll(example, pageable);
        assertThat(apiInterfacePage.getTotalElements()).isEqualTo(SIZE.longValue());
        assertThat(apiInterfacePage.getContent()).allMatch(item -> StringUtils.equals(item.getProjectId(), PROJECT_ID));
    }

    @Test
    @DisplayName("Test the save method with in the wiki repository")
    void edit() {
        Wiki wiki = Wiki.builder()
            .id(ID)
            .title(TITLE)
            .build();
        wikiRepository.insert(wiki);
        wiki.setTitle(TITLE_EDIT);
        wikiRepository.save(wiki);
        Optional<Wiki> apiInterfaceOptional = wikiRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(true);
        assertThat(apiInterfaceOptional.get().getTitle()).isEqualTo(TITLE_EDIT);
    }

    @Test
    @DisplayName("Test the deleteById method in the wiki repository")
    void delete_By_Id() {
        Wiki wiki = Wiki.builder()
            .id(ID)
            .title(TITLE)
            .build();
        wikiRepository.insert(wiki);
        wikiRepository.deleteById(ID);
        Optional<Wiki> apiInterfaceOptional = wikiRepository.findById(ID);
        assertThat(apiInterfaceOptional.isPresent()).isEqualTo(false);
    }
}