package com.sms.courier.entity.generator;

import com.sms.courier.common.enums.TemplateType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "GeneratorTemplateType")
public class GeneratorTemplateTypeEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Indexed(unique = true)
    private TemplateType templateType;

    private List<CodeFieldDesc> fieldDescList;
}
