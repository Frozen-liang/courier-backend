package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_OPEN_API_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_OPEN_API_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_OPEN_API_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_OPEN_API_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_OPEN_API_SETTING_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.OpenApiSettingRequest;
import com.sms.courier.dto.response.OpenApiSettingResponse;
import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import com.sms.courier.mapper.OpenApiSettingMapper;
import com.sms.courier.mapper.OpenApiSettingMapperImpl;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.OpenApiSettingRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.service.impl.OpenApiSettingServiceImpl;
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
import org.springframework.dao.DuplicateKeyException;

@DisplayName("Tests for OpenApiSettingService")
class OpenApiSettingServiceTest {

    private final OpenApiSettingRepository openApiSettingRepository = mock(OpenApiSettingRepository.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    private final OpenApiSettingMapper openApiSettingMapper = new OpenApiSettingMapperImpl();
    private final OpenApiSettingService openApiSettingService = new OpenApiSettingServiceImpl(
        openApiSettingRepository, commonRepository, jwtTokenManager, openApiSettingMapper);
    private final OpenApiSettingEntity openApiSetting = OpenApiSettingEntity.builder().id(ID).build();
    private final OpenApiSettingRequest openApiSettingRequest = OpenApiSettingRequest.builder().name("test")
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;

    private final static MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;


    static {
        SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the findById method in the OpenApiSetting service")
    public void findById_test() {
        when(openApiSettingRepository.findById(ID)).thenReturn(Optional.of(openApiSetting));
        OpenApiSettingResponse result1 = openApiSettingService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting OpenApiSetting")
    public void findById_exception_test() {
        when(openApiSettingRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> openApiSettingService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_OPEN_API_SETTING_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the OpenApiSetting service")
    public void add_test() {
        when(openApiSettingRepository.insert(any(OpenApiSettingEntity.class))).thenReturn(openApiSetting);
        when(jwtTokenManager.generateAccessToken(any())).thenReturn("token");
        assertThat(openApiSettingService.add(openApiSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An duplicate key exception occurred while adding OpenApiSetting")
    public void add_duplicateKeyException_test() {
        when(jwtTokenManager.generateAccessToken(any())).thenReturn("token");
        doThrow(new DuplicateKeyException("")).when(openApiSettingRepository).insert(any(OpenApiSettingEntity.class));
        assertThatThrownBy(() -> openApiSettingService.add(openApiSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isNull();
    }

    @Test
    @DisplayName("An exception occurred while adding OpenApiSetting")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(openApiSettingRepository).insert(any(OpenApiSettingEntity.class));
        assertThatThrownBy(() -> openApiSettingService.add(openApiSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_OPEN_API_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the OpenApiSetting service")
    public void edit_test() {
        when(openApiSettingRepository.findById(any())).thenReturn(Optional.of(OpenApiSettingEntity.builder().build()));
        when(openApiSettingRepository.save(any(OpenApiSettingEntity.class))).thenReturn(openApiSetting);
        assertThat(openApiSettingService.edit(openApiSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit OpenApiSetting")
    public void edit_exception_test() {
        when(openApiSettingRepository.findById(any())).thenReturn(Optional.of(OpenApiSettingEntity.builder().build()));
        doThrow(new RuntimeException()).when(openApiSettingRepository).save(any(OpenApiSettingEntity.class));
        assertThatThrownBy(() -> openApiSettingService.edit(openApiSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_OPEN_API_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("An duplicate key exception exception occurred while edit OpenApiSetting")
    public void edit_duplicateKeyException_test() {
        when(openApiSettingRepository.findById(any())).thenReturn(Optional.of(OpenApiSettingEntity.builder().build()));
        doThrow(new DuplicateKeyException("")).when(openApiSettingRepository).save(any(OpenApiSettingEntity.class));
        assertThatThrownBy(() -> openApiSettingService.edit(openApiSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isNull();
    }

    @Test
    @DisplayName("An not exist exception occurred while edit OpenApiSetting")
    public void edit_not_exist_exception_test() {
        when(openApiSettingRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> openApiSettingService.edit(openApiSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the OpenApiSetting service")
    public void list_test() {
        ArrayList<OpenApiSettingEntity> openApiSettingList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            openApiSettingList.add(OpenApiSettingEntity.builder().build());
        }
        when(openApiSettingRepository.findAll()).thenReturn(openApiSettingList);
        List<OpenApiSettingResponse> result = openApiSettingService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting OpenApiSetting list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(openApiSettingRepository).findAll();
        assertThatThrownBy(openApiSettingService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_OPEN_API_SETTING_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the OpenApiSetting service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(openApiSettingRepository.deleteByIdIn(ids)).thenReturn(1L);
        assertThat(openApiSettingService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete OpenApiSetting")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(openApiSettingRepository)
            .deleteByIdIn(ids);
        assertThatThrownBy(() -> openApiSettingService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_OPEN_API_SETTING_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the enable method in the OpenApiSetting service")
    public void enable_test() {
        List<String> ids = Collections.singletonList(ID);
        when(commonRepository.recover(ids, OpenApiSettingEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(openApiSettingService.enable(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("Test the unable method in the OpenApiSetting service")
    public void unable_test() {
        List<String> ids = Collections.singletonList(ID);
        when(commonRepository.deleteByIds(ids, OpenApiSettingEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(openApiSettingService.unable(Collections.singletonList(ID))).isTrue();
    }

}
