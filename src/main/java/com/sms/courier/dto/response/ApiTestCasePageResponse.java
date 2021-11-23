package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiTestCasePageResponse extends LookupUserResponse {

    private String caseName;

    private ApiPathAndRequestMethodResponse apiEntity;

    @Data
    private static class ApiPathAndRequestMethodResponse {

        private String apiPath;

        private Integer requestMethod;
    }
}
