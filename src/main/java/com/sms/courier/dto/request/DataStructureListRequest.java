package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataStructureListRequest extends PageDto {

    private String id;

    private String name;

    private String projectId;

    private String workspaceId;

    private Integer structType;

    private String description;
}
