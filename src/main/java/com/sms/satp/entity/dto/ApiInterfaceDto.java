package com.sms.satp.entity.dto;

import com.sms.satp.entity.Header;
import com.sms.satp.entity.Parameter;
import com.sms.satp.entity.RequestBody;
import com.sms.satp.entity.Response;
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
public class ApiInterfaceDto {

    private String id;
    private RequestMethod method;
    private List<String> tag;
    private String title;
    private String path;
    private String description;
    private String groupId;
    private String projectId;
    private List<Header> requestHeaders;
    private List<Header> responseHeaders;
    private List<Parameter> queryParams;
    private List<Parameter> pathParams;
    private RequestBody requestBody;
    private Response response;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;

}