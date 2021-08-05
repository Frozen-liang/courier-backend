package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
public class UserGroupResponse extends LookupUserResponse {

    private String name;

    @JsonProperty("isDefaultGroup")
    private boolean defaultGroup;

    private List<String> roleIds;

}