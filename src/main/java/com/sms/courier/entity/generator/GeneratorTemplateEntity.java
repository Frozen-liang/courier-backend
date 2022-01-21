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

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "GeneratorTemplate")
public class GeneratorTemplateEntity extends BaseEntity {

    private String name;

    private CodeType codeType;

    private List<CodeTemplate> codeTemplates;
}
