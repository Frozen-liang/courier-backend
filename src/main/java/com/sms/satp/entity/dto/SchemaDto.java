package com.sms.satp.entity.dto;

import com.sms.satp.entity.Schema;
import com.sms.satp.parser.common.SchemaType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaDto {

    private String id;
    private String projectId;
    private String name;
    private String title;
    private List<String> required;
    private SchemaType type;
    private Map<String, Schema> properties;
    private String description;
    private Boolean deprecated;
    private String createDateTime;
    private String modifyDateTime;
}