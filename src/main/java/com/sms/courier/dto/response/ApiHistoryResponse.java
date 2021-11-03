package com.sms.courier.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiHistoryResponse {

    /**
     * 历史记录.
     */
    private ApiHistoryDetailResponse record;

    private String groupName;

    private List<String> tagName;

    private String apiManager;

    private String createUsername;

    private String createNickname;
}
