package com.sms.satp.entity.group;

import com.sms.satp.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document("ApiGroup")
public class ApiGroupEntity extends BaseEntity {

    private String projectId;
    private String groupName;
}
