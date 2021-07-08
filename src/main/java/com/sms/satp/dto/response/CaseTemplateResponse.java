package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseTemplateResponse extends BaseResponse {

    private String name;
    private String createUserName;
    private String projectId;
    private String groupId;
    private String groupName;
    private String testStatus;
    private List<String> tagId;
    private List<String> tagName;
}
