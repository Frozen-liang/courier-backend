package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_API_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_COMMENT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_REPLIED_COMMENT_NOT_EXIST;
import static com.sms.courier.common.field.ApiCommentField.API_ID;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.ApiCommentResponse;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.entity.api.ApiCommentEntity;
import com.sms.courier.mapper.ApiCommentMapper;
import com.sms.courier.repository.ApiCommentRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.ApiCommentService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import com.sms.courier.utils.TreeUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public List<TreeResponse> list(ObjectId apiId) {
        try {
            List<ApiCommentResponse> apiComment = commonRepository
                .listLookupUser("ApiComment", List.of(API_ID.is(apiId)), ApiCommentResponse.class);
            return TreeUtils.createTree(apiComment);
        } catch (Exception e) {
            log.error("Failed to get the ApiComment list!", e);
            throw new ApiTestPlatformException(GET_API_COMMENT_LIST_ERROR);
        }
    }


    @Override
    public Boolean add(ApiCommentRequest apiCommentRequest) {
        log.info("ApiCommentService-add()-params: [ApiComment]={}", apiCommentRequest.toString());
        try {
            if (StringUtils.isNotBlank(apiCommentRequest.getParentId())) {
                ApiCommentEntity parentComment = apiCommentRepository.findById(apiCommentRequest.getParentId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_REPLIED_COMMENT_NOT_EXIST));
                Assert.isFalse(SecurityUtil.getCurrUserId().equals(parentComment.getCreateUserId()), "Can't reply to "
                    + "your own comments!");
            }
            ApiCommentEntity apiComment = apiCommentMapper.toEntity(apiCommentRequest);
            apiCommentRepository.insert(apiComment);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the ApiComment!", e);
            throw new ApiTestPlatformException(ADD_API_COMMENT_ERROR);
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
