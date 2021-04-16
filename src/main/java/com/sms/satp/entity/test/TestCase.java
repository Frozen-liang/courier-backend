package com.sms.satp.entity.test;

import com.sms.satp.entity.AuthInfo;
import com.sms.satp.common.enums.RequestMethod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "TestCase")
public class TestCase {

    @Id
    @Field("id")
    private String id;
    private String name;
    @Field("interface_id")
    private String interfaceId;
    private RequestMethod method;
    private String path;
    @Field("auth_info")
    private AuthInfo authInfo;
    private List<CaseHeader> cookies;
    @Field("request_headers")
    private List<CaseHeader> requestHeaders;
    @Field("query_params")
    private List<CaseParameter> queryParams;
    @Field("path_params")
    private List<CaseParameter> pathParams;
    private String requestBodyType;
    @Field("request_body")
    private CaseRequestBody requestBody;
    @Field("verify_response")
    private VerifyResponse verifyResponse;
    @Field("create_date_time")
    private LocalDateTime createDateTime;
    @Field("modify_date_time")
    private LocalDateTime modifyDateTime;
}