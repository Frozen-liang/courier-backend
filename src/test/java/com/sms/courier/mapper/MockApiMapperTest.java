package com.sms.courier.mapper;

import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.MockApiResponse;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.mock.MockApiEntity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Tests for MockApiMapper")
public class MockApiMapperTest {

    private final ParamInfoMapper paramInfoMapper = mock(ParamInfoMapper.class);
    private final MatchParamInfoMapper matchParamInfoMapper = mock(MatchParamInfoMapper.class);

    private final MockApiMapper mockApiMapper = new MockApiMapperImpl(paramInfoMapper, matchParamInfoMapper);
    private static final String MOCK_ID = "1";
    private static final String MOCK_NAME = "test";

    @Test
    void toEntity_test() {
        MockApiRequest request = getRequest();
        MockApiEntity entity = mockApiMapper.toEntity(request);
        assertThat(entity).isNotNull();
    }

    @Test
    void toEntityRequestIsNull_test() {
        MockApiEntity entity = mockApiMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void toResponse_test() {
        MockApiEntity entity = getEntity();
        MockApiResponse response = mockApiMapper.toResponse(entity);
        assertThat(response).isNotNull();
    }

    @Test
    void toResponseRequestParamsIsNotNull_test() {
        MockApiEntity entity = MockApiEntity.builder()
            .id(MOCK_ID)
            .mockName(MOCK_NAME)
            .requestParams(Lists.newArrayList(MatchParamInfo.builder().build()))
            .build();
        MockApiResponse response = mockApiMapper.toResponse(entity);
        assertThat(response).isNotNull();
    }

    @Test
    void toResponseRequestIsNull_test() {
        MockApiResponse response = mockApiMapper.toResponse(null);
        assertThat(response).isNull();
    }

    private MockApiEntity getEntity() {
        return MockApiEntity.builder()
            .id(MOCK_ID)
            .mockName(MOCK_NAME)
            .build();
    }

    private MockApiRequest getRequest() {
        return MockApiRequest.builder()
            .id(MOCK_ID)
            .mockName(MOCK_NAME)
            .build();
    }

}
