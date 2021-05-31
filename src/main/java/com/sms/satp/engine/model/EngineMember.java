package com.sms.satp.engine.model;

import com.sms.satp.engine.enums.EngineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngineMember {

    private String destination;
    private String sessionId;
    private String host;
    private String name;
    /**
     * The engine version.
     */
    private String version;
    @Default
    private Integer taskSizeLimit = -1;
    @Default
    private Integer currentTaskSize = 0;
    @Default
    private EngineStatus status = EngineStatus.PENDING;
}
