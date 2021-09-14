package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.LoginSettingRequest;
import com.sms.courier.dto.response.LoginSettingResponse;
import com.sms.courier.entity.system.LoginSettingEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for LoginSettingMapper")
class LoginSettingMapperTest {

    private final LoginSettingMapper loginSettingMapper = new LoginSettingMapperImpl();

    private static final Integer SIZE = 10;
    private static final String EMAIL_SUFFIX = "loginSetting";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the LoginSetting's entity object to a dto object")
    void entity_to_dto() {
        LoginSettingEntity loginSetting = LoginSettingEntity.builder()
            .emailSuffix(EMAIL_SUFFIX)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        LoginSettingResponse loginSettingResponse = loginSettingMapper.toDto(loginSetting);
        assertThat(loginSettingResponse.getEmailSuffix()).isEqualTo(EMAIL_SUFFIX);
    }

    @Test
    @DisplayName("Test the method for converting an LoginSetting entity list object to a dto list object")
    void loginSettingList_to_loginSettingDtoList() {
        List<LoginSettingEntity> loginSettings = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            loginSettings.add(LoginSettingEntity.builder().emailSuffix(EMAIL_SUFFIX).build());
        }
        List<LoginSettingResponse> loginSettingResponseList = loginSettingMapper.toDtoList(loginSettings);
        assertThat(loginSettingResponseList).hasSize(SIZE);
        assertThat(loginSettingResponseList)
            .allMatch(result -> StringUtils.equals(result.getEmailSuffix(), EMAIL_SUFFIX));
    }

    @Test
    @DisplayName("Test the method to convert the LoginSetting's dto object to a entity object")
    void dto_to_entity() {
        LoginSettingRequest loginSettingRequest = LoginSettingRequest.builder()
            .emailSuffix(EMAIL_SUFFIX)
            .build();
        LoginSettingEntity loginSetting = loginSettingMapper.toEntity(loginSettingRequest);
        assertThat(loginSetting.getEmailSuffix()).isEqualTo(EMAIL_SUFFIX);
    }

}
