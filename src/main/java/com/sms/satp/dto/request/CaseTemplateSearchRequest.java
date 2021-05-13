package com.sms.satp.dto.request;

import com.sms.satp.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseTemplateSearchRequest extends PageDto {

    private String name;
    private String groupId;
    private boolean remove;
    private List<String> testStatus;
    private List<String> caseTag;
    private List<String> createUserName;
}
