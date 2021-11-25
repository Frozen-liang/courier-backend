package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseCountUserStatisticsResponse {

    private String username;

    private Integer count;
}
