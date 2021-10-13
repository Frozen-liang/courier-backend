package com.sms.courier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.initialize.ApiCaseCount;
import com.sms.courier.repository.impl.CustomizedApiTestCaseRepositoryImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

@DisplayName("Tests for CustomizedApiTestCaseRepository")
class CustomizedApiTestCaseRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository =
        new CustomizedApiTestCaseRepositoryImpl(mongoTemplate,
            commonRepository);
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    @Test
    @DisplayName("Test the updateApiTestCaseStatusByApiId method in the CustomizedApiTestCaseRepository")
    public void updateApiTestCaseStatusByApiId_test() {
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(null);
        customizedApiTestCaseRepository
            .updateApiTestCaseStatusByApiId(Collections.singletonList(ObjectId.get().toString()),
                ApiBindingStatus.BINDING);
        verify(mongoTemplate, times(1)).updateMulti(any(), any(), any(Class.class));
    }

    @Test
    @DisplayName("Test the deleteById method in the CustomizedApiTestCaseRepository")
    public void deleteById_test() {
        when(commonRepository.deleteById(ID, ApiTestCaseEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiTestCaseRepository.deleteById(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedApiTestCaseRepository")
    public void deleteByIds() {
        when(commonRepository.deleteByIds(ID_LIST, ApiTestCaseEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiTestCaseRepository.deleteByIds(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("Test the findApiIdsByTestIds method in the CustomizedApiTestCaseRepository")
    public void findApiIdsByTestIds() {
        List<ApiTestCaseEntity> entityList =
            Lists.list(ApiTestCaseEntity.builder().id(ID).apiEntity(ApiEntity.builder().id(ID).build()).build());
        when(commonRepository.findIncludeFieldByIds(any(), any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(entityList);
        List<String> dtoList = customizedApiTestCaseRepository.findApiIdsByTestIds(ID_LIST);
        assertThat(dtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the countByProjectIds method in the CustomizedApiTestCaseRepository")
    public void countByProjectIds() {
        when(mongoTemplate.count(any(), anyString())).thenReturn(1L);
        Long count = customizedApiTestCaseRepository.countByProjectIds(ID_LIST);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test the getCasePageByProjectIdsAndCreateDate method in the CustomizedApiTestCaseRepository")
    public void getCasePageByProjectIdsAndCreateDate() {
        Page<ApiTestCaseResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiTestCaseResponse.builder().build()));
        when(commonRepository.page(any(QueryVo.class), any(), eq(ApiTestCaseResponse.class))).thenReturn(page);
        Page<ApiTestCaseResponse> pageDto =
            customizedApiTestCaseRepository.getCasePageByProjectIdsAndCreateDate(ID_LIST, LocalDateTime.now(),
                new PageDto());
        assertThat(pageDto.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test the findProjectIdAndGroupByApiId method in the CustomizedApiTestCaseRepository")
    public void findProjectIdAndGroupByApiId() {
        List<ApiCaseCount> apiCaseCountList = Lists.newArrayList(ApiCaseCount.builder().build());
        AggregationResults<ApiCaseCount> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(apiCaseCountList);
        when(mongoTemplate.aggregate(any(), eq(ApiTestCaseEntity.class), eq(ApiCaseCount.class))).thenReturn(results);
        List<ApiCaseCount> result = customizedApiTestCaseRepository.findProjectIdAndGroupByApiId(ID, Boolean.FALSE);
        assertThat(result.size()).isEqualTo(1);
    }

}
