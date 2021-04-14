package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_GLOBAL_FUNCTION_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_GLOBAL_FUNCTION_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_GLOBAL_FUNCTION_LIST_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalFunctionDto;
import com.sms.satp.entity.function.GlobalFunction;
import com.sms.satp.mapper.GlobalFunctionMapper;
import com.sms.satp.repository.GlobalFunctionRepository;
import com.sms.satp.service.GlobalFunctionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalFunctionServiceImpl implements GlobalFunctionService {

    private final GlobalFunctionRepository globalFunctionRepository;
    private final GlobalFunctionMapper globalFunctionMapper;
    private static final String CREATE_DATE_TIME = "createDateTime";

    public GlobalFunctionServiceImpl(GlobalFunctionRepository globalFunctionRepository,
        GlobalFunctionMapper globalFunctionMapper) {
        this.globalFunctionRepository = globalFunctionRepository;
        this.globalFunctionMapper = globalFunctionMapper;
    }

    @Override
    public GlobalFunctionDto findById(String id) {
        try {
            Optional<GlobalFunction> optional = globalFunctionRepository.findById(id);
            return globalFunctionMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the GlobalFunction by id!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_FUNCTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<GlobalFunctionDto> list(String functionDesc, String functionName) {
        try {
            GlobalFunction globalFunction = GlobalFunction.builder().functionDesc(functionDesc)
                .functionName(functionName).build();
            ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<GlobalFunction> example = Example.of(globalFunction, matcher);
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            return globalFunctionRepository.findAll(example, sort).stream().map(globalFunctionMapper::toDto).collect(
                Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the GlobalFunction list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_FUNCTION_LIST_ERROR);
        }
    }


    @Override
    public void add(GlobalFunctionDto globalFunctionDto) {
        log.info("GlobalFunctionService-add()-params: [GlobalFunction]={}", globalFunctionDto.toString());
        try {
            GlobalFunction globalFunction = globalFunctionMapper.toEntity(globalFunctionDto);
            globalFunction.setId(new ObjectId().toString());
            globalFunction.setCreateDateTime(LocalDateTime.now());
            globalFunctionRepository.insert(globalFunction);
        } catch (Exception e) {
            log.error("Failed to add the GlobalFunction!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_FUNCTION_ERROR);
        }
    }

    @Override
    public void edit(GlobalFunctionDto globalFunctionDto) {
        log.info("GlobalFunctionService-edit()-params: [GlobalFunction]={}", globalFunctionDto.toString());
        try {
            GlobalFunction globalFunction = globalFunctionMapper.toEntity(globalFunctionDto);
            globalFunctionRepository.findById(globalFunction.getId())
                .ifPresent((oldGlobalFunction) -> {
                    globalFunction.setModifyDateTime(LocalDateTime.now());
                    globalFunction.setCreateDateTime(oldGlobalFunction.getCreateDateTime());
                    globalFunctionRepository.save(globalFunction);
                });
        } catch (Exception e) {
            log.error("Failed to add the GlobalFunction!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_FUNCTION_ERROR);
        }
    }

    @Override
    public void delete(String id) {
        try {
            globalFunctionRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the GlobalFunction!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_FUNCTION_BY_ID_ERROR);
        }
    }

}