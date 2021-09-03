package com.sms.courier.entity.structure;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "ApiStructureRefRecord")
public class ApiStructureRefRecordEntity {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    private String name;

    private List<String> refStructIds;

}
