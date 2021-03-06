package com.sms.courier.engine.model;

import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "EngineMember")
public class EngineMemberEntity extends BaseEntity {

    private String destination;
    private String sessionId;
    private int port;
    @Indexed(unique = true)
    private String name;
    /**
     * The engine version.
     */
    private String version;
    @Default
    private Integer taskSizeLimit = -1;
    @Default
    private Integer caseTask = 0;
    @Default
    private Integer sceneCaseTask = 0;
    @Default
    private Integer taskCount = 0;
    @Default
    private EngineStatus status = EngineStatus.PENDING;
    @Default
    private ContainerStatus containerStatus = ContainerStatus.START;
    @Default
    @Field(value = "isOpen")
    private boolean open = true;
}
