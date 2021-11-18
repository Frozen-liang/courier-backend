package com.sms.courier.dto.request;

import com.sms.courier.common.enums.NoticeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTestCaseJobRunRequest {

    private List<String> apiTestCaseIds;

    private String envId;

    private String workspaceId;

    private DataCollectionRequest dataCollectionRequest;

    private NoticeType noticeType;

    private List<String> emails;
}
