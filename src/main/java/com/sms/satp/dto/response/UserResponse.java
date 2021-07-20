package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("isEnabled")
    private boolean enabled;

    private String currentWorkspace;

    private List<String> associatedWorkspaces;

    private List<String> roles;

}