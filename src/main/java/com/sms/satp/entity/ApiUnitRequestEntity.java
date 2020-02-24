package com.sms.satp.entity;

import com.sms.satp.engine.model.MultiPart;
import com.sms.satp.parser.common.HttpMethod;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ApiUnitRequest")
public class ApiUnitRequestEntity {

    private String serverAddress;
    private String path;
    private Object body;
    private HttpMethod httpMethod;
    private Map<String, ?> cookies;
    private Map<String, ?> headers;
    private Map<String, ?> queryParams;
    private Map<String, ?> pathParams;
    private Map<String, ?> formParams;
    private List<MultiPart> multiParts;
    private boolean enableExecutionTime;
}