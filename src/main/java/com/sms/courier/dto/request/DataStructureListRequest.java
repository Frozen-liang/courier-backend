package com.sms.courier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataStructureListRequest {

    private String id;

    private String name;

    private String projectId;

    private String workspaceId;

    private Integer structType;

    private String description;
}
