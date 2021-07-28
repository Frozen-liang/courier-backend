package com.sms.satp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.entity.mongo.QueryVo;
import com.sms.satp.repository.impl.CustomizedApiRepositoryImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for CustomizedApiRepositoryTest")
class CustomizedApiRepositoryTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final CustomizedApiRepository customizedApiRepository = new CustomizedApiRepositoryImpl(mongoTemplate,
        commonRepository, apiGroupRepository);
    private static final String ID = ObjectId.get().toString();
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
            .thenReturn(new PageImpl<>(List.of(ApiResponse.builder().build())));
        when(apiGroupRepository.findById(any()))
            .thenReturn(Optional.of(ApiGroupEntity.builder().realGroupId(1L).build()));
        when(apiGroupRepository.findAllByPathContains(any()))
            .thenReturn(Stream.of(ApiGroupEntity.builder().id(ID).build()));
        ApiPageRequest apiPageRequest = new ApiPageRequest();
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setApiStatus(Arrays.asList(1, 2));
        apiPageRequest.setProjectId(new ObjectId());
        apiPageRequest.setGroupId(new ObjectId());
        apiPageRequest.setRequestMethod(Arrays.asList(1, 2));
        apiPageRequest.setApiProtocol(Arrays.asList(1, 2));
        apiPageRequest.setTagId(Arrays.asList(new ObjectId(), new ObjectId()));
        Page<ApiResponse> page = customizedApiRepository.page(apiPageRequest);
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
}
