package com.sms.satp.entity.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceCriteria {
    private String projectId;
    private String groupId;
    private String title;
    private String tag;
    private String path;
}