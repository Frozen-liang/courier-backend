package com.sms.courier.dto.response;

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
public class UserProfileResponse {

    private String id;

    private String username;

    private String nickname;

    private String email;

    private List<String> roles;

    @JsonProperty("isExpired")
    private boolean expired;

}