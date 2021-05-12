package com.sms.satp.service.impl;

import static com.sms.satp.common.constant.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_GLOBAL_FUNCTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_GLOBAL_FUNCTION_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_GLOBAL_FUNCTION_LIST_ERROR;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.entity.function.GlobalFunction;
import com.sms.satp.mapper.GlobalFunctionMapper;
import com.sms.satp.repository.GlobalFunctionRepository;
import com.sms.satp.service.GlobalFunctionService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalFunctionServiceImpl implements GlobalFunctionService {

    private final GlobalFunctionRepository globalFunctionRepository;
    private final GlobalFunctionMapper globalFunctionMapper;
    private final MongoTemplate mongoTemplate;
    private static final String FUNCTION_KEY = "functionKey";

    public GlobalFunctionServiceImpl(GlobalFunctionRepository globalFunctionRepository,
        GlobalFunctionMapper globalFunctionMapper, MongoTemplate mongoTemplate) {
        this.globalFunctionRepository = globalFunctionRepository;
        this.globalFunctionMapper = globalFunctionMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public GlobalFunctionResponse findById(String id) {
        try {
            Optional<GlobalFunction> optional = globalFunctionRepository.findById(id);
            return globalFunctionMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the GlobalFunction by id!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_FUNCTION_BY_ID_ERROR);
        }
    }

    @Override
    public List<GlobalFunctionResponse> list(String functionKey, String functionName) {
        try {
            GlobalFunction globalFunction = GlobalFunction.builder().functionKey(functionKey)
                .functionName(functionName).build();
            ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(REMOVE, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(FUNCTION_KEY, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
            Example<GlobalFunction> example = Example.of(globalFunction, matcher);
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME);
            return globalFunctionMapper.toDtoList(globalFunctionRepository.findAll(example, sort));
        } catch (Exception e) {
            log.error("Failed to get the GlobalFunction list!", e);
            throw new ApiTestPlatformException(GET_GLOBAL_FUNCTION_LIST_ERROR);
        }
    }


    @Override
    public Boolean add(GlobalFunctionRequest globalFunctionRequest) {
        log.info("GlobalFunctionService-add()-params: [GlobalFunction]={}", globalFunctionRequest.toString());
        try {
            GlobalFunction globalFunction = globalFunctionMapper.toEntity(globalFunctionRequest);
            globalFunctionRepository.insert(globalFunction);
        } catch (Exception e) {
            log.error("Failed to add the GlobalFunction!", e);
            throw new ApiTestPlatformException(ADD_GLOBAL_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(GlobalFunctionRequest globalFunctionRequest) {
        log.info("GlobalFunctionService-edit()-params: [GlobalFunction]={}", globalFunctionRequest.toString());
        try {
            GlobalFunction globalFunction = globalFunctionMapper.toEntity(globalFunctionRequest);
            Optional<GlobalFunction> optional = globalFunctionRepository.findById(globalFunction.getId());
            if (optional.isEmpty()) {
                return Boolean.FALSE;
            }
            optional.ifPresent((oldGlobalFunction) -> {
                globalFunction.setCreateDateTime(oldGlobalFunction.getCreateDateTime());
                globalFunction.setCreateUserId(oldGlobalFunction.getCreateUserId());
                globalFunctionRepository.save(globalFunction);
            });
        } catch (Exception e) {
            log.error("Failed to add the GlobalFunction!", e);
            throw new ApiTestPlatformException(EDIT_GLOBAL_FUNCTION_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(String[] ids) {
        try {
            Query query = new Query(Criteria.where(ID).in(Arrays.asList(ids)));
            Update update = Update.update(REMOVE, Boolean.TRUE);
            update.set(MODIFY_DATE_TIME, LocalDateTime.now());
            UpdateResult updateResult = mongoTemplate.updateMulti(query, update, GlobalFunction.class);
            if (updateResult.getModifiedCount() > 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("Failed to delete the GlobalFunction!", e);
            throw new ApiTestPlatformException(DELETE_GLOBAL_FUNCTION_BY_ID_ERROR);
        }
        return Boolean.FALSE;
    }

}