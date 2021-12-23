package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.DataBaseRequest;
import com.sms.courier.dto.response.DataBaseResponse;
import com.sms.courier.entity.database.DatabaseEntity;
import com.sms.courier.entity.job.JobDatabase;
import com.sms.courier.mapper.DatabaseMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.DatabaseRepository;
import com.sms.courier.service.impl.DatabaseServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sms.courier.common.exception.ErrorCode.DELETE_DATABASE_BY_IDS_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_DATABASE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Tests for DataBaseService")
public class DataBaseServiceTest {

    private final DatabaseRepository dataBaseRepository = mock(DatabaseRepository.class);
    private final DatabaseMapper dataBaseMapper = mock(DatabaseMapper.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final DatabaseService dataBaseService = new DatabaseServiceImpl(dataBaseRepository, dataBaseMapper,
        commonRepository);

    private static final String ID = ObjectId.get().toString();


    @Test
    @DisplayName("Test the get method in the DataBase service")
    public void get_test() {
        Optional<DatabaseEntity> optional = Optional.of(DatabaseEntity.builder().id(ID).build());
        when(dataBaseRepository.findById(any())).thenReturn(optional);
        DataBaseResponse response = DataBaseResponse.builder().id(ID).build();
        when(dataBaseMapper.toResponse(any())).thenReturn(response);
        DataBaseResponse dto = dataBaseService.get(ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while get database")
    public void get_exception_test() {
        when(dataBaseRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> dataBaseService.get(ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list method in the DataBase service")
    public void list_test() {
        List<DataBaseResponse> responses = Lists.newArrayList(DataBaseResponse.builder().id(ID).build());
        when(dataBaseRepository.findAllByProjectIdAndRemoved(any(), any(Boolean.class))).thenReturn(responses);
        List<DataBaseResponse> dto = dataBaseService.list(ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the add method in the DataBase service")
    public void add_test() {
        DatabaseEntity databaseEntity = DatabaseEntity.builder().id(ID).build();
        when(dataBaseMapper.toEntity(any())).thenReturn(databaseEntity);
        when(dataBaseRepository.insert(any(DatabaseEntity.class))).thenReturn(databaseEntity);
        Boolean isSuccess = dataBaseService.add(DataBaseRequest.builder().id(ID).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while add database")
    public void add_exception_test() {
        DatabaseEntity databaseEntity = DatabaseEntity.builder().id(ID).build();
        when(dataBaseMapper.toEntity(any())).thenReturn(databaseEntity);
        when(dataBaseRepository.insert(any(DatabaseEntity.class))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> dataBaseService.add(DataBaseRequest.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the DataBase service")
    public void edit_test() {
        Optional<DatabaseEntity> optional = Optional.of(DatabaseEntity.builder().id(ID).build());
        when(dataBaseRepository.findById(any())).thenReturn(optional);
        DatabaseEntity newEntity = DatabaseEntity.builder().id(ID).build();
        when(dataBaseMapper.toEntity(any())).thenReturn(newEntity);
        when(dataBaseRepository.save(any())).thenReturn(newEntity);
        Boolean isSuccess = dataBaseService.edit(DataBaseRequest.builder().id(ID).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while edit database")
    public void edit_exception_test() {
        when(dataBaseRepository.findById(any())).thenThrow(new ApiTestPlatformException(EDIT_DATABASE_ERROR));
        assertThatThrownBy(() -> dataBaseService.edit(DataBaseRequest.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the delete method in the DataBase service")
    public void delete_test() {
        when(commonRepository.deleteByIds(any(), eq(DatabaseEntity.class))).thenReturn(Boolean.TRUE);
        Boolean isSuccess = dataBaseService.delete(Lists.newArrayList(ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while delete database")
    public void delete_exception_test() {
        when(commonRepository.deleteByIds(any(), eq(DatabaseEntity.class)))
            .thenThrow(new ApiTestPlatformException(DELETE_DATABASE_BY_IDS_ERROR));
        assertThatThrownBy(() -> dataBaseService.delete(Lists.newArrayList(ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the findJobById method in the DataBase service")
    public void findJobById_test() {
        Optional<DatabaseEntity> optional = Optional.of(DatabaseEntity.builder().id(ID).build());
        when(dataBaseRepository.findById(any())).thenReturn(optional);
        JobDatabase jobDatabase = JobDatabase.builder().id(ID).build();
        when(dataBaseMapper.toJobDatabase(any())).thenReturn(jobDatabase);
        JobDatabase dto = dataBaseService.findJobById(ID);
        assertThat(dto).isNotNull();
    }

}
