package com.sms.satp.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCaseTemplateRequest {

    @NotNull(message = "The name can not be empty")
    private String name;
    private String createUserName;
    @NotNull(message = "The projectId can not be empty")
    private String projectId;
    private String groupId;
    private String testStatus;
    private List<String> caseTag;
    private Boolean removed;
}
