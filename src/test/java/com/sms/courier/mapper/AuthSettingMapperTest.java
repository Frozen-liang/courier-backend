package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.OAuthSettingRequest;
import com.sms.courier.dto.response.OAuthSettingResponse;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for AuthSettingMapper")
class AuthSettingMapperTest {

    private final AuthSettingMapper authSettingMapper = new AuthSettingMapperImpl();

    private static final Integer SIZE = 10;
    private static final String SCOPE = "scope";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the AuthSetting's entity object to a dto object")
    void entity_to_dto() {
        OAuthSettingEntity authSetting = OAuthSettingEntity.builder()
            .scope(SCOPE)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        OAuthSettingResponse authSettingResponse = authSettingMapper.toDto(authSetting);
        assertThat(authSettingResponse.getScope()).isEqualTo(SCOPE);
    }

    @Test
    @DisplayName("Test the method for converting an AuthSetting entity list object to a dto list object")
    void authSettingList_to_authSettingDtoList() {
        List<OAuthSettingEntity> authSettings = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            authSettings.add(OAuthSettingEntity.builder().scope(SCOPE).build());
        }
        List<OAuthSettingResponse> authSettingResponseList = authSettingMapper.toDtoList(authSettings);
        assertThat(authSettingResponseList).hasSize(SIZE);
        assertThat(authSettingResponseList).allMatch(result -> StringUtils.equals(result.getScope(), SCOPE));
    }

    @Test
    @DisplayName("Test the method to convert the AuthSetting's dto object to a entity object")
    void dto_to_entity() {
        OAuthSettingRequest authSettingRequest = OAuthSettingRequest.builder()
            .scope(SCOPE)
            .build();
        OAuthSettingEntity authSetting = authSettingMapper.toEntity(authSettingRequest);
        assertThat(authSetting.getScope()).isEqualTo(SCOPE);
    }

}
