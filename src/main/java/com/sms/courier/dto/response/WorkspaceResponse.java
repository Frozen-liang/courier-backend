package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WorkspaceResponse extends LookupUserResponse {

    private String name;

    private Integer limit;

    private Long allCaseCount;

    private Long recentCaseCount;
}