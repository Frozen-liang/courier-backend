package com.sms.satp.entity.job.common;

import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.datacollection.TestData;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class JobDataCollection {

    private String id;

    private String projectId;

    private String collectionName;

    private TestData testData;
}
