package com.sms.courier.mapper;

import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for MockSettingMapper")
public class MockSettingMapperTest {

    private final MockSettingMapper mockSettingMapper = new MockSettingMapperImpl();
    private static final String MOCK_ID = "1";

    @Test
    void toEntity_test(){
        MockSettingRequest request = MockSettingRequest.builder()
            .id(MOCK_ID)
            .build();
        MockSettingEntity entity = mockSettingMapper.toEntity(request);
        assertThat(entity).isNotNull();
    }

    @Test
    void toEntityRequestIsNull_test(){
        MockSettingEntity entity = mockSettingMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void toResponse_test(){
        MockSettingEntity entity = MockSettingEntity.builder()
            .id(MOCK_ID)
            .build();
        MockSettingResponse response = mockSettingMapper.toResponse(entity);
        assertThat(response).isNotNull();
    }

    @Test
    void toResponseRequestIsNull_test(){
        MockSettingResponse response = mockSettingMapper.toResponse(null);
        assertThat(response).isNull();
    }

}
