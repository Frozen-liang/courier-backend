package com.sms.courier.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiCountIdsDto {

    private List<String> sceneCaseCountApiIds;

    private List<String> otherObjectSceneCaseCountApiIds;
}
