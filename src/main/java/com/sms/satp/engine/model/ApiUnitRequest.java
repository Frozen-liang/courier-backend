package com.sms.satp.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.satp.parser.common.HttpMethod;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ApiUnitRequest {

    @JsonProperty("server_address")
    private String serverAddress;
    private String path;
    private Object body;
    @JsonProperty("http_method")
    private HttpMethod httpMethod;
    private Map<String, ?> cookies;
    private Map<String, ?> headers;
    @JsonProperty("query_params")
    private Map<String, ?> queryParams;
    @JsonProperty("path_params")
    private Map<String, ?> pathParams;
    @JsonProperty("form_params")
    private Map<String, ?> formParams;
    @JsonProperty("multi_parts")
    private List<MultiPart> multiParts;
    @JsonProperty("enable_execution_time")
    private boolean enableExecutionTime;


}
