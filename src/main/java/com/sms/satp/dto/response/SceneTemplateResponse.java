package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneTemplateResponse {

    private SceneCaseResponse sceneCaseDto;

    private List<SceneCaseApiResponse> sceneCaseApiDtoList;

    private List<CaseTemplateConnResponse> caseTemplateConnDtoList;
}
