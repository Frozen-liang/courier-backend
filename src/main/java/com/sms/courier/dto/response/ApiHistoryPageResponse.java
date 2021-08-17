package com.sms.courier.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiHistoryPageResponse {

    private String id;
    private String apiId;
    private LocalDateTime createDateTime;
    private String createUsername;
    private String createNickname;
    private String description;

}
