package com.sms.satp.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneTemplateRequest {

    private UpdateSceneCaseRequest updateSceneCaseRequest;

    private List<UpdateSceneCaseApiRequest> updateSceneCaseApiRequests;

    private List<UpdateCaseTemplateConnRequest> updateCaseTemplateConnRequests;
}
