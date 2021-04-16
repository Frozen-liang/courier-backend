package com.sms.satp.entity.scenetest;

import com.sms.satp.entity.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseSearchDto extends PageDto {

    private String name;
    private String groupId;
    private Integer status;
    private List<String> testStatus;
    private List<String> caseTag;
    private List<Integer> priority;
    private List<String> createUserName;

}
