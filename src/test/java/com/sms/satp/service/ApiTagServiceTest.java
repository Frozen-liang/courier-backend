package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_API_TAG_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_TAG_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_TAG_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.dto.ApiTagDto;
import com.sms.satp.entity.tag.ApiTag;
import com.sms.satp.mapper.ApiTagMapper;
import com.sms.satp.repository.ApiTagRepository;
import com.sms.satp.service.impl.ApiTagServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ApiTagService")
class ApiTagServiceTest {

    private final ApiTagRepository apiTagRepository = mock(ApiTagRepository.class);
    private final ApiTagMapper apiTagMapper = mock(ApiTagMapper.class);
    private final ApiTagService apiTagService = new ApiTagServiceImpl(
        apiTagRepository,
        apiTagMapper);
    private final ApiTag apiTag = ApiTag.builder().id(ID).build();
    private final ApiTagDto apiTagDto = ApiTagDto.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String NOT_EXIST_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = "10";
    private static final String LABEL_NAME = "testName";
    private static final ApiTagType LABEL_TYPE = ApiTagType.getType(1);

    @Test
    @DisplayName("Test the findById method in the ApiTag service")
    public void findById_test() {
        when(apiTagRepository.findById(ID)).thenReturn(Optional.of(apiTag));
        when(apiTagMapper.toDto(apiTag)).thenReturn(apiTagDto);
        ApiTagDto result1 = apiTagService.findById(ID);
        ApiTagDto result2 = apiTagService.findById(NOT_EXIST_ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTag")
    public void findById_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).findById(ID);
        assertThatThrownBy(() -> apiTagService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiTag service")
    public void add_test() {
        when(apiTagMapper.toEntity(apiTagDto)).thenReturn(apiTag);
        when(apiTagRepository.insert(any(ApiTag.class))).thenReturn(apiTag);
        apiTagService.add(apiTagDto);
        verify(apiTagRepository, times(1)).insert(any(ApiTag.class));
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTag")
    public void add_exception_test() {
        when(apiTagMapper.toEntity(apiTagDto)).thenReturn(apiTag);
        doThrow(new RuntimeException()).when(apiTagRepository).insert(any(ApiTag.class));
        assertThatThrownBy(() -> apiTagService.add(apiTagDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TAG_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTag service")
    public void edit_test() {
        when(apiTagMapper.toEntity(apiTagDto)).thenReturn(apiTag);
        when(apiTagRepository.findById(any()))
            .thenReturn(Optional.of(ApiTag.builder().id(ID).build()));
        when(apiTagRepository.save(any(ApiTag.class))).thenReturn(apiTag);
        apiTagService.edit(apiTagDto);
        verify(apiTagRepository, times(1)).save(any(ApiTag.class));
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTag")
    public void edit_exception_test() {
        when(apiTagMapper.toEntity(apiTagDto)).thenReturn(apiTag);
        when(apiTagRepository.findById(any())).thenReturn(Optional.of(apiTag));
        doThrow(new RuntimeException()).when(apiTagRepository).save(any(ApiTag.class));
        assertThatThrownBy(() -> apiTagService.edit(apiTagDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_TAG_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiTag service")
    public void list_test() {
        ArrayList<ApiTag> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ApiTag.builder().build());
        }
        ArrayList<ApiTagDto> apiTagDtos = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTagDtos.add(ApiTagDto.builder().build());
        }
        when(apiTagRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(apiTagMapper.toDtoList(list)).thenReturn(apiTagDtos);
        List<ApiTagDto> result = apiTagService.list(PROJECT_ID, LABEL_NAME, LABEL_TYPE);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTag list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> apiTagService.list(PROJECT_ID, LABEL_NAME, LABEL_TYPE))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTag service")
    public void delete_test() {
        apiTagService.delete(ID);
        verify(apiTagRepository, times(1)).deleteById(ID);
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTag")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).deleteById(ID);
        assertThatThrownBy(() -> apiTagService.delete(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TAG_BY_ID_ERROR.getCode());
    }
}
