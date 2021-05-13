package com.sms.satp.entity.datacollection;

import com.sms.satp.entity.BaseEntity;
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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "DataCollection")
public class DataCollection extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    private String collectionName;
    private List<String> paramList;
    private List<TestData> dataList;
}
