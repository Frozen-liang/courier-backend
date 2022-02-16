package com.sms.courier.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.dto.response.StructureRefResponse;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test for ReplaceHelper")
public class ReplaceHelperTest {

    private final String MOCK_ID = new ObjectId().toString();
    private final String MOCK_DES = "test";
    private final Integer MOCK_TYPE = 0;

    @Test
    @DisplayName("Test replaceCustomStruct method for ReplaceHelperTest")
    public void replaceCustomStruct_test() throws JsonProcessingException {
        ApiResponse apiResponse = ApiResponse.builder()
            .requestParams(Lists.newArrayList(
                ParamInfoResponse.builder()
                    .ref(Boolean.TRUE)
                    .customStructs(
                        Lists.newArrayList(ParamInfoResponse.builder()
                            .paramId(MOCK_ID).key(MOCK_DES).paramType(MOCK_TYPE).description(MOCK_DES)
                            .childParam(Lists.newArrayList(ParamInfoResponse.builder().build()))
                            .build()))
                    .structureRef(StructureRefResponse.builder()
                        .struct(Lists.newArrayList(ParamInfoResponse.builder().paramId(MOCK_ID).build()))
                        .build())
                    .build()
            )).build();
        String str = new ReplaceHelper(new ObjectMapper().writeValueAsString(apiResponse.getRequestParams()))
            .replaceCustomStruct()
            .toInfoList();
        assertThat(str).isNotNull();
    }

}
