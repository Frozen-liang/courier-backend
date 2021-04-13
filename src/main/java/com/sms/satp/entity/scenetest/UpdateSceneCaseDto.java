package com.sms.satp.entity.scenetest;

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
    private ObjectId id;
    private String name;
    private String groupId;
    private String testStatus;
    private List<String> caseTag;
    private Integer priority;
    private Integer status;
    @NotNull(message = "The modifyUserId can not be empty")
    private String modifyUserId;
}
