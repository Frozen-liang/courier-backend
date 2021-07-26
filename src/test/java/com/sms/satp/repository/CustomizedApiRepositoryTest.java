package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.impl.CustomizedApiRepositoryImpl;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for CustomizedApiRepositoryTest")
class CustomizedApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final CustomizedApiRepository customizedApiRepository = new CustomizedApiRepositoryImpl(mongoTemplate,
        commonRepository, apiGroupRepository);
    private static final Long TOTAL_ELEMENTS = 20L;
    private static final String ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);

    /*@Test
    @DisplayName("Test the findById method in the CustomizedApiRepository")
    public void findById_test() {
        AggregationResults aggregationResults = mock(AggregationResults.class);
        when(mongoTemplate.aggregate(any(), any(Class.class), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getMappedResults()).thenReturn(List.of(ApiResponse.builder().build()));
        Optional<ApiResponse> optional = customizedApiRepository.findById(ID);
        assertThat(optional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test the page method in the CustomizedApiRepository")
    public void page_test() {
        ArrayList<ApiResponse> apiDtoList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiDtoList.add(new ApiResponse());
        }
        ApiPageRequest apiPageRequest = new ApiPageRequest();
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setApiStatus(Arrays.asList(1, 2));
        apiPageRequest.setProjectId(new ObjectId());
        apiPageRequest.setGroupId(new ObjectId());
        apiPageRequest.setRequestMethod(Arrays.asList(1, 2));
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setTagId(Arrays.asList(new ObjectId(), new ObjectId()));
        when(apiGroupRepository.findById(any()))
            .thenReturn(Optional.of(ApiGroupEntity.builder().realGroupId(1L).build()));
        when(apiGroupRepository.findAllByPathContains(any()))
            .thenReturn(Stream.of(ApiGroupEntity.builder().id(ID).build()));
        when(mongoTemplate.count(any(Query.class), any(Class.class))).thenReturn(TOTAL_ELEMENTS);
        AggregationResults aggregationResults = mock(AggregationResults.class);
        when(mongoTemplate.aggregate(any(), any(Class.class), any(Class.class)))
            .thenReturn(aggregationResults);
        when(aggregationResults.getMappedResults()).thenReturn(apiDtoList);
        Page<ApiResponse> page = customizedApiRepository.page(apiPageRequest);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
    }*/

    @Test
    @DisplayName("Test the deleteById method in the CustomizedApiRepository")
    public void deleteById_test() {
        when(commonRepository.deleteById(ID, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.deleteById(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CustomizedApiRepository")
    public void deleteByIds() {
        when(commonRepository.deleteByIds(ID_LIST, ApiEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(customizedApiRepository.deleteByIds(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteByGroupIds method in the CustomizedApiRepository")
    public void deleteByGroupIds() {
        when(commonRepository.deleteByIds(ID_LIST, ApiEntity.class)).thenReturn(Boolean.TRUE);
        when(mongoTemplate.updateMulti(any(), any(), any(Class.class))).thenReturn(null);
        customizedApiRepository.deleteByGroupIds(ID_LIST);
        verify(mongoTemplate, times(1)).updateMulti(any(), any(), any(Class.class));
    }
}
