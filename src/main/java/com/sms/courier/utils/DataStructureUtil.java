package com.sms.courier.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.generator.ReplaceHelper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

@Slf4j
public class DataStructureUtil {

    private DataStructureUtil() {

    }

    public static List<ParamInfoResponse> resetApiRequest(List<ParamInfoResponse> responseList) {
        try {
            String jsonString = new ObjectMapper().writeValueAsString(responseList);
            String responseStr = new ReplaceHelper(jsonString).replaceCustomStruct().toInfoList();

            List<ParamInfoResponse> paramInfoResponseList = new ObjectMapper()
                .readValue(responseStr, new TypeReference<List<ParamInfoResponse>>() {
                });

            List<ParamInfoResponse> responses = Lists.newArrayList();
            for (ParamInfoResponse infoResponse : paramInfoResponseList) {
                if (infoResponse.isRef()) {
                    resetStruct(responses, infoResponse);
                } else {
                    responses.add(infoResponse);
                }
            }
            return responses;
        } catch (JsonProcessingException e) {
            log.error("Entity serialization error!");
            throw ExceptionUtils.mpe(ErrorCode.ENTITY_SERIALIZATION_ERROR);
        }
    }

    private static void resetStruct(List<ParamInfoResponse> responses, ParamInfoResponse infoResponse) {
        if (infoResponse.isRef()) {
            for (ParamInfoResponse response : infoResponse.getStructureRef().getStruct()) {
                resetStruct(responses, response);
            }
        } else {
            responses.add(infoResponse);
        }
    }

}
