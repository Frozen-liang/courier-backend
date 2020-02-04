package com.sms.satp.parser.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiPath {

    private String path;
    /**
     * The summary property from a PathItem instance.
     */
    private String summary;
    /**
     * The description property from a PathItem instance.
     */
    private String description;
    /**
     * The get/put/post/delete/options/head/patch/trace property from a PathItem instance.
     */
    private List<ApiOperation> operations;

}
