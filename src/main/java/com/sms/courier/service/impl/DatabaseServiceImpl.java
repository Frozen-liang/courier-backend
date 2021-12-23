package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.DATABASE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_DATABASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_DATABASE_BY_IDS_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_DATABASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_DATABASE_BY_ID_ERROR;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.dto.request.DataBaseRequest;
import com.sms.courier.dto.response.DataBaseResponse;
import com.sms.courier.entity.database.DatabaseEntity;
import com.sms.courier.entity.job.JobDatabase;
import com.sms.courier.mapper.DatabaseMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.DatabaseRepository;
import com.sms.courier.service.DatabaseService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepository dataBaseRepository;
    private final DatabaseMapper dataBaseMapper;
    private final CommonRepository commonRepository;

    public DatabaseServiceImpl(DatabaseRepository dataBaseRepository,
        DatabaseMapper dataBaseMapper, CommonRepository commonRepository) {
        this.dataBaseRepository = dataBaseRepository;
        this.dataBaseMapper = dataBaseMapper;
        this.commonRepository = commonRepository;
    }

    @Override
    public DataBaseResponse get(String id) {
        return dataBaseRepository.findById(id).map(dataBaseMapper::toResponse).orElseThrow(() -> ExceptionUtils.mpe(
            GET_DATABASE_BY_ID_ERROR));
    }

    @Override
    public List<DataBaseResponse> list(String projectId) {
        return dataBaseRepository.findAllByProjectIdAndRemoved(projectId, Boolean.FALSE);
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = DATABASE, template = "{{#dataBaseRequest.name}}")
    public Boolean add(DataBaseRequest dataBaseRequest) {
        try {
            dataBaseRepository.insert(dataBaseMapper.toEntity(dataBaseRequest));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the database!", e);
            throw ExceptionUtils.mpe(ADD_DATABASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = DATABASE, template = "{{#dataBaseRequest.name}}")
    public Boolean edit(DataBaseRequest dataBaseRequest) {
        try {
            Optional<DatabaseEntity> optional = dataBaseRepository.findById(dataBaseRequest.getId());
            optional.ifPresent(dataBaseEntity -> {
                DatabaseEntity newEntity = dataBaseMapper.toEntity(dataBaseRequest);
                newEntity.setPassword(StringUtils.isBlank(dataBaseRequest.getPassword())
                    ? dataBaseEntity.getPassword() : dataBaseRequest.getPassword());
                dataBaseRepository.save(newEntity);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the database!", e);
            throw ExceptionUtils.mpe(EDIT_DATABASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = DATABASE,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, DatabaseEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete database by ids!", e);
            throw ExceptionUtils.mpe(DELETE_DATABASE_BY_IDS_ERROR);
        }
    }

    @Override
    public JobDatabase findJobById(String id) {
        return dataBaseRepository.findById(id).map(dataBaseMapper::toJobDatabase).orElse(null);
    }

}
