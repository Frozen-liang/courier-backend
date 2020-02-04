package com.sms.satp.parser.model;

import com.sms.satp.parser.common.HttpMethod;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiOperation {

    private HttpMethod httpMethod;
    private List<String> tags;
    private String summary;
    private String description;
    private String operationId;
    private List<ApiParameter> parameters;
    private ApiRequestBody apiRequestBody;
    private ApiResponse apiResponse;
    private Boolean deprecated;


}
