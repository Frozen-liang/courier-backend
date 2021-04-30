package com.sms.satp.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneTemplateDto {

    private SceneCaseDto sceneCaseDto;

    private List<SceneCaseApiDto> sceneCaseApiDtoList;

    private List<CaseTemplateConnDto> caseTemplateConnDtoList;
}
