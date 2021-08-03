package com.sms.courier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryListRequest {

    private String username;

    private String groupId;

    private String email;

    private String workspaceId;
}