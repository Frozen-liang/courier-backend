package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_LOGIN_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_LOGIN_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_LOGIN_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_LOGIN_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_LOGIN_SETTING_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.LoginSettingRequest;
import com.sms.courier.dto.response.LoginSettingResponse;
import com.sms.courier.entity.system.LoginSettingEntity;
import com.sms.courier.mapper.LoginSettingMapper;
import com.sms.courier.mapper.LoginSettingMapperImpl;
import com.sms.courier.repository.LoginSettingRepository;
import com.sms.courier.service.impl.LoginSettingServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for LoginSettingService")
class LoginSettingServiceTest {

    private final LoginSettingRepository loginSettingRepository = mock(LoginSettingRepository.class);
    private final LoginSettingMapper loginSettingMapper = new LoginSettingMapperImpl();
    private final LoginSettingService loginSettingService = new LoginSettingServiceImpl(
        loginSettingRepository, loginSettingMapper);
    private final LoginSettingEntity loginSetting = LoginSettingEntity.builder().id(ID).build();
    private final LoginSettingResponse loginSettingResponse = LoginSettingResponse.builder()
        .id(ID).build();
    private final LoginSettingRequest loginSettingRequest = LoginSettingRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the LoginSetting service")
    public void findById_test() {
        when(loginSettingRepository.findById(ID)).thenReturn(Optional.of(loginSetting));
        LoginSettingResponse result1 = loginSettingService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting LoginSetting")
    public void findById_exception_test() {
        when(loginSettingRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> loginSettingService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_LOGIN_SETTING_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the LoginSetting service")
    public void add_test() {
        when(loginSettingRepository.insert(any(LoginSettingEntity.class))).thenReturn(loginSetting);
        assertThat(loginSettingService.add(loginSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding LoginSetting")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(loginSettingRepository).insert(any(LoginSettingEntity.class));
        assertThatThrownBy(() -> loginSettingService.add(loginSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_LOGIN_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the LoginSetting service")
    public void edit_test() {
        when(loginSettingRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(loginSettingRepository.save(any(LoginSettingEntity.class))).thenReturn(loginSetting);
        assertThat(loginSettingService.edit(loginSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit LoginSetting")
    public void edit_exception_test() {
        when(loginSettingRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(loginSettingRepository).save(any(LoginSettingEntity.class));
        assertThatThrownBy(() -> loginSettingService.edit(loginSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_LOGIN_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit LoginSetting")
    public void edit_not_exist_exception_test() {
        when(loginSettingRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> loginSettingService.edit(loginSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the LoginSetting service")
    public void list_test() {
        ArrayList<LoginSettingEntity> loginSettingList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            loginSettingList.add(LoginSettingEntity.builder().build());
        }
        when(loginSettingRepository.findAll(any(Sort.class))).thenReturn(loginSettingList);
        List<LoginSettingResponse> result = loginSettingService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting LoginSetting list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(loginSettingRepository).findAll(any(Sort.class));
        assertThatThrownBy(loginSettingService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_LOGIN_SETTING_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the LoginSetting service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(loginSettingRepository.deleteByIdIn(ids)).thenReturn(2L);
        assertThat(loginSettingService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete LoginSetting")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(loginSettingRepository)
            .deleteByIdIn(ids);
        assertThatThrownBy(() -> loginSettingService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_LOGIN_SETTING_BY_ID_ERROR.getCode());
    }

}
