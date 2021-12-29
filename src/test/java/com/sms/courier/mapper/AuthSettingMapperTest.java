package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.AuthSettingRequest;
import com.sms.courier.dto.response.AuthSettingResponse;
import com.sms.courier.security.oauth.AuthSettingEntity;
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
        AuthSettingEntity authSetting = AuthSettingEntity.builder()
            .scope(SCOPE)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        AuthSettingResponse authSettingResponse = authSettingMapper.toDto(authSetting);
        assertThat(authSettingResponse.getScope()).isEqualTo(SCOPE);
    }

    @Test
    @DisplayName("Test the method for converting an AuthSetting entity list object to a dto list object")
    void authSettingList_to_authSettingDtoList() {
        List<AuthSettingEntity> authSettings = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            authSettings.add(AuthSettingEntity.builder().scope(SCOPE).build());
        }
        List<AuthSettingResponse> authSettingResponseList = authSettingMapper.toDtoList(authSettings);
        assertThat(authSettingResponseList).hasSize(SIZE);
        assertThat(authSettingResponseList).allMatch(result -> StringUtils.equals(result.getScope(), SCOPE));
    }

    @Test
    @DisplayName("Test the method to convert the AuthSetting's dto object to a entity object")
    void dto_to_entity() {
        AuthSettingRequest authSettingRequest = AuthSettingRequest.builder()
            .scope(SCOPE)
            .build();
        AuthSettingEntity authSetting = authSettingMapper.toEntity(authSettingRequest);
        assertThat(authSetting.getScope()).isEqualTo(SCOPE);
    }

}
