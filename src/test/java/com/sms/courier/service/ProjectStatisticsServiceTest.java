package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.service.impl.ProjectStatisticsServiceImpl;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for ProjectStatisticsService")
public class ProjectStatisticsServiceTest {

    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final ProjectStatisticsService projectStatisticsService =
        new ProjectStatisticsServiceImpl(customizedApiRepository);

    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test for sceneCountPage in ApiService")
    public void sceneCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(customizedApiRepository.sceneCountPage(any())).thenReturn(page);
        Page<ApiPageResponse> dtoPage = projectStatisticsService
            .sceneCountPage(ApiIncludeCaseRequest.builder().build());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for sceneCountPage in ApiService")
    public void sceneCountPage_exception_test() {
        when(customizedApiRepository.sceneCountPage(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.sceneCountPage(ApiIncludeCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for caseCountPage in ApiService")
    public void caseCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(customizedApiRepository.caseCountPage(any())).thenReturn(page);
        Page<ApiPageResponse> dtoPage = projectStatisticsService.caseCountPage(ApiIncludeCaseRequest.builder().build());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for caseCountPage in ApiService")
    public void caseCountPage_exception_test() {
        when(customizedApiRepository.caseCountPage(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.caseCountPage(ApiIncludeCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
