package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.CIRCULAR_REFERENCE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DATE_STRUCTURE_CANNOT_DELETE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_DATA_STRUCTURE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_DATA_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATA_STRUCTURE_REF_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_NAME_EXISTS_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.DataStructureListRequest;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.structure.StructureEntity;
import com.sms.courier.entity.structure.StructureRefRecordEntity;
import com.sms.courier.mapper.DataStructureMapper;
import com.sms.courier.mapper.DataStructureMapperImpl;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.mapper.ParamInfoMapperImpl;
import com.sms.courier.repository.ApiDataStructureRefRecordRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.DataStructureRefRecordRepository;
import com.sms.courier.repository.DataStructureRepository;
import com.sms.courier.service.impl.DataStructureServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;


@DisplayName("Tests for DataStructureService")
class DataStructureServiceTest {

    private final DataStructureRepository dataStructureRepository = mock(DataStructureRepository.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository = mock(
        ApiDataStructureRefRecordRepository.class);
    private final DataStructureRefRecordRepository structureRefRecordRepository = mock(
        DataStructureRefRecordRepository.class);
    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final DataStructureMapper dataStructureMapper = new DataStructureMapperImpl(paramInfoMapper);
    private final DataStructureService dataStructureService = new DataStructureServiceImpl(
        dataStructureRepository, structureRefRecordRepository, apiDataStructureRefRecordRepository, commonRepository,
        dataStructureMapper);
    private final StructureEntity dataStructure = StructureEntity.builder().id(ID).build();
    private final DataStructureResponse dataStructureResponse = DataStructureResponse.builder()
        .id(ID).build();
    private final DataStructureRequest dataStructureRequest = DataStructureRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the DataStructure service")
    public void findById_test() {
        when(dataStructureRepository.findById(ID)).thenReturn(Optional.of(dataStructure));
        when(structureRefRecordRepository.findById(any()))
            .thenReturn(Optional.of(StructureRefRecordEntity.builder().structureRef(List.of(
                StructureRefRecordEntity.builder().id(ObjectId.get().toString()).build())).build()));
        DataStructureResponse result1 = dataStructureService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting DataStructure")
    public void findById_exception_test() {
        when(dataStructureRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> dataStructureService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_STRUCTURE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the DataStructure service")
    public void add_test() {
        when(dataStructureRepository.insert(any(StructureEntity.class))).thenReturn(dataStructure);
        when(structureRefRecordRepository.save(any(StructureRefRecordEntity.class)))
            .thenReturn(new StructureRefRecordEntity());
        assertThat(dataStructureService.add(dataStructureRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding DataStructure")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(dataStructureRepository).insert(any(StructureEntity.class));
        when(structureRefRecordRepository.save(any(StructureRefRecordEntity.class)))
            .thenReturn(new StructureRefRecordEntity());
        assertThatThrownBy(() -> dataStructureService.add(dataStructureRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_DATA_STRUCTURE_ERROR.getCode());
    }

    @Test
    @DisplayName("An DuplicateKeyException occurred while edit DataStructure")
    public void Add_DuplicateKeyException_test() {
        when(dataStructureRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(structureRefRecordRepository.save(any(StructureRefRecordEntity.class)))
            .thenReturn(new StructureRefRecordEntity());
        doThrow(new DuplicateKeyException("")).when(dataStructureRepository).insert(any(StructureEntity.class));
        assertThatThrownBy(() -> dataStructureService.add(dataStructureRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_NAME_EXISTS_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the DataStructure service")
    public void edit_test() {
        when(dataStructureRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(dataStructureRepository.save(any(StructureEntity.class))).thenReturn(dataStructure);
        when(structureRefRecordRepository.save(any(StructureRefRecordEntity.class)))
            .thenReturn(new StructureRefRecordEntity());
        when(structureRefRecordRepository.findByIdIn(any()))
            .thenReturn(List.of(StructureRefRecordEntity.builder().id(ObjectId.get().toString()).build()));
        assertThat(dataStructureService.edit(dataStructureRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit DataStructure")
    public void edit_exception_test() {
        when(dataStructureRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(structureRefRecordRepository.findByIdIn(any()))
            .thenReturn(List.of(StructureRefRecordEntity.builder().id(ObjectId.get().toString()).build()));
        doThrow(new RuntimeException()).when(dataStructureRepository).save(any(StructureEntity.class));
        assertThatThrownBy(() -> dataStructureService.edit(dataStructureRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_DATA_STRUCTURE_ERROR.getCode());
    }

    @Test
    @DisplayName("An DuplicateKeyException occurred while edit DataStructure")
    public void edit_DuplicateKeyException_test() {
        when(dataStructureRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(structureRefRecordRepository.findByIdIn(any()))
            .thenReturn(List.of(StructureRefRecordEntity.builder().id(ObjectId.get().toString()).build()));
        doThrow(new DuplicateKeyException("")).when(dataStructureRepository).save(any(StructureEntity.class));
        assertThatThrownBy(() -> dataStructureService.edit(dataStructureRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_NAME_EXISTS_ERROR.getCode());
    }

    @Test
    @DisplayName("An ReferenceException occurred while edit DataStructure")
    public void edit_ReferenceException_test() {
        when(dataStructureRepository.existsById(any())).thenReturn(Boolean.TRUE);
        dataStructureRequest.setAddStructIds(List.of(ID));
        when(structureRefRecordRepository.findByIdIn(any()))
            .thenReturn(List.of(StructureRefRecordEntity.builder().id(ID).build()));
        assertThatThrownBy(() -> dataStructureService.edit(dataStructureRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(CIRCULAR_REFERENCE_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit DataStructure")
    public void edit_not_exist_exception_test() {
        when(dataStructureRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> dataStructureService.edit(dataStructureRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getDataStructureList method in the DataStructure service")
    public void getDataStructureList_test() {
        DataStructureListRequest dataStructureListRequest = DataStructureListRequest.builder().projectId(PROJECT_ID)
            .workspaceId(ObjectId.get().toString()).id(ID).build();
        List<Object> dataStructureList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataStructureList.add(StructureEntity.builder().build());
        }
        when(commonRepository.list(any(Query.class), any())).thenReturn(dataStructureList);
        Page<DataStructureListResponse> result = dataStructureService
            .getDataStructureList(dataStructureListRequest);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting DataStructure list")
    public void getDataStructureList_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).list(any(Query.class), any());
        assertThatThrownBy(() -> dataStructureService.getDataStructureList(DataStructureListRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_STRUCTURE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getDataStructureDataList method in the DataStructure service")
    public void getDataStructureDataList_test() {
        DataStructureListRequest dataStructureListRequest = DataStructureListRequest.builder().projectId(PROJECT_ID)
            .workspaceId(ObjectId.get().toString()).id(ID).build();
        List<Object> dataStructureList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataStructureList.add(StructureEntity.builder().build());
        }
        when(commonRepository.list(any(Query.class), any())).thenReturn(dataStructureList);
        List<DataStructureResponse> result = dataStructureService.getDataStructureDataList(dataStructureListRequest);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting DataStructure data list")
    public void getDataStructureDataList_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).list(any(Query.class), any());
        assertThatThrownBy(
            () -> dataStructureService.getDataStructureDataList(DataStructureListRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_STRUCTURE_DATA_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getRefStructList method in the DataStructure service")
    public void getRefStructList_test() {
        DataStructureListRequest dataStructureListRequest = DataStructureListRequest.builder().projectId(PROJECT_ID)
            .workspaceId(ObjectId.get().toString()).id(ID).build();
        List<DataStructureResponse> dataStructureList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataStructureList.add(DataStructureResponse.builder().build());
        }
        when(commonRepository.list(any(Query.class), any())).thenReturn(Collections.emptyList());
        when(dataStructureRepository.findByRefIdInAndStructTypeAndIdNotIn(any(), any(), any()))
            .thenReturn(dataStructureList);
        List<DataStructureResponse> result = dataStructureService.getRefStructList(dataStructureListRequest);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("Test the getRefStructList method then return empty in the DataStructure service")
    public void getRefStructList_test_then_return_empty() {
        DataStructureListRequest dataStructureListRequest = DataStructureListRequest.builder().projectId(PROJECT_ID)
            .workspaceId(ObjectId.get().toString()).build();
        when(dataStructureRepository.findByRefIdInAndStructType(any(), any())).thenReturn(Collections.emptyList());
        List<DataStructureResponse> result = dataStructureService.getRefStructList(dataStructureListRequest);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while getting DataStructure ref list")
    public void getRefStructList_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).list(any(Query.class), any());
        assertThatThrownBy(
            () -> dataStructureService.getRefStructList(DataStructureListRequest.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_DATA_STRUCTURE_REF_LIST_ERROR.getCode());
    }


    @Test
    @DisplayName("Test the delete method in the DataStructure service")
    public void delete_test() {
        when(dataStructureRepository.deleteByIdIs(ID)).thenReturn(Boolean.TRUE);
        when(apiDataStructureRefRecordRepository.findByRefStructIdsIs(ID)).thenReturn(Collections.emptyList());
        assertThat(dataStructureService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete DataStructure")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(dataStructureRepository)
            .deleteByIdIs(ID);
        assertThatThrownBy(() -> dataStructureService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_DATA_STRUCTURE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the DataStructure service")
    public void delete_reference_exception_test() {
        when(dataStructureRepository.deleteByIdIs(ID)).thenReturn(Boolean.TRUE);
        when(apiDataStructureRefRecordRepository.findByRefStructIdsIs(ID))
            .thenReturn(List.of(DataStructureReferenceResponse.builder().build()));
        assertThatThrownBy(() -> dataStructureService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DATE_STRUCTURE_CANNOT_DELETE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the getReference method in the DataStructure service")
    public void getReference_test() {
        List<Object> dataStructureList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            dataStructureList.add(StructureRefRecordEntity.builder().build());
        }
        when(commonRepository.list(any(Query.class), any())).thenReturn(dataStructureList);
        when(apiDataStructureRefRecordRepository.findByRefStructIdsIs(any())).thenReturn(Collections.emptyList());
        Map<String, List<DataStructureReferenceResponse>> result = dataStructureService.getReference(ID);
        assertThat(result).isNotNull();
        assertThat(result.get("dataStructure")).hasSize(10);
    }

}
