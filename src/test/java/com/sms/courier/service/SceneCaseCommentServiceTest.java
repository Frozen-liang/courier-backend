package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.SceneCaseCommentRequest;
import com.sms.courier.dto.response.SceneCaseCommentResponse;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.entity.scenetest.SceneCaseCommentEntity;
import com.sms.courier.mapper.SceneCaseCommentMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.SceneCaseCommentRepository;
import com.sms.courier.service.impl.SceneCaseCommentServiceImpl;
import com.sms.courier.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_COMMENT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_REPLIED_COMMENT_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for SceneCaseCommentService")
public class SceneCaseCommentServiceTest {

    private final SceneCaseCommentRepository sceneCaseCommentRepository = mock(SceneCaseCommentRepository.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final SceneCaseCommentMapper sceneCaseCommentMapper = mock(SceneCaseCommentMapper.class);

    private final SceneCaseCommentService sceneCaseCommentService =
        new SceneCaseCommentServiceImpl(sceneCaseCommentRepository, commonRepository, sceneCaseCommentMapper);
    private final SceneCaseCommentEntity sceneCaseComment = SceneCaseCommentEntity.builder().id(ID).build();
    private final SceneCaseCommentRequest sceneCaseCommentRequest = SceneCaseCommentRequest.builder().parentId(PARENT_ID)
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String PARENT_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final ObjectId API_ID = ObjectId.get();
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the add method in the sceneCaseComment service")
    public void add_test() {
        when(sceneCaseCommentRepository.findById(PARENT_ID)).thenReturn(
            Optional.of(SceneCaseCommentEntity.builder().createUserId(ID).build()));
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
        when(sceneCaseCommentRepository.insert(any(SceneCaseCommentEntity.class))).thenReturn(sceneCaseComment);
        assertThat(sceneCaseCommentService.add(sceneCaseCommentRequest)).isTrue();
    }

    @Test
    @DisplayName("An custom exception occurred while adding sceneCaseComment")
    public void add_custom_exception_test() {
        when(sceneCaseCommentRepository.findById(PARENT_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sceneCaseCommentService.add(sceneCaseCommentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_REPLIED_COMMENT_NOT_EXIST.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding sceneCaseComment")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(sceneCaseCommentRepository).findById(anyString());
        assertThatThrownBy(() -> sceneCaseCommentService.add(sceneCaseCommentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_SCENE_CASE_COMMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the sceneCaseComment service")
    public void list_test() {
        ArrayList<SceneCaseCommentResponse> sceneCaseCommentList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            sceneCaseCommentList.add(SceneCaseCommentResponse.builder().build());
        }
        when(commonRepository.listLookupUser(anyString(), any(), any(Class.class))).thenReturn(sceneCaseCommentList);
        List<TreeResponse> result = sceneCaseCommentService.list(API_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting sceneCaseComment list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).listLookupUser(anyString(), any(), any(Class.class));
        assertThatThrownBy(() -> sceneCaseCommentService.list(API_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCENE_CASE_COMMENT_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the sceneCaseComment service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        doNothing().when(sceneCaseCommentRepository).deleteByIdIn(ids);
        assertThat(sceneCaseCommentService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete sceneCaseComment")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(sceneCaseCommentRepository).deleteByIdIn(ids);
        assertThatThrownBy(() -> sceneCaseCommentService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_SCENE_CASE_COMMENT_BY_ID_ERROR.getCode());
    }

}
