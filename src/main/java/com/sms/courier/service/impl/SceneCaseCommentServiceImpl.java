package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_COMMENT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_REPLIED_COMMENT_NOT_EXIST;
import static com.sms.courier.common.field.SceneCaseCommentField.SCENE_CASE_ID;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.SceneCaseCommentRequest;
import com.sms.courier.dto.response.SceneCaseCommentResponse;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.entity.scenetest.SceneCaseCommentEntity;
import com.sms.courier.mapper.SceneCaseCommentMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.SceneCaseCommentRepository;
import com.sms.courier.service.SceneCaseCommentService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import com.sms.courier.utils.TreeUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseCommentServiceImpl implements SceneCaseCommentService {

    private final SceneCaseCommentRepository sceneCaseCommentRepository;
    private final CommonRepository commonRepository;
    private final SceneCaseCommentMapper sceneCaseCommentMapper;

    public SceneCaseCommentServiceImpl(SceneCaseCommentRepository sceneCaseCommentRepository,
        CommonRepository commonRepository, SceneCaseCommentMapper sceneCaseCommentMapper) {
        this.sceneCaseCommentRepository = sceneCaseCommentRepository;
        this.commonRepository = commonRepository;
        this.sceneCaseCommentMapper = sceneCaseCommentMapper;
    }

    @Override
    public List<TreeResponse> list(ObjectId sceneCaseId) {
        try {
            List<SceneCaseCommentResponse> sceneCaseCommentEntity = commonRepository
                .listLookupUser("SceneCaseComment", List.of(SCENE_CASE_ID.is(sceneCaseId)),
                    SceneCaseCommentResponse.class);
            return TreeUtils.createTree(sceneCaseCommentEntity);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseComment list!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_COMMENT_LIST_ERROR);
        }
    }

    @Override
    public Boolean add(SceneCaseCommentRequest sceneCaseCommentRequest) {
        log.info("SceneCaseCommentService-add()-params: [SceneCaseComment]={}", sceneCaseCommentRequest.toString());
        try {
            if (StringUtils.isNotBlank(sceneCaseCommentRequest.getParentId())) {
                SceneCaseCommentEntity parentComment = sceneCaseCommentRepository
                    .findById(sceneCaseCommentRequest.getParentId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_REPLIED_COMMENT_NOT_EXIST));
                Assert.isFalse(SecurityUtil.getCurrUserId().equals(parentComment.getCreateUserId()), "Can't reply to "
                    + "your own comments!");
            }
            SceneCaseCommentEntity sceneCaseCommentEntity = sceneCaseCommentMapper.toEntity(sceneCaseCommentRequest);
            sceneCaseCommentRepository.insert(sceneCaseCommentEntity);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseComment!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_COMMENT_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(List<String> ids) {
        try {
            sceneCaseCommentRepository.deleteByIdIn(ids);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseComment!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_COMMENT_BY_ID_ERROR);
        }
    }

}
