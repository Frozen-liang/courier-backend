package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;

    private String username;

    private String groupId;

    private String email;

    private Boolean enabled;

    private String currentWorkspace;

    private List<String> associatedWorkspaces;

    private List<String> roles;

}