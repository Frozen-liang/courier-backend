package com.sms.satp.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneCaseApiDto {

    @NotNull(message = "The sceneCaseApiDto can not be empty")
    private SceneCaseApiDto sceneCaseApiDto;

    @NotNull(message = "The currentUserId can not be empty")
    private String currentUserId;
}
