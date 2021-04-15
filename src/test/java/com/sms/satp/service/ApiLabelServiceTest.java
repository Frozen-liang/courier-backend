package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_API_LABEL_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_LABEL_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_LABEL_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_LABEL_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_LABEL_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.ApiLabelDto;
import com.sms.satp.entity.ApiLabel;
import com.sms.satp.mapper.ApiLabelMapper;
import com.sms.satp.repository.ApiLabelRepository;
import com.sms.satp.service.impl.ApiLabelServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ApiLabelService")
class ApiLabelServiceTest {

    private final ApiLabelRepository apiLabelRepository = mock(ApiLabelRepository.class);
    private final ApiLabelMapper apiLabelMapper = mock(ApiLabelMapper.class);
    private final ApiLabelService apiLabelService = new ApiLabelServiceImpl(
        apiLabelRepository,
        apiLabelMapper);
    private final ApiLabel apiLabel = ApiLabel.builder().id(ID).build();
    private final ApiLabelDto apiLabelDto = ApiLabelDto.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String NOT_EXIST_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = "10";
    private static final String LABEL_NAME = "testName";
    private static final Short LABEL_TYPE = 1;

    @Test
    @DisplayName("Test the findById method in the ApiLabel service")
    public void findById_test() {
        when(apiLabelRepository.findById(ID)).thenReturn(Optional.of(apiLabel));
        when(apiLabelMapper.toDto(apiLabel)).thenReturn(apiLabelDto);
        ApiLabelDto result1 = apiLabelService.findById(ID);
        ApiLabelDto result2 = apiLabelService.findById(NOT_EXIST_ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("An exception occurred while getting ApiLabel")
    public void findById_exception_test() {
        doThrow(new RuntimeException()).when(apiLabelRepository).findById(ID);
        assertThatThrownBy(() -> apiLabelService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_LABEL_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiLabel service")
    public void add_test() {
        when(apiLabelMapper.toEntity(apiLabelDto)).thenReturn(apiLabel);
        when(apiLabelRepository.insert(any(ApiLabel.class))).thenReturn(apiLabel);
        apiLabelService.add(apiLabelDto);
        verify(apiLabelRepository, times(1)).insert(any(ApiLabel.class));
    }

    @Test
    @DisplayName("An exception occurred while adding ApiLabel")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(apiLabelRepository).insert(any(ApiLabel.class));
        assertThatThrownBy(() -> apiLabelService.add(apiLabelDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_LABEL_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiLabel service")
    public void edit_test() {
        when(apiLabelMapper.toEntity(apiLabelDto)).thenReturn(apiLabel);
        when(apiLabelRepository.findById(any()))
            .thenReturn(Optional.of(ApiLabel.builder().id(ID).build()));
        when(apiLabelRepository.save(any(ApiLabel.class))).thenReturn(apiLabel);
        apiLabelService.edit(apiLabelDto);
        verify(apiLabelRepository, times(1)).save(any(ApiLabel.class));
    }

    @Test
    @DisplayName("An exception occurred while edit ApiLabel")
    public void edit_exception_test() {
        when(apiLabelMapper.toEntity(apiLabelDto)).thenReturn(apiLabel);
        when(apiLabelRepository.findById(any())).thenReturn(Optional.of(apiLabel));
        doThrow(new RuntimeException()).when(apiLabelRepository).save(any(ApiLabel.class));
        assertThatThrownBy(() -> apiLabelService.edit(apiLabelDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_LABEL_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiLabel service")
    public void list_test() {
        ArrayList<ApiLabel> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ApiLabel.builder().build());
        }
        when(apiLabelRepository.findAll(any(),any(Sort.class))).thenReturn(list);
        when(apiLabelMapper.toDto(apiLabel)).thenReturn(apiLabelDto);
        List<ApiLabelDto> result = apiLabelService.list(PROJECT_ID, LABEL_NAME, LABEL_TYPE);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiLabel list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(apiLabelRepository).findAll(any(),any(Sort.class));
        assertThatThrownBy(() -> apiLabelService.list(PROJECT_ID, LABEL_NAME, LABEL_TYPE))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_LABEL_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiLabel service")
    public void delete_test() {
        apiLabelService.delete(ID);
        verify(apiLabelRepository, times(1)).deleteById(ID);
    }

    @Test
    @DisplayName("An exception occurred while delete ApiLabel")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(apiLabelRepository).deleteById(ID);
        assertThatThrownBy(() -> apiLabelService.delete(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_LABEL_BY_ID_ERROR.getCode());
    }
}
