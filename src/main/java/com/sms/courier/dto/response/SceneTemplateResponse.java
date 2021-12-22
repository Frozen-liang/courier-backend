package com.sms.courier.dto.response;

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

    private SceneCaseConnResponse sceneCaseDto;

    private List<SceneCaseApiConnResponse> sceneCaseApiDtoList;
}
