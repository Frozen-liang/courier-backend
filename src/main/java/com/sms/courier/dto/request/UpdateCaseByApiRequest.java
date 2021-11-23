package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCaseByApiRequest {

    @NotNull(message = "The api must not be null!")
    private ApiRequest api;

    @NotNull(message = "The caseList must not be null!")
    @Size(min = 1, message = "The caseList size must not be empty!")
    private List<CaseRequest> caseList;

    @Data
    public static class CaseRequest {

        private String id;
        @JsonProperty("isReplace")
        private boolean replace;
    }
}