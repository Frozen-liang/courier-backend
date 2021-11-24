package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.entity.mongo.GroupResultVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.repository.impl.CustomizedApiRepositoryImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

@DisplayName("Tests for CustomizedApiRepositoryTest")
class CustomizedApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final ApiTagRepository apiTagRepository = mock(ApiTagRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CustomizedApiRepository customizedApiRepository = new CustomizedApiRepositoryImpl(mongoTemplate,
        commonRepository, apiGroupRepository, apiTagRepository, userRepository);
    private static final String ID = ObjectId.get().toString();
    private static final Long MOCK_COUNT = 1L;
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    @Test
    @DisplayName("Test the findById method in the CustomizedApiRepository")
    public void findById_test() {
        when(commonRepository.findById(anyString(), anyString(), any(List.class), any())).thenReturn(Optional.of(
            ApiResponse.builder().build()));
        assertThat(customizedApiRepository.findById(ID).isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test the page method in the CustomizedApiRepository")
    public void page_test() {
        when(commonRepository.page(any(QueryVo.class), any(), any()))
            .thenReturn(new PageImpl<>(List.of(ApiPageResponse.builder().id(ID).build())));
        when(apiTagRepository.findAllByIdIn(any())).thenReturn(Stream.empty());
        when(userRepository.findByIdIn(any())).thenReturn(Collections.emptyList());
        when(apiGroupRepository.findById(any()))
            .thenReturn(Optional.of(ApiGroupEntity.builder().realGroupId(1L).build()));
        when(apiGroupRepository.findAllByPathContains(any()))
            .thenReturn(Stream.of(ApiGroupEntity.builder().id(ID).build()));
        AggregationResults aggregationResults = mock(AggregationResults.class);
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any())).thenReturn(aggregationResults);
        when(aggregationResults.getMappedResults())
            .thenReturn(List.of(GroupResultVo.builder().id(ID).count(1).build()));
        ApiPageRequest apiPageRequest = new ApiPageRequest();
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setApiStatus(Arrays.asList(1, 2));
        apiPageRequest.setProjectId(new ObjectId());
        apiPageRequest.setGroupId(new ObjectId());
        apiPageRequest.setRequestMethod(Arrays.asList(1, 2));
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setTagId(Arrays.asList(new ObjectId(), new ObjectId()));
        Page<ApiPageResponse> page = customizedApiRepository.page(apiPageRequest);
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Test the deleteById method in the CustomizedApiRepository")
    public void deleteById_test() {
        when(commonRepository.deleteById(ID, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.deleteById(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedApiRepository")
    public void deleteByIds_test() {
        when(commonRepository.deleteByIds(ID_LIST, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.deleteByIds(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("Test the recover method in the CustomizedApiRepository")
    public void recover_test() {
        when(commonRepository.recover(ID_LIST, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.recover(ID_LIST)).isTrue();
    }


    @Test
    @DisplayName("Test the deleteByGroupIds method in the CustomizedApiRepository")
    public void deleteByGroupIds_test() {
        when(commonRepository.deleteByIds(ID_LIST, ApiEntity.class)).thenReturn(Boolean.TRUE);
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(null);
        customizedApiRepository.deleteByGroupIds(ID_LIST);
        verify(mongoTemplate, times(1)).updateMulti(any(), any(), any(Class.class));
    }

    @Test
    @DisplayName("Test for batchUpdateByIds in CustomizedApiRepository")
    public void batchUpdateByIds_test() {
        when(commonRepository.updateFieldByIds(any(), any(UpdateRequest.class), any())).thenReturn(true);
        Boolean result = customizedApiRepository.updateFieldByIds(List.of(ID), new UpdateRequest<>());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test for sceneCount in CustomizedApiRepository")
    public void sceneCount_test() {
        when(mongoTemplate.count(any(), anyString())).thenReturn(1L);
        Long count = customizedApiRepository.sceneCount(new ObjectId());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test for caseCount in CustomizedApiRepository")
    public void caseCount_test() {
        when(mongoTemplate.count(any(), anyString())).thenReturn(1L);
        Long count = customizedApiRepository.caseCount(new ObjectId());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test for sceneCountPage in CustomizedApiRepository")
    public void sceneCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(commonRepository.page(any(QueryVo.class),any(),eq(ApiPageResponse.class))).thenReturn(page);
        Page<ApiPageResponse> dtoPage = customizedApiRepository.sceneCountPage(ApiIncludeCaseRequest.builder().build());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for caseCountPage in CustomizedApiRepository")
    public void caseCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(commonRepository.page(any(QueryVo.class),any(),eq(ApiPageResponse.class))).thenReturn(page);
        Page<ApiPageResponse> dtoPage = customizedApiRepository.caseCountPage(ApiIncludeCaseRequest.builder().build());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for count in CustomizedApiRepository")
    public void count_test() {
        when(mongoTemplate.count(any(),eq(ApiEntity.class))).thenReturn(MOCK_COUNT);
        Long dto = customizedApiRepository.count(Lists.newArrayList(ID));
        assertThat(dto).isEqualTo(MOCK_COUNT);
    }

}
