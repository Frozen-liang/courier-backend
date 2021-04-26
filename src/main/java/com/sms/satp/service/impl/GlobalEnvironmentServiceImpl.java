package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID;
import static com.sms.satp.common.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;
import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalEnvironmentDto;
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
    public GlobalEnvironmentDto findById(String id) {
        try {
            Optional<GlobalEnvironment> optional = globalEnvironmentRepository.findById(id);
            return globalEnvironmentMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment by id!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR);
        }
    }

    @Override
    public void add(GlobalEnvironmentDto globalEnvironmentDto) {
        log.info("GlobalEnvironmentService-add()-params: [GlobalEnvironment]={}", globalEnvironmentDto.toString());
        try {
            GlobalEnvironment globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentDto);
            globalEnvironmentRepository.insert(globalEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_ENVIRONMENT_ERROR);
        }
    }

    @Override
    public void edit(GlobalEnvironmentDto globalEnvironmentDto) {
        log.info("GlobalEnvironmentService-edit()-params: [GlobalEnvironment]={}", globalEnvironmentDto.toString());
        try {
            GlobalEnvironment globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentDto);
            Optional<GlobalEnvironment> optional = globalEnvironmentRepository.findById(globalEnvironment.getId());
            optional.ifPresent((oldGlobalEnvironment) -> {
                globalEnvironment.setCreateDateTime(oldGlobalEnvironment.getCreateDateTime());
                globalEnvironment.setCreateUserId(oldGlobalEnvironment.getCreateUserId());
                globalEnvironmentRepository.save(globalEnvironment);
            });
        } catch (Exception e) {
            log.error("Failed to add the globalEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_ENVIRONMENT_ERROR);
        }
    }

    @Override
    public List<GlobalEnvironmentDto> list() {
        try {
            List<GlobalEnvironment> globalEnvironments = globalEnvironmentRepository
                .findByRemoveOrderByCreateDateTimeDesc(Boolean.FALSE);
            return globalEnvironmentMapper.toDtoList(globalEnvironments);
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    public void delete(String[] ids) {
        try {
            Query query = new Query(Criteria.where(ID).in(Arrays.asList(ids)));
            Update update = Update.update(REMOVE, Boolean.TRUE);
            update.set(MODIFY_DATE_TIME, LocalDateTime.now());
            mongoTemplate.updateMulti(query, update, GlobalEnvironment.class);
        } catch (Exception e) {
            log.error("Failed to delete the GlobalEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_ENVIRONMENT_ERROR_BY_ID);
        }
    }
}
