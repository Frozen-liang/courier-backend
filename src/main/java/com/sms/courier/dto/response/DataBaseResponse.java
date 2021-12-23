package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DataBaseResponse extends BaseResponse {

    private String projectId;

    private String name;

    private Integer databaseType;

    private String url;

    private String port;

    private String username;

    private String dataBaseName;
}
