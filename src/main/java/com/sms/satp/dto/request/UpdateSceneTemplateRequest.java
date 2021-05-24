package com.sms.satp.dto.request;

import com.sms.satp.dto.response.CaseTemplateConnResponse;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
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

    private SceneCaseResponse sceneCaseDto;

    private List<UpdateSceneCaseApiRequest> sceneCaseApiDtoList;

    private List<CaseTemplateConnResponse> caseTemplateConnDtoList;
}
