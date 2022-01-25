package com.sms.courier.entity.generator;

import com.sms.courier.entity.BaseEntity;
import com.sms.courier.generator.enums.CodeType;
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

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "GeneratorTemplate")
public class GeneratorTemplateEntity extends BaseEntity {

    private String name;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private CodeType codeType;

    @Field("isDefaultTemplate")
    private boolean defaultTemplate;

    private List<CodeTemplate> codeTemplates;
}
