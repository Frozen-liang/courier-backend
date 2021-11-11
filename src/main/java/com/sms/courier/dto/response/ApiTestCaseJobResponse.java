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
public class ApiTestCaseJobResponse extends JobResponse {

    private JobCaseApiResponse apiTestCase;


    /**
     * 测试人员.
     */
    private String createUserName;

    private Integer time;

}
