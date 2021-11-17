package com.sms.courier.dto.request;

import com.sms.courier.common.enums.NoticeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseJobRequest {

    private String sceneCaseId;

    private String workspaceId;

    private String caseTemplateId;

    private String projectId;

    private DataCollectionRequest dataCollectionRequest;

    private String envId;

    private NoticeType noticeType;

    private List<String> emails;
}
