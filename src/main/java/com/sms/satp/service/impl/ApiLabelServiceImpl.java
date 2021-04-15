package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_API_LABEL_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_LABEL_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_LABEL_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_LABEL_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_LABEL_LIST_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.ApiLabel;
import com.sms.satp.entity.dto.ApiLabelDto;
import com.sms.satp.mapper.ApiLabelMapper;
import com.sms.satp.repository.ApiLabelRepository;
import com.sms.satp.service.ApiLabelService;
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

@Slf4j
@Service
public class ApiLabelServiceImpl implements ApiLabelService {

    private final ApiLabelRepository apiLabelRepository;
    private final ApiLabelMapper apiLabelMapper;
    private static final String CREATE_DATE_TIME = "createDateTime";
    private static final String PROJECT_ID = "projectId";
    private static final String LABEL_TYPE = "labelType";

    public ApiLabelServiceImpl(ApiLabelRepository apiLabelRepository, ApiLabelMapper apiLabelMapper) {
        this.apiLabelRepository = apiLabelRepository;
        this.apiLabelMapper = apiLabelMapper;
    }

    @Override
    public ApiLabelDto findById(String id) {
        try {
            Optional<ApiLabel> optional = apiLabelRepository.findById(id);
            return apiLabelMapper.toDto(optional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the ApiLabel by id!", e);
            throw new ApiTestPlatformException(GET_API_LABEL_BY_ID_ERROR);
        }
    }

    @Override
    public List<ApiLabelDto> list(String projectId, String labelName, Short labelType) {
        try {
            ApiLabel apiLabel = ApiLabel.builder().projectId(projectId).labelName(labelName)
                .labelType(labelType).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(PROJECT_ID, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(LABEL_TYPE, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnoreNullValues();
            Example<ApiLabel> example = Example.of(apiLabel, exampleMatcher);
            Sort sort = Sort.by(Direction.DESC,CREATE_DATE_TIME);
            List<ApiLabel> list = apiLabelRepository.findAll(example,sort);
            return list.stream().map(apiLabelMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the ApiLabel list!", e);
            throw new ApiTestPlatformException(GET_API_LABEL_LIST_ERROR);
        }
    }

    @Override
    public void add(ApiLabelDto apiLabelDto) {
        log.info("ApiLabelService-add()-params: [ApiLabel]={}", apiLabelDto.toString());
        try {
            ApiLabel apiLabel = apiLabelMapper.toEntity(apiLabelDto);
            apiLabel.setId(new ObjectId().toString());
            apiLabel.setCreateDateTime(LocalDateTime.now());
            apiLabelRepository.insert(apiLabel);
        } catch (Exception e) {
            log.error("Failed to add the ApiLabel!", e);
            throw new ApiTestPlatformException(ADD_API_LABEL_ERROR);
        }
    }

    @Override
    public void edit(ApiLabelDto apiLabelDto) {
        log.info("ApiLabelService-edit()-params: [ApiLabel]={}", apiLabelDto.toString());
        try {
            ApiLabel apiLabel = apiLabelMapper.toEntity(apiLabelDto);
            Optional<ApiLabel> optional = apiLabelRepository.findById(apiLabelDto.getId());
            optional.ifPresent((oldApiLabel) -> {
                apiLabel.setCreateDateTime(oldApiLabel.getCreateDateTime());
                apiLabel.setModifyDateTime(LocalDateTime.now());
                apiLabelRepository.save(apiLabel);
            });
        } catch (Exception e) {
            log.error("Failed to add the ApiLabel!", e);
            throw new ApiTestPlatformException(EDIT_API_LABEL_ERROR);
        }
    }

    @Override
    public void delete(String id) {
        try {
            apiLabelRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the ApiLabel!", e);
            throw new ApiTestPlatformException(DELETE_API_LABEL_BY_ID_ERROR);
        }
    }

}
