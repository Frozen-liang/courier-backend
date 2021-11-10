package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseJobResponse extends JobResponse {

    @JsonProperty("isNext")
    private boolean next;

    private List<JobSceneCaseApiResponse> apiTestCase;

    /**
     * 测试人员.
     */
    private String createUserName;

}
