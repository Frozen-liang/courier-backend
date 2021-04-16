package com.sms.satp.entity.dto;

import com.sms.satp.entity.AuthInfo;
import com.sms.satp.entity.test.CaseHeader;
import com.sms.satp.entity.test.CaseParameter;
import com.sms.satp.entity.test.CaseRequestBody;
import com.sms.satp.entity.test.VerifyResponse;
import com.sms.satp.common.enums.RequestMethod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDto {

    private String id;
    private String name;
    private String interfaceId;
    private RequestMethod method;
    private String path;
    private AuthInfo authInfo;
    private List<CaseHeader> cookies;
    private List<CaseHeader> requestHeaders;
    private List<CaseParameter> queryParams;
    private List<CaseParameter> pathParams;
    private String requestBodyType;
    private CaseRequestBody requestBody;
    private VerifyResponse verifyResponse;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;

}