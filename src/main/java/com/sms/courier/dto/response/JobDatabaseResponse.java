package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobDatabaseResponse {

    private String id;

    private String projectId;

    private String name;

    private Integer databaseType;

    private String url;

    private String port;

    private String username;

    private String password;

    private String dataBaseName;
}
