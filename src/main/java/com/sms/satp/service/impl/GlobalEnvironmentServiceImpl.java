package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_GLOBAL_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_ENVIRONMENT_LIST_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import com.sms.satp.entity.env.GlobalEnvironment;
import com.sms.satp.mapper.GlobalEnvironmentMapper;
import com.sms.satp.repository.GlobalEnvironmentRepository;
import com.sms.satp.service.GlobalEnvironmentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalEnvironmentServiceImpl implements GlobalEnvironmentService {

    private final GlobalEnvironmentRepository globalEnvironmentRepository;
    private final GlobalEnvironmentMapper globalEnvironmentMapper;
    private static final String CREATE_DATE_TIME = "createDateTime";

    public GlobalEnvironmentServiceImpl(GlobalEnvironmentRepository globalEnvironmentRepository,
        GlobalEnvironmentMapper globalEnvironmentMapper) {
        this.globalEnvironmentRepository = globalEnvironmentRepository;
        this.globalEnvironmentMapper = globalEnvironmentMapper;
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
            globalEnvironment.setId(new ObjectId().toString());
            globalEnvironment.setCreateDateTime(LocalDateTime.now());
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
            globalEnvironmentRepository.findById(globalEnvironment.getId())
                .ifPresent((oldGlobalEnvironment) -> {
                    globalEnvironment.setModifyDateTime(LocalDateTime.now());
                    globalEnvironment.setCreateDateTime(oldGlobalEnvironment.getCreateDateTime());
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
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            return globalEnvironmentRepository.findAll(sort).stream().map(globalEnvironmentMapper::toDto).collect(
                Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the GlobalEnvironment list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_ENVIRONMENT_LIST_ERROR);
        }
    }
}
