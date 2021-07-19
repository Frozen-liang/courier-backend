package com.sms.satp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String id;

    private String password;

    private String username;

    private String groupId;

    private String email;
    @JsonProperty("isEnabled")
    private Boolean enabled;

    private List<String> associatedWorkspaces;

}