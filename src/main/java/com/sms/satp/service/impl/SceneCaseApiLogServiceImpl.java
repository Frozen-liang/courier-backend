package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_API_LOG_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_LOG_PAGE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.scenetest.SceneCaseApiLog;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.repository.SceneCaseApiLogRepository;
import com.sms.satp.service.SceneCaseApiLogService;
import com.sms.satp.utils.PageDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseApiLogServiceImpl implements SceneCaseApiLogService {

    private final SceneCaseApiLogRepository sceneCaseApiLogRepository;
    private final SceneCaseApiLogMapper sceneCaseApiLogMapper;

    public SceneCaseApiLogServiceImpl(SceneCaseApiLogRepository sceneCaseApiLogRepository,
        SceneCaseApiLogMapper sceneCaseApiLogMapper) {
        this.sceneCaseApiLogRepository = sceneCaseApiLogRepository;
        this.sceneCaseApiLogMapper = sceneCaseApiLogMapper;
    }

    @Override
    public Page<SceneCaseApiLogDto> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            SceneCaseApiLog sceneCase = SceneCaseApiLog.builder()
                .projectId(projectId)
                .build();
            Example<SceneCaseApiLog> example = Example.of(sceneCase);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return sceneCaseApiLogRepository.findAll(example, pageable)
                .map(sceneCaseApiLogMapper::toSceneCaseApiLogDto);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApiLog page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_LOG_PAGE_ERROR);
        }
    }

    @Override
    public void add(SceneCaseApiLogDto sceneCaseApiLogDto) {
        try {
            SceneCaseApiLog sceneCaseApiLog = sceneCaseApiLogMapper.toSceneCaseApiLog(sceneCaseApiLogDto);
            sceneCaseApiLogRepository.insert(sceneCaseApiLog);
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApiLog!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_LOG_ERROR);
        }
    }

}
