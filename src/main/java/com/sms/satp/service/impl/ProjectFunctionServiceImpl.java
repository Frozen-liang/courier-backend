package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_PROJECT_FUNCTION_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_PROJECT_FUNCTION_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_FUNCTION_LIST_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalFunctionDto;
import com.sms.satp.entity.dto.ProjectFunctionDto;
import com.sms.satp.entity.function.ProjectFunction;
import com.sms.satp.mapper.ProjectFunctionMapper;
import com.sms.satp.repository.ProjectFunctionRepository;
import com.sms.satp.service.GlobalFunctionService;
import com.sms.satp.service.ProjectFunctionService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectFunctionServiceImpl implements ProjectFunctionService {

    private final ProjectFunctionRepository projectFunctionRepository;
    private final ProjectFunctionMapper projectFunctionMapper;
    private final GlobalFunctionService globalFunctionService;
    private static final String CREATE_DATE_TIME = "createDateTime";
    private static final String PROJECT_ID = "projectId";
    private static final String REMOVE = "remove";

    public ProjectFunctionServiceImpl(ProjectFunctionRepository projectFunctionRepository,
        ProjectFunctionMapper projectFunctionMapper, GlobalFunctionService globalFunctionService) {
        this.projectFunctionRepository = projectFunctionRepository;
        this.projectFunctionMapper = projectFunctionMapper;
        this.globalFunctionService = globalFunctionService;
    }

    @Override
    public ProjectFunctionDto findById(String id) {
        try {
            Optional<ProjectFunction> optional = projectFunctionRepository.findById(id);
            return projectFunctionMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ProjectFunction by id!", e);
            throw new ApiTestPlatformException(GET_PROJECT_FUNCTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<Object> list(String projectId, String functionDesc, String functionName) {
        try {
            ProjectFunction projectFunction = ProjectFunction.builder()
                .projectId(projectId).functionDesc(functionDesc).functionName(functionName).build();
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(REMOVE, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<ProjectFunction> example = Example.of(projectFunction, matcher);
            ArrayList<Object> list = new ArrayList<>();
            List<GlobalFunctionDto> globalFunctionList = globalFunctionService.list(functionDesc, functionName);
            List<ProjectFunction> projectFunctionList = projectFunctionRepository.findAll(example, sort);
            list.addAll(globalFunctionList);
            list.addAll(projectFunctionList.stream().map(projectFunctionMapper::toDto).collect(Collectors.toList()));
            return list;
        } catch (Exception e) {
            log.error("Failed to get the ProjectFunction list!", e);
            throw new ApiTestPlatformException(GET_PROJECT_FUNCTION_LIST_ERROR);
        }
    }


    @Override
    public void add(ProjectFunctionDto projectFunctionDto) {
        log.info("ProjectFunctionService-add()-params: [ProjectFunction]={}", projectFunctionDto.toString());
        try {
            ProjectFunction projectFunction = projectFunctionMapper.toEntity(projectFunctionDto);
            projectFunctionRepository.insert(projectFunction);
        } catch (Exception e) {
            log.error("Failed to add the ProjectFunction!", e);
            throw new ApiTestPlatformException(ADD_PROJECT_FUNCTION_ERROR);
        }
    }

    @Override
    public void edit(ProjectFunctionDto projectFunctionDto) {
        log.info("ProjectFunctionService-edit()-params: [ProjectFunction]={}", projectFunctionDto.toString());
        try {
            ProjectFunction projectFunction = projectFunctionMapper.toEntity(projectFunctionDto);
            projectFunctionRepository.findById(projectFunction.getId())
                .ifPresent((oldProjectFunction) -> projectFunctionRepository.save(projectFunction));
        } catch (Exception e) {
            log.error("Failed to add the ProjectFunction!", e);
            throw new ApiTestPlatformException(EDIT_PROJECT_FUNCTION_ERROR);
        }
    }

    @Override
    public void delete(String[] ids) {
        try {
            Iterable<ProjectFunction> projectFunctions = projectFunctionRepository.findAllById(Arrays.asList(ids));
            projectFunctions.forEach((projectFunction) -> projectFunction.setRemove(Boolean.TRUE));
            projectFunctionRepository.saveAll(projectFunctions);
        } catch (Exception e) {
            log.error("Failed to delete the ProjectFunction!", e);
            throw new ApiTestPlatformException(DELETE_PROJECT_FUNCTION_BY_ID_ERROR);
        }
    }

}