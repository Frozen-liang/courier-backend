package com.sms.courier.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiAndCaseResponse {

    private ApiResponse apiResponse;
    private List<ApiTestCaseResponse> apiTestCase;

    @Data
    public static class ApiTestCaseResponse {

        private String caseName;
        private String id;
        private String createUsername;
    }

}
