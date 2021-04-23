package com.sms.satp.entity.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSceneCaseDto {

    @NotNull(message = "The id can not be empty")
    private String id;
    private String name;
    private String groupId;
    private String testStatus;
    private List<String> caseTag;
    private Integer priority;
    private boolean remove;
}
