package com.sms.satp.service.impl;

import static com.sms.satp.common.constant.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_PAGE_ERROR;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.service.GlobalEnvironmentService;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectEnvironmentServiceImpl implements ProjectEnvironmentService {

    private final ProjectEnvironmentRepository projectEnvironmentRepository;
    private final GlobalEnvironmentService globalEnvironmentService;
    private final MongoTemplate mongoTemplate;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;

    public ProjectEnvironmentServiceImpl(ProjectEnvironmentRepository
        projectEnvironmentRepository, GlobalEnvironmentService globalEnvironmentService,
        MongoTemplate mongoTemplate,
        ProjectEnvironmentMapper projectEnvironmentMapper) {
        this.projectEnvironmentRepository = projectEnvironmentRepository;
        this.globalEnvironmentService = globalEnvironmentService;
        this.mongoTemplate = mongoTemplate;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
    }

    @Override
    public Page<ProjectEnvironmentResponse> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
                .projectId(projectId)
                .build();
            Example<ProjectEnvironment> example = Example.of(projectEnvironment);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return projectEnvironmentRepository.findAll(example, pageable)
                .map(projectEnvironmentMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment page!", e);
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_PAGE_ERROR);
        }
    }

    @Override
    public List<Object> list(String projectId) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().projectId(projectId).build();
            List<Object> result = new ArrayList<>();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, GenericPropertyMatchers.exact())
                .withMatcher(REMOVE, GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<ProjectEnvironment> example = Example.of(projectEnvironment, exampleMatcher);
            List<GlobalEnvironmentResponse> globalEnvironments = globalEnvironmentService.list();
            List<ProjectEnvironment> projectEnvironments = projectEnvironmentRepository.findAll(example, sort);
            result.addAll(globalEnvironments);
            result.addAll(projectEnvironmentMapper.toDtoList(projectEnvironments));
            return result;
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_LIST_ERROR);
        }
    }

    @Override
    public Boolean add(ProjectEnvironmentRequest projectEnvironmentRequest) {
        log.info("ProjectEnvironmentService-add()-params: [ProjectEnvironment]={}",
            projectEnvironmentRequest.toString());
        try {
            ProjectEnvironment projectEnvironment = projectEnvironmentMapper
                .toEntity(projectEnvironmentRequest);
            projectEnvironmentRepository.insert(projectEnvironment);
        } catch (Exception e) {
            log.error("Failed to add the projectEnvironment!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(ProjectEnvironmentRequest projectEnvironmentRequest) {
        log.info("ProjectEnvironmentService-edit()-params: [ProjectEnvironment]={}",
            projectEnvironmentRequest.toString());
        try {
            ProjectEnvironment projectEnvironment = projectEnvironmentMapper
                .toEntity(projectEnvironmentRequest);
            Optional<ProjectEnvironment> optional = projectEnvironmentRepository.findById(projectEnvironment.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            optional.ifPresent(oldProjectEnvironment -> {
                projectEnvironment.setCreateDateTime(oldProjectEnvironment.getCreateDateTime());
                projectEnvironment.setCreateUserId(oldProjectEnvironment.getCreateUserId());
                projectEnvironmentRepository.save(projectEnvironment);
            });
        } catch (Exception e) {
            log.error("Failed to edit the projectEnvironment!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_ENVIRONMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(String[] ids) {
        try {
            Query query = new Query(Criteria.where(ID).in(Arrays.asList(ids)));
            Update update = Update.update(REMOVE, Boolean.TRUE);
            update.set(MODIFY_DATE_TIME, LocalDateTime.now());
            UpdateResult updateResult = mongoTemplate.updateMulti(query, update, ProjectEnvironment.class);
            if (updateResult.getModifiedCount() > 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("Failed to delete the projectEnvironment!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR);
        }
        return Boolean.FALSE;
    }

    @Override
    public ProjectEnvironmentResponse findById(String id) {
        try {
            Optional<ProjectEnvironment> projectEnvironmentOptional
                = projectEnvironmentRepository.findById(id);
            return projectEnvironmentMapper.toDto(projectEnvironmentOptional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ProjectEnvironment by id!", e);
            throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR);
        }
    }

}