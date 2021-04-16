package com.sms.satp.parser.model;

import com.sms.satp.common.enums.RequestMethod;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiOperation {

    private RequestMethod requestMethod;
    private List<String> tags;
    private String summary;
    private String description;
    private String operationId;
    private List<ApiParameter> parameters;
    private ApiRequestBody apiRequestBody;
    private ApiResponse apiResponse;
    private Boolean deprecated;


}
