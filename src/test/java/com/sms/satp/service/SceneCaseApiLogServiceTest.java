package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.exception.ErrorCode;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.scenetest.SceneCaseApiLog;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.repository.SceneCaseApiLogRepository;
import com.sms.satp.service.impl.SceneCaseApiLogServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_API_LOG_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test cases for SceneCaseApiLogServiceTest")
class SceneCaseApiLogServiceTest {

    private SceneCaseApiLogRepository sceneCaseApiLogRepository;
    private SceneCaseApiLogMapper sceneCaseApiLogMapper;
    private SceneCaseApiLogServiceImpl sceneCaseApiLogService;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_PROJECT_ID = "1";
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;

    @BeforeEach
    void setUpBean() {
        sceneCaseApiLogRepository = mock(SceneCaseApiLogRepository.class);
        sceneCaseApiLogMapper = mock(SceneCaseApiLogMapper.class);
        sceneCaseApiLogService = new SceneCaseApiLogServiceImpl(sceneCaseApiLogRepository, sceneCaseApiLogMapper);
    }

    @Test
    @DisplayName("Test the page method in the SceneCaseApiLog service")
    void page_test() {
        List<SceneCaseApiLog> sceneCaseApiLogList = Lists.newArrayList(SceneCaseApiLog.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<SceneCaseApiLog> sceneCaseDtoPage = new PageImpl<>(sceneCaseApiLogList, pageable, MOCK_TOTAL);
        when(sceneCaseApiLogRepository.findAll(any(), (Pageable) any())).thenReturn(sceneCaseDtoPage);
        SceneCaseApiLogDto sceneCaseApiLogDto = SceneCaseApiLogDto.builder().sceneCaseId(MOCK_ID).build();
        when(sceneCaseApiLogMapper.toDtoBySceneCase(any(), any())).thenReturn(sceneCaseApiLogDto);
        Page<SceneCaseApiLogDto> page = sceneCaseApiLogService.page(PageDto.builder().build(), MOCK_PROJECT_ID);
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the SceneCaseApiLog service throws exception")
    void page_test_thenThrownException() {
        when(sceneCaseApiLogRepository.findAll(any(), (Pageable) any())).thenThrow(new ApiTestPlatformException(
            ErrorCode.GET_SCENE_CASE_API_LOG_PAGE_ERROR));
        assertThatThrownBy(() -> sceneCaseApiLogService.page(PageDto.builder().build(), MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseApiLog service")
    void add_test() {
        SceneCaseApiLog sceneCaseApiLog = SceneCaseApiLog.builder().id(MOCK_ID).build();
        when(sceneCaseApiLogMapper.toSceneCaseApiLog(any())).thenReturn(sceneCaseApiLog);
        when(sceneCaseApiLogRepository.insert(any(SceneCaseApiLog.class))).thenReturn(sceneCaseApiLog);
        sceneCaseApiLogService.add(SceneCaseApiLogDto.builder().build());
        verify(sceneCaseApiLogRepository, times(1)).insert(sceneCaseApiLog);
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseApiLog service thrown exception")
    void add_test_thenThrownException() {
        SceneCaseApiLog sceneCaseApiLog = SceneCaseApiLog.builder().id(MOCK_ID).build();
        when(sceneCaseApiLogMapper.toSceneCaseApiLog(any())).thenReturn(sceneCaseApiLog);
        when(sceneCaseApiLogRepository.insert(any(SceneCaseApiLog.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_LOG_ERROR));
        assertThatThrownBy(() -> sceneCaseApiLogService.add(SceneCaseApiLogDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
