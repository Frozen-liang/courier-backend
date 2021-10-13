package com.sms.courier.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryListResponse {

    private String id;

    /**
     * 修改描述.
     */
    private String description;

    private LocalDateTime createDateTime;

    private String createUserId;

    private String createUsername;

    private String createNickname;
}
