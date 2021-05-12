package com.sms.satp.service.impl;

import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID;
import static com.sms.satp.common.exception.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.mapper.GlobalEnvironmentMapper;
import com.sms.satp.repository.GlobalEnvironmentRepository;
import com.sms.satp.service.GlobalEnvironmentService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalEnvironmentServiceImpl implements GlobalEnvironmentService {

    private final GlobalEnvironmentRepository globalEnvironmentRepository;
    private final GlobalEnvironmentMapper globalEnvironmentMapper;
    private final MongoTemplate mongoTemplate;

    public GlobalEnvironmentServiceImpl(GlobalEnvironmentRepository globalEnvironmentRepository,
        GlobalEnvironmentMapper globalEnvironmentMapper,
        MongoTemplate mongoTemplate) {
        this.globalEnvironmentRepository = globalEnvironmentRepository;
        this.globalEnvironmentMapper = globalEnvironmentMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public GlobalEnvironmentResponse findById(String id) {
        try {
            Optional<GlobalEnvironment> optional = globalEnvironmentRepository.findById(id);
            return globalEnvironmentMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment by id!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest) {
        log.info("GlobalEnvironmentService-add()-params: [GlobalEnvironment]={}", globalEnvironmentRequest.toString());
        try {
            GlobalEnvironment globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentRequest);
            globalEnvironmentRepository.insert(globalEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest) {
        log.info("GlobalEnvironmentService-edit()-params: [GlobalEnvironment]={}", globalEnvironmentRequest.toString());
        try {
            GlobalEnvironment globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentRequest);
            Optional<GlobalEnvironment> optional = globalEnvironmentRepository.findById(globalEnvironment.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            optional.ifPresent((oldGlobalEnvironment) -> {
                globalEnvironment.setCreateDateTime(oldGlobalEnvironment.getCreateDateTime());
                globalEnvironment.setCreateUserId(oldGlobalEnvironment.getCreateUserId());
                globalEnvironmentRepository.save(globalEnvironment);
            });
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<GlobalEnvironmentResponse> list() {
        try {
            List<GlobalEnvironment> globalEnvironments = globalEnvironmentRepository
                .findByRemovedOrderByCreateDateTimeDesc(Boolean.FALSE);
            return globalEnvironmentMapper.toDtoList(globalEnvironments);
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    public Boolean delete(String[] ids) {
        try {
            Query query = new Query(Criteria.where(ID).in(Arrays.asList(ids)));
            Update update = Update.update(REMOVE, Boolean.TRUE);
            update.set(MODIFY_DATE_TIME, LocalDateTime.now());
            UpdateResult updateResult = mongoTemplate.updateMulti(query, update, GlobalEnvironment.class);
            if (updateResult.getModifiedCount() > 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("Failed to delete the GlobalEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID);
        }
        return Boolean.FALSE;
    }
}
