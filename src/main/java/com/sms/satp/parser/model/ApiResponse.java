package com.sms.satp.parser.model;

import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.schema.ApiSchema;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {

    private String description;
    private List<ApiHeader> headers;
    private List<MediaType> mediaTypes;
    private ApiSchema schema;
}
