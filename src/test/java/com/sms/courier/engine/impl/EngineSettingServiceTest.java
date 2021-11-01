package com.sms.courier.engine.impl;

import static com.sms.courier.common.exception.ErrorCode.EDIT_ENGINE_SETTING_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.EngineSettingRequest;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.EngineSettingService;
import com.sms.courier.engine.model.EngineSettingEntity;
import com.sms.courier.repository.EngineSettingRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for EngineSettingService")
public class EngineSettingServiceTest {

    private final EngineSettingRepository engineSettingRepository = mock(EngineSettingRepository.class);
    private final EngineSettingService engineSettingService = new EngineSettingServiceImpl(engineSettingRepository);
    private final EngineSettingRequest request = new EngineSettingRequest();

    @DisplayName("Test for findOne in EngineSettingService")
    @Test
    public void findOne_test() {
        when(engineSettingRepository.getFirstByOrderByModifyDateTimeDesc())
            .thenReturn(EngineSettingResponse.builder().build());
        EngineSettingResponse result = engineSettingService.findOne();
        assertThat(result).isNotNull();
    }

    @DisplayName("Test for edit in EngineSettingService")
    @Test
    public void edit_test() {
        when(engineSettingRepository.findById(any())).thenReturn(Optional.of(EngineSettingEntity.builder().build()));
        Boolean result = engineSettingService.edit(request);
        assertThat(result).isTrue();
    }

    @DisplayName("An exception occurred while edit EngineSetting")
    @Test
    public void edit_exception_test() {
        when(engineSettingRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> engineSettingService.edit(request)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_ENGINE_SETTING_ERROR.getCode());
    }
}
