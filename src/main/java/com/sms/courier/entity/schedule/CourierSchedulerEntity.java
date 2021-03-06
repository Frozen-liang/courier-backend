package com.sms.courier.entity.schedule;

import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.entity.BaseEntity;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "CourierScheduler")
public class CourierSchedulerEntity extends BaseEntity {

    private String imageName;
    private String containerName;
    private String version;
    private Map<String, String> envVariable;
    private ContainerStatus containerStatus;
}
