package com.sms.satp.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseTemplateSearchDto extends PageDto {

    private String name;
    private String groupId;
    private boolean remove;
    private List<String> testStatus;
    private List<String> caseTag;
    private List<String> createUserName;
}
