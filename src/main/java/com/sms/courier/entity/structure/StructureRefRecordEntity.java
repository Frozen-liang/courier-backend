package com.sms.courier.entity.structure;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "StructureRefRecord")
public class StructureRefRecordEntity {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    @Exclude
    private String name;

    @DBRef
    @Exclude
    private List<StructureRefRecordEntity> structureRef;

}
