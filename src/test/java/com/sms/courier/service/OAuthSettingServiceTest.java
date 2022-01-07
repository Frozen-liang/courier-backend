package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_AUTH_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_AUTH_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_AUTH_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_AUTH_SETTING_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.OAuthSettingRequest;
import com.sms.courier.dto.response.OAuthSettingResponse;
import com.sms.courier.mapper.AuthSettingMapper;
import com.sms.courier.mapper.AuthSettingMapperImpl;
import com.sms.courier.repository.OAuthSettingRepository;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.security.oauth.OAuthType;
import com.sms.courier.service.impl.OAuthSettingServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for AuthSettingService")
class OAuthSettingServiceTest {

    private final OAuthSettingRepository oauthSettingRepository = mock(OAuthSettingRepository.class);
    private final AuthSettingMapper authSettingMapper = new AuthSettingMapperImpl();
    private final OAuthSettingService oauthSettingService = new OAuthSettingServiceImpl(
        oauthSettingRepository, authSettingMapper);
    private final OAuthSettingEntity authSetting = OAuthSettingEntity.builder().id(ID).authType(OAuthType.NERKO)
        .build();
    private final OAuthSettingRequest authSettingRequest = OAuthSettingRequest.builder().clientSecret("test")
        .authType(OAuthType.NERKO)
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;

    @Test
    @DisplayName("Test the findById method in the AuthSetting service")
    public void findById_test() {
        when(oauthSettingRepository.findById(ID)).thenReturn(Optional.of(authSetting));
        OAuthSettingResponse result1 = oauthSettingService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting AuthSetting")
    public void findById_exception_test() {
        when(oauthSettingRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> oauthSettingService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_AUTH_SETTING_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the AuthSetting service")
    public void add_test() {
        when(oauthSettingRepository.insert(any(OAuthSettingEntity.class))).thenReturn(authSetting);
        when(oauthSettingRepository.existsByAuthType(any())).thenReturn(false);
        assertThat(oauthSettingService.add(authSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding AuthSetting")
    public void add_exception_test() {
        when(oauthSettingRepository.existsByAuthType(any())).thenReturn(false);
        doThrow(new RuntimeException()).when(oauthSettingRepository).insert(any(OAuthSettingEntity.class));
        assertThatThrownBy(() -> oauthSettingService.add(authSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_AUTH_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the AuthSetting service")
    public void edit_test() {
        when(oauthSettingRepository.findById(any())).thenReturn(Optional.of(authSetting));
        when(oauthSettingRepository.existsByAuthType(any())).thenReturn(false);
        when(oauthSettingRepository.save(any(OAuthSettingEntity.class))).thenReturn(authSetting);
        assertThat(oauthSettingService.edit(authSettingRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit AuthSetting")
    public void edit_exception_test() {
        OAuthSettingRequest request = OAuthSettingRequest.builder().build();
        when(oauthSettingRepository.findById(any())).thenReturn(Optional.of(OAuthSettingEntity.builder().authType(
            OAuthType.NERKO).build()));
        doThrow(new RuntimeException()).when(oauthSettingRepository).save(any(OAuthSettingEntity.class));
        assertThatThrownBy(() -> oauthSettingService.edit(request))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_AUTH_SETTING_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit AuthSetting")
    public void edit_not_exist_exception_test() {
        when(oauthSettingRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> oauthSettingService.edit(authSettingRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the AuthSetting service")
    public void list_test() {
        List<OAuthSettingEntity> authSettingList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            authSettingList.add(OAuthSettingEntity.builder().build());
        }
        when(oauthSettingRepository.findAll()).thenReturn(authSettingList);
        List<OAuthSettingResponse> result = oauthSettingService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting AuthSetting list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(oauthSettingRepository).findAll();
        assertThatThrownBy(oauthSettingService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_AUTH_SETTING_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the AuthSetting service")
    public void delete_test() {
        doNothing().when(oauthSettingRepository).deleteById(ID);
        assertThat(oauthSettingService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete AuthSetting")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(oauthSettingRepository)
            .deleteById(ID);
        assertThatThrownBy(() -> oauthSettingService.delete(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_AUTH_SETTING_BY_ID_ERROR.getCode());
    }

}
