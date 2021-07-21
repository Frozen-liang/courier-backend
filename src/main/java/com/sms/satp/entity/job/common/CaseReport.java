package com.sms.satp.entity.job.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseReport {

    private String caseId;

    private Integer status;

    private String requestUrl;

    private Map<String, String> requestHeader;

    private Map<String, String> responseHeader;

    private Object responseData;

    private Object requestData;

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    private String failMessage;

    private Long timeCost;

}
