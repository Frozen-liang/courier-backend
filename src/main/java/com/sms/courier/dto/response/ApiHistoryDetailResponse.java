package com.sms.courier.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryDetailResponse {

    private String id;

    private boolean remove;

    private String createUserId;

    private String createUsername;

    private String createNickname;
    
    private String modifyUserId;

    private LocalDateTime createDateTime;

    private LocalDateTime modifyDateTime;

    private String projectId;

    private String groupName;

    private String groupId;

    private List<String> tagName;

    private List<String> tagId;

    private String apiName;

    private String description;

    private String apiPath;

    private Integer apiProtocol;

    private Integer requestMethod;

    private Integer apiRequestParamType;

    private String requestRaw;

    private Integer requestRawType;

    private List<ParamInfoResponse> requestHeaders;
    private List<ParamInfoResponse> responseHeaders;
    private List<ParamInfoResponse> pathParams;
    private List<ParamInfoResponse> restfulParams;
    private List<ParamInfoResponse> requestParams;
    private List<ParamInfoResponse> responseParams;

    private Integer apiStatus;

    private String preInject;

    private String postInject;

    private String swaggerId;

    private Integer apiResponseJsonType;

    private Integer apiRequestJsonType;

    private Integer apiResponseParamType;

    private String responseRaw;

    private Integer responseRawType;

    private String richText;

    private String markdown;

    private Integer apiNodeType;

    private String apiManagerId;

    private String apiManager;

    private Integer apiEncodingType;
}
