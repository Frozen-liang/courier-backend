package com.sms.satp.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String id;

    private String password;

    private String username;

    private String groupId;

    private String email;

    private Boolean enabled;

    private List<String> associatedWorkspaces;






}