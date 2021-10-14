package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_API_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_COMMENT_LIST_ERROR;
import static com.sms.courier.common.field.ApiCommentField.API_ID;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.ApiCommentResponse;
import com.sms.courier.entity.api.ApiCommentEntity;
import com.sms.courier.mapper.ApiCommentMapper;
import com.sms.courier.repository.ApiCommentRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.ApiCommentService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiCommentServiceImpl implements ApiCommentService {

    private final ApiCommentRepository apiCommentRepository;
    private final CommonRepository commonRepository;
    private final ApiCommentMapper apiCommentMapper;

    public ApiCommentServiceImpl(ApiCommentRepository apiCommentRepository,
        CommonRepository commonRepository,
        ApiCommentMapper apiCommentMapper) {
        this.apiCommentRepository = apiCommentRepository;
        this.commonRepository = commonRepository;
        this.apiCommentMapper = apiCommentMapper;
    }

    @Override
    public ApiCommentResponse findById(String id) {
        return apiCommentRepository.findById(id).map(apiCommentMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_API_COMMENT_BY_ID_ERROR));
    }

    @Override
    public List<ApiCommentResponse> list(ObjectId apiId) {
        try {
            return commonRepository.listLookupUser("ApiComment", List.of(API_ID.is(apiId)),
                ApiCommentResponse.class);
        } catch (Exception e) {
            log.error("Failed to get the ApiComment list!", e);
            throw new ApiTestPlatformException(GET_API_COMMENT_LIST_ERROR);
        }
    }


    @Override
    public Boolean add(ApiCommentRequest apiCommentRequest) {
        log.info("ApiCommentService-add()-params: [ApiComment]={}", apiCommentRequest.toString());
        try {
            ApiCommentEntity apiComment = apiCommentMapper.toEntity(apiCommentRequest);
            apiCommentRepository.insert(apiComment);
        } catch (Exception e) {
            log.error("Failed to add the ApiComment!", e);
            throw new ApiTestPlatformException(ADD_API_COMMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(ApiCommentRequest apiCommentRequest) {
        log.info("ApiCommentService-edit()-params: [ApiComment]={}", apiCommentRequest.toString());
        try {
            boolean exists = apiCommentRepository.existsById(apiCommentRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ApiComment", apiCommentRequest.getId());
            }
            ApiCommentEntity apiComment = apiCommentMapper.toEntity(apiCommentRequest);
            apiCommentRepository.save(apiComment);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the ApiComment!", e);
            throw new ApiTestPlatformException(EDIT_API_COMMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<String> ids) {
        try {
            apiCommentRepository.deleteByIdIn(ids);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the ApiComment!", e);
            throw new ApiTestPlatformException(DELETE_API_COMMENT_BY_ID_ERROR);
        }
    }

}
