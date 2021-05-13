package com.sms.satp.entity.group;

import com.sms.satp.entity.BaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Document("ApiGroup")
public class ApiGroupEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    private String projectId;
    @EqualsAndHashCode.Include
    private String name;
}
