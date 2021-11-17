package com.sms.courier.engine.model;

import com.sms.courier.entity.BaseEntity;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "EngineSetting")
public class EngineSettingEntity extends BaseEntity {

    private String imageName;
    private String containerName;
    private String version;
    private Integer taskSizeLimit;
    private Map<String, String> envVariable;
}
