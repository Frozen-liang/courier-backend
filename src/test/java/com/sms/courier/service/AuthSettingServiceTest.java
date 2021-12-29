package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AUTH_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AuthSettingRequest;
import com.sms.courier.dto.response.AuthSettingResponse;
import com.sms.courier.mapper.AuthSettingMapper;
import com.sms.courier.mapper.AuthSettingMapperImpl;
import com.sms.courier.repository.AuthSettingRepository;
import com.sms.courier.security.oauth.AuthSettingEntity;
import com.sms.courier.service.impl.AuthSettingServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for AuthSettingService")
class AuthSettingServiceTest {

    private final AuthSettingRepository authSettingRepository = mock(AuthSettingRepository.class);
    private final AuthSettingMapper authSettingMapper = new AuthSettingMapperImpl();
    private final AuthSettingService authSettingService = new AuthSettingServiceImpl(
        authSettingRepository, authSettingMapper);
    private final AuthSettingEntity authSetting = AuthSettingEntity.builder().id(ID).build();
    private final AuthSettingResponse authSettingResponse = AuthSettingResponse.builder()
        .id(ID).build();
    private final AuthSettingRequest authSettingRequest = AuthSettingRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = ObjectId.get().toString();

    /*@Test
    @DisplayName("Test the findById method in the AuthSetting service")
    public void findById_test() {
        when(authSettingRepository.findById(ID)).thenReturn(Optional.of(authSetting));
        AuthSettingResponse result1 = authSettingService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting AuthSetting")
    public void findById_exception_test() {
        when(authSettingRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authSettingService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_AUTH_SETTING_BY_ID_ERROR.getCode());
    }*/

    @Test
    @DisplayName("Test the add method in the AuthSetting service")
    public void add_test() {
        when(authSettingRepository.insert(any(AuthSettingEntity.class))).thenReturn(authSetting);
        assertThat(authSettingService.add(authSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding AuthSetting")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(authSettingRepository).insert(any(AuthSettingEntity.class));
        assertThatThrownBy(() -> authSettingService.add(authSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_AUTH_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the AuthSetting service")
    public void edit_test() {
        when(authSettingRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(authSettingRepository.save(any(AuthSettingEntity.class))).thenReturn(authSetting);
        assertThat(authSettingService.edit(authSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit AuthSetting")
    public void edit_exception_test() {
        when(authSettingRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(authSettingRepository).save(any(AuthSettingEntity.class));
        assertThatThrownBy(() -> authSettingService.edit(authSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_AUTH_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit AuthSetting")
    public void edit_not_exist_exception_test() {
        when(authSettingRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> authSettingService.edit(authSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

   /* @Test
    @DisplayName("Test the list method in the AuthSetting service")
    public void list_test() {
        ArrayList<AuthSettingEntity> authSettingList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            authSettingList.add(AuthSettingEntity.builder().build());
        }
        when(authSettingRepository.findAll(any(), any(Sort.class))).thenReturn(authSettingList);
        List<AuthSettingResponse> result = authSettingService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting AuthSetting list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(authSettingRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(authSettingService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_AUTH_SETTING_LIST_ERROR.getCode());
    }*/

    @Test
    @DisplayName("Test the delete method in the AuthSetting service")
    public void delete_test() {
        doNothing().when(authSettingRepository).deleteById(ID);
        assertThat(authSettingService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete AuthSetting")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(authSettingRepository)
            .deleteById(ID);
        assertThatThrownBy(() -> authSettingService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_AUTH_SETTING_BY_ID_ERROR.getCode());
    }

}
